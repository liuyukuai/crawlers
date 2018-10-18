package com.itxiaoer.spider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import sun.jvm.hotspot.debugger.linux.sparc.LinuxSPARCThreadContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Spider {

    public Map<String, Object> find(String domain, String cookie) {
        Map<String, Object> map = Maps.newHashMap();
        //  1
        UrlFinder urlFinder = new UrlFinder(new Connection(domain, cookie));
        urlFinder.parse();

        //List<String> un_reach = urlFinder.getUN_REACH();
        List<String> url_list = urlFinder.getURL_LIST();
        //map.put("二级页面数量-不可达", un_reach.size());
        map.put("二级页面数量-可达", url_list.size());

        List<String> collects = url_list.stream().distinct().collect(Collectors.toList());

        //外链
       // collects.removeAll(subDomainList);

        List<String> otherDomains = collects.stream().filter(u -> !u.contains(this.subDomain(domain)) && !u.contains("www" + this.subDomain(domain))).collect(Collectors.toList());

        otherDomains = otherDomains.stream().map(u -> host(u)).distinct().collect(Collectors.toList());

        map.put("外链", otherDomains);

        // 子域名数量

        collects.removeAll(otherDomains);
        collects.stream().forEach(System.out::println);
        List<String> subDomainList = collects.stream().filter(u -> u.contains(this.subDomain(domain)) && !u.contains("www" + this.subDomain(domain))).collect(Collectors.toList());
       // System.out.println();
        //subDomainList.stream().forEach(System.out::println);
        subDomainList = subDomainList.stream().map(u -> host(u)).distinct().collect(Collectors.toList());

        map.put("子域名列表", subDomainList);


        // 2
        List<String> urls = Lists.newArrayList();
        List<String> unreach = Lists.newArrayList();

        url_list.forEach(e -> {
            UrlFinder urlFinder_ = new UrlFinder(new Connection(e, cookie));
            urlFinder_.parse();

            if (!urlFinder_.getUN_REACH().isEmpty()) {
                unreach.addAll(urlFinder_.getUN_REACH());
            }
            if (!urlFinder_.getURL_LIST().isEmpty()) {
                urls.addAll(urlFinder_.getURL_LIST());
            }
        });

        map.put("三级页面数量-不可达", unreach.size());
        map.put("三级页面数量-可达", urls.size());


        return map;
    }

    private String host(String domain) {
        if (domain.startsWith("https://")) {
            System.out.println("https "+domain);
            domain = domain.replaceAll("https://", "");
        } else {
            domain = domain.replaceAll("http://", "");
        }
        if (domain.indexOf("/") != -1) {

            return domain.substring(0, domain.indexOf("/"));
        }
        return domain;
    }


    private String subDomain(String domain) {
        if (domain.startsWith("https://")) {
            System.out.println("https "+domain);
            domain = domain.replaceAll("https://", "");
        } else {
            domain = domain.replaceAll("http://", "");
        }
        return domain.substring(domain.indexOf("."), domain.length());
    }
}

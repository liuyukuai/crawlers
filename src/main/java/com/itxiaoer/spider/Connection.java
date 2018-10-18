package com.itxiaoer.spider;

import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Connection {

    private String domain;

    private String cookie;


    private List<String> un_reach;

    public Connection(String domain, String cookie) {
        this.domain = domain;
        this.cookie = cookie;
    }

    public BufferedReader connection() {
        un_reach = new ArrayList<>();
        try {

            URL URL = new URL(this.domain);
            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3468.0 Safari/537.36");
            connection.setRequestProperty("Cookie", this.cookie);
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", " zh-CN,zh;q=0.9");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("Connection", " keep-alive");
            connection.setRequestProperty("Referer", this.domain + "/");
            connection.setRequestProperty("Host", this.host());
            connection.setConnectTimeout(1000 * 60);
            connection.setInstanceFollowRedirects(false);
            // result
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            return new BufferedReader(isr);
        } catch (Exception e) {
            un_reach.add(this.domain);
            return null;
        }
    }


    private String host() {
        if (this.getDomain().startsWith("https://")) {
            return this.getDomain().replaceAll("https://", "");
        }
        return this.getDomain().replaceAll("http://", "");
    }


    public String getDomain() {
        String host = "";
        String prefix = "";
        if (this.domain.startsWith("https://")) {
            host = this.domain.replaceAll("https://", "");
            prefix = "https://";
        } else {
            host = this.domain.replaceAll("http://", "");
            prefix = "http://";
        }
        if (host.contains("/")) {
            host = host.substring(0, host.indexOf("/"));
        }
        return prefix + host + "/";
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public List<String> getUn_reach() {
        return un_reach;
    }

    public void setUn_reach(List<String> un_reach) {
        this.un_reach = un_reach;
    }
}

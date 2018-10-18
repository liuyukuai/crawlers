package com.itxiaoer.spider;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

public class Main {
    private final static String DOMAIN = "http://www.nmg.gov.cn/col/col4191/index.html";
    //private final static String DOMAIN = "http://www.bjhd.gov.cn";//海淀区政府
   // private final static String DOMAIN = "http://www.miit.gov.cn";//工信部
   //  private final static String DOMAIN = "http://www.sasac.gov.cn";//国资委
    // private final static String DOMAIN = "http://www.caih.com/";//中国东信
    private final static String COOKIE = "JSESSIONID=3668B3A23F7AF41DB53981D02CF9716E;";

    public static void main(String[] args) {


        Map<String, Object> stringObjectMap = new Spider().find(DOMAIN, COOKIE);

        stringObjectMap.forEach((k, v) -> {
            if (v instanceof List) {
                List _v = (List) v;
               // System.out.println(k);
               _v.stream().forEach(System.out::println);
                System.out.println(k + " = " + _v.size());
            } else {
                System.out.println(k + " = " + v);
            }
        });


    }
}

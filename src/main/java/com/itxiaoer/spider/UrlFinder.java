package com.itxiaoer.spider;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class UrlFinder {
    private Connection connection;

    private List<String> UN_REACH;

    private List<String> URL_LIST;

    public UrlFinder(Connection connection) {
        this.connection = connection;
    }

    public void find() {
        URL_LIST = new ArrayList<>();
        BufferedReader connection = this.connection.connection();
        if (connection != null) {
            try {
                String str = null, rs = null;
                while ((str = connection.readLine()) != null) {
                    rs = get(str);
//                    System.out.println(str);
                    if (!rs.isEmpty()) {
                        URL_LIST.add(rs);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        UN_REACH = new ArrayList<>();
        UN_REACH.addAll(this.connection.getUn_reach());
    }

    public void parse() {
        this.find();
        this.URL_LIST = this.parse(this.URL_LIST);
        this.UN_REACH = this.parse(this.UN_REACH);
    }

    private List<String> parse(List<String> hrefs) {
        List<String> urls = new ArrayList<>();
        try {
            this.URL_LIST.forEach(e -> {
                int i1 = e.indexOf("href=");
                int i2 = e.indexOf("</a>");
                System.out.println(e + "-- " + i1 + "-" + i2);

                String substring = e.substring(i1, i2).replace("href=", "");
                if (substring.startsWith("\"")) {
                    substring = substring.substring(1, substring.length());
                }
                String[] split = substring.split(">");
                String url_1 = split[0];
                String url = url_1.split(" ")[0];

//                System.out.println("-----" + url);
                if (url.contains("javascript") || url.startsWith("#")) {
                    return;
                }//http://青海省人民政府.政务.cn

                if (url.contains("http://青海省人民政府.政务.cn")) {
                    return;
                }

                if (url.endsWith("\"")) {
                    url = url.substring(0, url.length() - 1);
                }

                if (url.startsWith("/")) {
                    url = this.connection.getDomain() + url;
                }
                if (!url.startsWith("http")) {
                    url = this.connection.getDomain() + "/" + url;
                }

                urls.add(url);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }


    private String get(String line) {
        Pattern pattern = Pattern
                .compile("<a.*?href=[\"']?((https?://)?/?[^\"']+)[\"']?.*?>(.+)</a>");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            return matcher.group(0);
        return "";
    }


    private String charset(String content) {
        content = Optional.ofNullable(content).orElse("");
        Pattern pattern = Pattern.compile("charset=.*");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(0).split("charset=")[1];
        return "";
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List<String> getUN_REACH() {
        return UN_REACH;
    }

    public void setUN_REACH(List<String> UN_REACH) {
        this.UN_REACH = UN_REACH;
    }

    public List<String> getURL_LIST() {
        return URL_LIST;
    }

    public void setURL_LIST(List<String> URL_LIST) {
        this.URL_LIST = URL_LIST;
    }
}

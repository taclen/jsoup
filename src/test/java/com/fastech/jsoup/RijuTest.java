package com.fastech.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class RijuTest {

    public static String search(String keyword){
        //http://www.zhuixinfan.com/search.php?mod=tvplay&searchid=5793&searchsubmit=yes&kw=%E5%8D%8A%E6%B3%BD%E7%9B%B4%E6%A0%91
        Document doc = null;//首页
        try {
            doc = Jsoup.connect("http://www.zhuixinfan.com/search.php?mod=tvplay&searchid=6357&searchsubmit=yes&kw=" + URLEncoder.encode(keyword, "utf-8")).timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //#wp > table > tbody > tr:nth-child(2) > td.td2 > a
        Elements sts = doc.select("#wp > table > tbody > tr:nth-child(2) > td.td2 > a");
        if (null != sts && sts.size() > 0) return sts.first().attr("href");

        return null;
    }
    public static Set<String> getMagnets(String url){
        Set<String> urls = new HashSet<>();

        Document doc = null;//首页
        try {
            doc = Jsoup.connect(url).timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //#ajax_tbody > tr:nth-child(1) > td.td2
        //#ajax_tbody > tr:nth-child(2) > td.td2 > a
        Elements sts = doc.select("#ajax_tbody > tr:nth-child(n+1) > td.td2  > a");
        for (Element st :
                sts) {
            urls.add(st.attr("href"));
        }
        //http://www.zhuixinfan.com/main.php?mod=viewresource&sid=5310
//        System.out.println(urls);
        Set<String> magnets = new HashSet<>();
        for (String u :
                urls) {
            Document docm = null;
            try {
                docm = Jsoup.connect("http://www.zhuixinfan.com/" + u).timeout(30000).get();
                Element magnet = docm.getElementById("torrent_url");
                magnets.add(magnet.text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(magnets);
        return  magnets;
    }
    public static void main(String[] args) {
//        String baseurl = "http://www.zhuixinfan.com/";
//        String url = search("安堂机器人");//"main.php?mod=viewtvplay&pid=399&extra="
//        if (url != null) {
//            System.out.println(url);
//            Set<String> magnets = getMagnets(baseurl + url);
//            System.out.println(magnets.size());
//            System.out.println(magnets);
//        }

        System.out.println(System.currentTimeMillis());
    }
}

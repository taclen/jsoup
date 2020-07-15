package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫调用谷歌翻译
 */
public class GoogleTranslate {

    /**
     * 模拟谷歌翻译JS  keyword获取token
     * @param value
     * @return
     */
    private static String token(String value) {
        String result = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            Resource resource =new ClassPathResource("gg.js");
            FileReader reader = new FileReader(resource.getFile().getAbsolutePath());
            engine.eval(reader);

            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable)engine;
                result = String.valueOf(invoke.invokeFunction("token", value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 英语翻译成中文
     * @param key
     * @return
     */
    public static String eng2chn(String key){
        //https://translate.google.cn/translate_a/single?client=webapp&sl=en&tl=zh-CN&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=1&ssel=0&tsel=0&xid=1791807&kc=1&tk=710528.811055&q=hi
        try {

            String tk = token(key);

            Connection connect = Jsoup.connect("https://translate.google.cn/translate_a/single?client=webapp&sl=en&tl=zh-CN&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=1&ssel=0&tsel=0&xid=1791807&kc=1&tk=" + tk + "&q="+key);
            Map<String, String> header = new HashMap<String, String>();
//            header.put("Host", "translate.google.cn");
//            header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
//            header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            header.put("Accept-Language", "zh-cn,zh;q=0.5");
//            header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
//            header.put("Mimetype", "application/json");
//            header.put("Content-Type", "text/*, application/xml");
            header.put("authority", "translate.google.cn");
            header.put("method", "GET");
            header.put("path", "/translate_a/single?client=webapp&sl=en&tl=zh-CN&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=1&ssel=0&tsel=0&xid=1791807&kc=1&tk=" + tk + "&q="+key);
            header.put("scheme", "https");
            header.put("accept", "*/*");
            header.put("accept-encoding", "gzip, deflate, br");
            header.put("accept-language", "zh-CN,zh;q=0.9");
            header.put("referer", "https://translate.google.cn/");
            header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
            header.put("x-client-data", "CKC1yQEIl7bJAQiltskBCMG2yQEIqZ3KAQi3qsoBCMuuygEIvLDKAQj3tMoBCJe1ygEI7bXKAQiOusoB");
            header.put("scheme", "https");

            Connection data = connect.headers(header);

            Document doc = data.ignoreContentType(true).get();
//            System.out.println(doc.select("body").first().text());
            String result = doc.select("body").first().text();
            result = result.substring(0, result.indexOf("\",\""));
            result = result.substring(result.indexOf("\"") + 1);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 中文翻译成英语
     * @param key
     * @return
     */
    public static String chn2eng(String key){
        //https://translate.google.cn/translate_a/single?client=webapp&sl=zh-CN&tl=en&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=2&ssel=3&tsel=3&xid=1791807&kc=1&tk=327463.149640&q=%E6%88%91%E4%BB%AC
        try {

            String tk = token(key);

            key = URLEncoder.encode(key, "utf-8");
            Connection connect = Jsoup.connect("https://translate.google.cn/translate_a/single?client=webapp&sl=zh-CN&tl=en&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=2&ssel=3&tsel=3&xid=1791807&kc=1&tk=" + tk + "&q="+key);
            Map<String, String> header = new HashMap<String, String>();
//            header.put("Host", "translate.google.cn");
//            header.put("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
//            header.put("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            header.put("Accept-Language", "zh-cn,zh;q=0.5");
//            header.put("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7");
//            header.put("Mimetype", "application/json");
//            header.put("Content-Type", "text/*, application/xml");
            header.put("authority", "translate.google.cn");
            header.put("method", "GET");
            header.put("path", "/translate_a/single?client=webapp&sl=zh-CN&tl=en&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&otf=2&ssel=3&tsel=3&xid=1791807&kc=1&tk=" + tk + "&q="+key);
            header.put("scheme", "https");
            header.put("accept", "*/*");
            header.put("sec-fetch-mode", "cors");
            header.put("sec-fetch-site", "same-origin");
            header.put("accept-encoding", "gzip, deflate, br");
            header.put("accept-language", "zh-CN,zh;q=0.9");
            header.put("referer", "https://translate.google.cn/");
            header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
            header.put("x-client-data", "CKC1yQEIl7bJAQiltskBCMG2yQEIqZ3KAQi3qsoBCMuuygEIvLDKAQj3tMoBCJe1ygEI7bXKAQiOusoB");
            header.put("scheme", "https");

            Connection data = connect.headers(header);

            Document doc = data.ignoreContentType(true).get();
//            System.out.println(doc.select("body").first().text());
            String result = doc.select("body").first().text();
            result = result.substring(0, result.indexOf("\",\""));
            result = result.substring(result.indexOf("\"") + 1);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {

            String key = "- Go offline, launch setup from: Corel VideoStudio Ultimate 2020 " +
                    "" +
                    "(Important: Don't separate folders, run setup from above path. Othwerwise setup  will fail!)" +
                    "" +
                    "- Launch Patch.";

            System.out.println("原文：" + key + "\n结果：" + eng2chn(key));

            String key2 = "我爱你";

            System.out.println("原文：" + key2 + "\n结果：" + chn2eng(key2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

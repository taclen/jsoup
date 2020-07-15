package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import static com.fastech.jsoup.TestDingDIan.fileModify;

public class TestShaTan {

    /**
     * 下载小说至文件
     * 从第一章开始爬
     * 第一次下载时使用
     * @param path 小说存放路径
     * @param url 小说网址
     */
    public static void txtDownLoad(String path, String url){
        // http://m.shatanxs.com/35/35977/
        // http://m.shatanxs.com/35/35977/502112.html
        // /35/35977/
        // /35/35977/502127_2.html
        // /35/35977/502128.html
        // #pb_next
        String result = "网址：" + url + "\n";
        String file_name = path;//存放路径

        String start = "9";//顶点小说的第一章的标签index是9
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.connect(url).timeout(60000).get();//小说首页
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //#_52mb_h1 > a
            Element name = doc.select("#_52mb_h1 > a").first();//小说名
            System.out.println(name);

            file_name += name.text() + ".txt";//拼接文件名
            File file = new File(file_name);
            if (!file.exists()) file.createNewFile();


            Element next = doc.getElementById("pb_next");

            System.out.println(next.attr("href"));
            //#nr1
            Element content = doc.getElementById("nr1");//章节正文
            int i = 1;
            result += "第" + i + "章\n";
            result += content.text() + "\n";
            while (next.attr("href").contains(".html")){

                Connection conn = Jsoup.connect(next.attr("href"));//访问具体章节页面
                Document docs = conn.timeout(60000).get();

                next = docs.getElementById("pb_next");
                //#nr1
                Element content1 = docs.getElementById("nr1");//章节正文
                result += "第" + ++i + "章\n";
                result += content1.text().replaceAll("本章未完，请翻开下方下一章继续阅读", "")
                        .replaceAll("-335", "")+ "\n";
                System.out.println("======================第" + i + "章======================");
                if (i%10==0){//每100章写入文件并清空result字符串
                    fileModify(result, file_name, true);
                    result = "";
                }
            }




            if (!result.equals("")){//最后剩余章节写入文件
                fileModify(result, file_name, true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("====" + url);
        }finally {
            System.out.println("总耗时： " + (System.currentTimeMillis() - st)/1000 + " s");
        }
    }

    /**
     * 获取顶点排行榜上所有推荐位小说uri
     * @param url 网址
     * @return
     * @throws Exception
     */
    public static Set<String> getRankTxts(String url) throws Exception {
        Set<String> urls = new HashSet<>();
        Document doc = Jsoup.connect(url).timeout(30000).get();//首页
        //body > div.cover > p:nth-child(5)
        //body > div.cover > p:nth-child(20) > a  body > div.cover > p:nth-child(7)
        Elements sts = doc.select("body > div.cover > p:nth-child(n+1)");


        //body > div:nth-child(6) > a:nth-child(2)
        Element wy = doc.select("body > div:nth-child(6) > a:nth-child(2)").first();
        String count = wy.attr("href");
        count = count.substring(count.lastIndexOf("_")+1, count.lastIndexOf("/"));
        int num = Integer.parseInt(count);
        System.out.println(num);//7775
        for (int i = 0; i < 30; i++) {//20  30
            Document doc2 = Jsoup.connect("http://m.shatanxs.com/top/allvisit_" + i + "/").timeout(60000).get();
            Elements sts2 = doc2.select("body > div.cover > p:nth-child(n+1)");
            for (Element st :
                    sts2) {
//                    System.out.println(st.child(0).attr("href"));
                    //http://m.shatanxs.com/detail/8.html
                    Document doc3 = Jsoup.connect("http://m.shatanxs.com" + st.child(0).attr("href")).timeout(30000).get();
                    //body > div.cover > div.readlink > a.rl
                    Element el = doc3.select("body > div.cover > div.readlink > a").first();
//                    System.out.println(el);
                    urls.add(el.attr("href"));
            }
        }
        return urls;
    }
    public static Set<String> getSearchTxts(String url, String keyword) throws Exception {
        Set<String> urls = new HashSet<>();

        Document doc = Jsoup.parse(FileUtils.readFile2("E:\\download\\txt\\h\\shatan\\muzi.html"));//connect(url+"?type=articlename&submit=&s=" + URLEncoder.encode(keyword, "GBK")).timeout(30000).post();//首页
//        System.out.println(doc);
        //body > div.cover > p:nth-child(2) > a
        //body > div.cover > p:nth-child(337) > a
        Elements sts = doc.select("body > div.cover > p:nth-child(n+2) > a");
        System.out.println(sts.size());
        for (Element st :
                sts) {

//                System.out.println(st.child(0).attr("href"));
//                //http://m.shatanxs.com/detail/8.html
            Document doc2 = Jsoup.connect("http://m.shatanxs.com" + st.attr("href")).timeout(60000).get();
            //body > div.cover > div.readlink > a.rl
            Element el = doc2.select("body > div.cover > div.readlink > a").first();
            System.out.println(el);
            urls.add(el.attr("href"));
            Thread.sleep(100l);
        }


        return urls;
    }
    //http://www.shubao202.com/read/29621/2
    public static void main(String[] args) {
//        txtDownLoad("E:\\download\\txt\\h\\shatan\\", "http://m.shatanxs.com/115/115392/18528760.html");
        try {
//            Set<String> urls = getRankTxts("http://m.shatanxs.com/top/allvisit_1/");
            Set<String> urls = getSearchTxts("http://m.shatanxs.com/s.php","女儿");
            System.out.println(urls.size());
            System.out.println(urls);
            System.out.println("共有" + urls.size() + "本小说未下载");
            for (int i = 0; i < urls.size(); i++) {
                String uri = urls.toArray()[i].toString();
                System.out.println("开始下载第" + i + "本小说：" + uri);
                if (i > 0 && i%20 == 0) Thread.sleep(200000l);//启动20个线程后睡眠10分钟
                new Thread(() -> txtDownLoad("E:\\download\\txt\\h\\shatan\\", "http://m.shatanxs.com" + uri)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

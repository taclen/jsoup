package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static com.fastech.jsoup.TestDingDIan.fileModify;

public class Jiuailai {

    /**
     * 下载小说至文件
     * 从第一章开始爬
     * 第一次下载时使用
     * @param path 小说存放路径
     * @param url 小说网址
     */
    public static void txtDownLoad(String path, String url){
        String result = "网址：" + url + "\n";
        String file_name = path;//存放路径

        String start = "4";//顶点小说的第一章的标签index是9
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();//小说首页
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //#info > h1
            Element name = doc.select("#info > div > h1").first();//小说名
            System.out.println(name);
            //#info > p:nth-child(2)
            Element author = doc.select("#info > p:nth-child(2) > a").first();//作者
            System.out.println(author);

            up_time = doc.select("#info > p:nth-child(4)").first().text().split(" ")[0].replaceAll("最后更新：", "");//更新时间
            System.out.println(up_time);

            //#list > dl > dd:nth-child(9) > a  第一章的css标签 nth-child(n+9) n+9表示从9开始往后所有
            Elements zjs = doc.select("#list > dl > dd:nth-child(n+" + start + ") > a");
            System.out.println(zjs.size());//总章节数

            file_name += name.text() + "_" + author.text().replace("作 者：", "")
                    + "_" + up_time + "_"+ zjs.size() + ".txt";//拼接文件名 小说名_作者_最新章节
            File file = new File(file_name);
            if (!file.exists()) file.createNewFile();

            int i = 1;
            for (Element element: zjs) {
                result += element.text() + "\n";//章节名
                String uri = element.attr("href").substring(element.attr("href").lastIndexOf("/") + 1);
                System.out.println(uri);//章节uri


                Connection conn = Jsoup.connect(url + uri);//访问具体章节页面
                Document docs = conn.timeout(30000).get();
                Element content = docs.getElementById("content");//章节正文
                result += content.text()
                        .replaceAll("一秒记住:15小说网 m.15xsw.com", "") + "\n";

                //#wrapper > div.content_read > div > div.bookname > h1
                String bookname = docs.select("#wrapper > div.content_read > div > div.bookname > h1").first().text();
                int ys = 0;
                if (bookname.contains("(") && bookname.contains("/") && bookname.contains(")")){//第 14 部分阅读(1/9)
                    bookname = bookname.substring(bookname.lastIndexOf("/") + 1);
                    bookname = bookname.substring(0, bookname.indexOf(")"));
                    ys = Integer.parseInt(bookname);
                }

                if (ys > 0){
                    for (int j = 1; j < ys + 1; j++) {
                        String urii = uri.substring(0,uri.lastIndexOf(".")) + "_" + j + uri.substring(uri.lastIndexOf("."));
                        System.out.println(urii);
                        Connection conn2 = Jsoup.connect(url + urii);//访问下一页
                        Document doc2 = conn2.timeout(30000).get();
                        Element content2 = doc2.getElementById("content");//章节正文
                        result += content2.text()
                                .replaceAll("一秒记住:15小说网 m.15xsw.com", "") + "\n";
                    }
                }

                System.out.println(result.endsWith("本章未完，点击下一页继续阅读\n"));
                result = result.replaceAll("-->> 本章未完，点击下一页继续阅读","").replaceAll("> 本章未完，点击下一页继续阅读\n" +
                        "> ","").replaceAll("&lt;/div&gt; www.7biquge.com 7笔趣阁\\[记住我们:\\]\n" +
                        "]","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
//                .replaceAll("","")
                ;
                System.out.println("======================第" + i++ + "/" +zjs.size() + "章======================");
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
        }finally {
            System.out.println("总耗时： " + (System.currentTimeMillis() - st)/1000 + " s");
        }
    }

    /**
     * 获取首页所有推荐位小说uri
     * @param url
     * @return
     * @throws Exception
     */
    public static Set<String> getHomeTxts(String url) throws Exception {
        Set<String> urls = new HashSet<>();
        Document doc = Jsoup.connect(url).timeout(30000).get();//首页
        //#hotcontent > div.l > div:nth-child(1) > dl > dt > a
        //#hotcontent > div.l > div:nth-child(5) > dl > dt > a
        Elements sts = doc.select("#hotcontent > div.l > div:nth-child(n+1) > dl > dt > a");//首推小说
        for (Element st :
                sts) {
            urls.add(st.attr("href"));
        }
        System.out.println(sts.size());
        //#hotcontent > div.r > ul > li:nth-child(1) > span.s2l > a
        //#hotcontent > div.r > ul > li:nth-child(14) > span.s2l > a
        Elements qts = doc.select("#hotcontent > div.r > ul > li:nth-child(n+1) > span.s2l > a");//强推小说
        for (Element qt :
                qts) {
            urls.add(qt.attr("href"));
        }
        System.out.println(qts.size());
        //#novelslist1 > div:nth-child(2) > ul > li:nth-child(1) > a:nth-child(1)

        Elements cts = doc.select("#novelslist1 > div:nth-child(n+1) > ul > li:nth-child(n+1) > a:nth-child(1)");//次推小说
        for (Element ct :
                cts) {
            urls.add(ct.attr("href"));
        }
        System.out.println(cts.size());

        //#novelslist1 > div.content.border > ul > li:nth-child(10) > a:nth-child(2)
        Elements rts = doc.select("#novelslist1 > div.content.border > ul > li:nth-child(n+1) > a:nth-child(1)");//弱推小说
        for (Element rt :
                rts) {
            urls.add(rt.attr("href"));
        }
        System.out.println(rts.size());

        //#novelslist2 > div.content.border > ul > li:nth-child(2) > a:nth-child(1)
        Elements cts2 = doc.select("#novelslist2 > div:nth-child(n+1) > ul > li:nth-child(n+1) > a:nth-child(1)");//次推小说2
        for (Element ct :
                cts2) {
            urls.add(ct.attr("href"));
        }
        System.out.println(cts2.size());

        //#novelslist2 > div.content.border > ul > li:nth-child(10) > a:nth-child(2)
        Elements rts2 = doc.select("#novelslist2 > div.content.border > ul > li:nth-child(n+1) > a:nth-child(1)");//弱推小说2
        for (Element rt :
                rts2) {
            urls.add(rt.attr("href"));
        }
        System.out.println(rts2.size());

        return urls;
    }
    public static void main(String[] args) {
        try {
            Set<String> urls = getHomeTxts("http://www.shuquw.com/");
            for (int i = 0; i < urls.size(); i++) {
                String uri = urls.toArray()[i].toString();
                System.out.println("开始下载第" + i + "本小说：" + uri);
                if (i > 0 && i%20 == 0) Thread.sleep(300000l);//启动20个线程后睡眠5分钟
                new Thread(() -> txtDownLoad("E:\\download\\txt\\h\\jiuailai\\", "http://www.shuquw.com" + uri)).start();
            }
//        txtDownLoad("E:\\download\\txt\\h\\","http://www.shuquw.com/208_208643/");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

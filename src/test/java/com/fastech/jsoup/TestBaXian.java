package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

import static com.fastech.jsoup.FileUtils.fileModify;

public class TestBaXian {
    /**
     * 下载小说至文件
     * 从第一章开始爬
     * 第一次下载时使用
     * @param path 小说存放路径
     * @param url 小说网址
     */
    public static void txtDownLoad(String path, String url){
        String result = "网址：" + url + "\n";//http://www.baxianxs.com/xiaoshuo.asp?id=14096
        String file_name = path;//存放路径


        String start = "2";//八仙小说的第一章的标签index是2
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
//            Document doc = Jsoup.connect(url).timeout(60000).get();//小说首页
            Document doc = Jsoup.parse(new URL(url).openStream(), "GBK", url);
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //body > table.m9 > tbody > tr:nth-child(2) > td > strong
            Element name = doc.select("body > table.m9 > tbody > tr:nth-child(2) > td > strong").first();//小说名
            System.out.println(name);
            //body > table.m9 > tbody > tr:nth-child(3) > td:nth-child(2) > a
            Element author = doc.select("body > table.m9 > tbody > tr:nth-child(3) > td:nth-child(2) > a").first();//作者
            System.out.println(author);

            //body > table.m9 > tbody > tr:nth-child(3) > td:nth-child(6)
            up_time = doc.select("body > table.m9 > tbody > tr:nth-child(3) > td:nth-child(6)").first().text().replaceAll("更新: ", "").replaceAll("/","-").trim();//更新时间
            System.out.println(up_time);


            Element element = doc.selectFirst("body > table > tbody > tr > td > div.bai > a");
//            System.out.println(element);
            file_name += name.text() + "_" + author.text()
                    + "_" + up_time + ".txt";//拼接文件名 小说名_作者_最新章节
            File file = new File(file_name);
            if(FileUtils.fileExists(path, name.text())){
                System.out.println("跳过已存在小说：" + file_name);
                file.delete();
//                return;
            }
            if (!file.exists())file.createNewFile();
            int i = 0;
            String uri = element.attr("href");
            System.out.println("第" + ++i + "章 " + uri);//章节uri   http://www.baxianxs.com/page.asp?id=1509124
            result += "第" + i + "章\n";
//            Connection conn = Jsoup.connect(uri);//访问具体章节页面
            Document docs = Jsoup.parse(new URL(uri).openStream(), "GBK", uri);
            Element content = docs.select("body > table.mview > tbody > tr:nth-child(3) > td").first();//章节正文 body > table.mview > tbody > tr:nth-child(3) > td
            result += content.text() + "\n";


            //body > table.viewxia > tbody > tr:nth-child(1) > td > a:nth-child(2)
            Element next = docs.selectFirst("body > table.viewxia > tbody > tr:nth-child(1) > td > a:nth-child(2)");
            while (null != next &&  null != next.attr("href")){
                uri = next.attr("href");
                System.out.println("第" + ++i + "章 " + uri);//章节uri   http://www.baxianxs.com/page.asp?id=1509124
//                Connection connn = Jsoup.connect(uri);//访问具体章节页面
                Document docn = Jsoup.parse(new URL(uri).openStream(), "GBK", uri);
                content = docn.select("body > table.mview > tbody > tr:nth-child(3) > td").first();//章节正文 body > table.mview > tbody > tr:nth-child(3) > td
                next = docn.selectFirst("body > table.viewxia > tbody > tr:nth-child(1) > td > a:nth-child(2)");
                result += "第" + i + "章\n";
                result += content.text() + "\n";
                result = result.replaceAll("wWＷ.bAxＩanXｓ.cｏM", "")
                ;
                if (!result.equals("")){//最后剩余章节写入文件
                    fileModify(result, file_name, true);
                    result = "";
                }
                Thread.sleep(500l);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("=======" + url);
        }finally {
            System.out.println("总耗时： " + (System.currentTimeMillis() - st)/1000 + " s");
        }
    }
    public static void main(String[] args) {
        //http://www.baxianxs.com/
        //http://www.baxianxs.com/xiaoshuo.asp?id=14096
        //http://www.baxianxs.com/hit.asp?id=24
        txtDownLoad("E:\\download\\txt\\h\\baxian\\","http://www.baxianxs.com/xiaoshuo.asp?id=14101");
    }
}

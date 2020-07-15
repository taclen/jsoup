package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

import static com.fastech.jsoup.FileUtils.fileModify;

public class TestGGShuji {
    /**
     * 下载小说至文件
     * 从第一章开始爬
     * 第一次下载时使用
     * @param path 小说存放路径
     * @param url 小说网址
     */
    public static void txtDownLoad(String path, String url){
        String result = "网址：" + url + "\n";
        //http://www.ggshuji.net/book/42454.html
        //http://www.ggshuji.net/read/42454_1.html
        //#con_one_1 > div > ul > li:nth-child(1) > a
        //#con_one_1 > div > ul > li:nth-child(5) > a
        //#con_one_1 > div > ul > li:nth-child(23) > a
        //#con_one_1 > div > ul > li:nth-child(24) > a
        String file_name = path;//存放路径

        String start = "9";//顶点小说的第一章的标签index是9
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.parse(new URL(url).openStream(), "GBK", url);;//小说首页
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(1) > td:nth-child(2) > h1
            Element name = doc.select("body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(1) > td:nth-child(2) > h1").first();//小说名
            System.out.println(name);
            //body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(3)
            Element author = doc.select("body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(3)").first();//作者
            System.out.println(author);
            //body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(3) > td > table > tbody > tr > td:nth-child(2)
            up_time = doc.select("body > div:nth-child(2) > div.left > table > tbody > tr:nth-child(3) > td > table > tbody > tr > td:nth-child(2)").first().text().replaceAll("更新时间：", "");//更新时间
            System.out.println(up_time);

            //#con_one_1 > div > ul > li:nth-child(1) > a
            Elements zjs = doc.select("#con_one_1 > div > ul > li:nth-child(n+1) > a");
            System.out.println(zjs.size());//总章节数
            zjs.remove(zjs.size()-1);

            file_name += name.text().replaceAll("　","") + "_" + author.text().replace("作者：", "")
                    + "_" + up_time + "_"+ zjs.size() + ".txt";//拼接文件名 小说名_作者_最新章节
            File file = new File(file_name);
            if(FileUtils.fileExists(path, name.text())){
                System.out.println("跳过已存在小说：" + file_name);
                file.delete();
//                return;
            }
            if (!file.exists()) file.createNewFile();

            int i = 1;
            for (Element element: zjs) {
                result += element.text() + "\n";//章节名
                String uri = element.attr("href");
                System.out.println(uri);//章节uri
//                Connection conn = Jsoup.connect(url + element.attr("href"));//访问具体章节页面
                Document docs = Jsoup.parse(new URL(uri).openStream(), "GBK", uri);;
                Element content = docs.select("body > div.read_c > div.chapter").first();//章节正文 body > div.read_c > div.chapter
                result += content.text()
                        .replaceAll("（www。。）txt电子书下载", "")
                        .replaceAll("（WWW。。）免费电子书下载", "")
                        .replaceAll("（www。。）好看的txt电子书", "")
                        .replaceAll("按键盘上方向键 ← 或 → 可快速上下翻页，按键盘上的 Enter 键可回到本书目录页，按键盘上方向键 ↑ 可回到本页顶部！ ————未阅读完？加入书签已便下次继续阅读！","")
                        + "\n";
                System.out.println("======================第" + i++ + "/" +zjs.size() + "章======================");
                if (i%5==0){//每100章写入文件并清空result字符串
                    fileModify(result, file_name, true);
                    result = "";
                }
                Thread.sleep(500l);
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
    public static void main(String[] args) {
        //http://ggshuji.net/
        txtDownLoad("E:\\download\\txt\\h\\gougoushuji\\","http://www.ggshuji.net/book/41730.html");
    }
}

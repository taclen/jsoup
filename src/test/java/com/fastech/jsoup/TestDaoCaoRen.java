package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import static com.fastech.jsoup.FileUtils.fileModify;

public class TestDaoCaoRen {
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

        //https://www.daocaorenshuwu.com/book/wodemeimeishiouxiang/
        String start = "1";//顶点小说的第一章的标签index是9
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.connect(url).timeout(60000).get();//小说首页
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //#info > h1
            Element name = doc.select("h1.book-name > a").first();//小说名
            System.out.println(name);
            //body > div:nth-child(6) > div.col-big.fl > div.book-info > div > div > div.media > div.media-body > div.row
            Element author = doc.select("div.row > div:contains(作者)").first();//作者
            System.out.println(author);
//
            up_time = doc.select("div.row > div.col-md-4.col-sm-6.dark.hidden-sm.hidden-xs").first().text().split(" ")[0].replaceAll("最后更新：", "");//更新时间
            System.out.println(up_time);

            //#all-chapter > div > div.panel-body > div > div:nth-child(1) > a
            //#all-chapter > div > div.panel-body > div > div:nth-child(4) > a
            Elements zjs = doc.select("#all-chapter > div > div.panel-body > div > div:nth-child(n+" + start + ") > a");
            System.out.println(zjs.size());//总章节数

            file_name += name.text() + "_" + author.text().replace("作者：", "")
                    + "_" + up_time + "_"+ zjs.size() + ".txt";//拼接文件名 小说名_作者_最新章节
            File file = new File(file_name);
            if (!file.exists()) file.createNewFile();

//            int i = 1;
            for (int k = 1479; k < zjs.size(); k++) {
                Element element = zjs.get(k);
                result += element.text() + "\n";//章节名
                String uri = element.attr("href");
                uri = url.substring(0,url.indexOf(":") + 1) + uri;
                System.out.println(uri);//章节uri
                Connection conn = Jsoup.connect(uri);//访问具体章节页面
                Document docs = conn.timeout(60000).get();
                //#cont-text > p:nth-child(4)
                //#cont-text > p:nth-child(5)
                //#cont-text > p:nth-child(7)
                //#cont-text
                Element content = docs.getElementById("cont-text");//章节正文
                Elements ps = content.select("p:not(*[class])");
                System.out.println(ps.first());
                System.out.println(ps.first().ownText());
                result += content.text()
                        .replaceAll("稻草人书屋", "") + "\n";
                //#content > div.row.mt10 > div.col-md-6.text-center > a:nth-child(3) > button
                Element next = docs.select("#content > div.row.mt10 > div.col-md-6.text-center > a:nth-child(3) > button").first();
                if (next == null)next = docs.select("button:contains(下一页)").first();
//                System.out.println(next.text());
                int j = 2;
                while (null != next && next.text().contains("下一页")){
                    Connection connn = Jsoup.connect(uri.substring(0,uri.lastIndexOf(".")) + "_" + j + uri.substring(uri.lastIndexOf(".")));//访问具体章节页面
                    Document docn = connn.timeout(60000).get();
                    Element contentn = docn.getElementById("cont-text");//章节正文
                    //#content > div.row.mt10 > div.col-md-6.text-center > a:nth-child(3) > button
                    result += contentn.text() + "\n";
                    System.out.println("页数：" + j);
//                    next = docn.getElementById("#content > button:contains(下一页)");
                    //#content > div:nth-child(20) > div.col-md-6.text-center > a:nth-child(3) > button
                    next = docn.select("#content > div.row.mt10 > div.col-md-6.text-center > a:nth-child(3) > button").first();
                    if (next == null)next = docs.select("button:contains(下一页)n").first();
                    j++;
                }
                result = result.replaceAll("内容来自Ｄaocaorenshuwu.com","")
                .replaceAll("www.daocaorenshuwu.com","")
                .replaceAll("内容来自daoＣaorenshuwu.com","")
                .replaceAll("内容来自daoＣaorenshuwu.ＣＯＭ","")
                .replaceAll("内容来自daocaoＲenshuwu.com","")
                .replaceAll("本文来自稻草人书屋","")
                .replaceAll("稻草人书屋免费下载TXT电子书","")
                .replaceAll("内容来自daocaorenshuwu.ＣＯm","")
                .replaceAll("内容来自ＤaoＣaoＲenshuwu.ＣＯＭ","")
                .replaceAll("copyright 稻草人书屋","")
                .replaceAll("免费下载TXT电子书","")
                .replaceAll("欢迎到看书","")
                .replaceAll("内容来自daocaoＲenshuwu.coＭ","")
                .replaceAll("copyright","")
                .replaceAll("内容来自daocaoＲenshuwu.Ｃom","")
                .replaceAll("daocaorenshuwu.com","")
                .replaceAll("本文来自 ","")
                .replaceAll("稻草人书屋","")
                ;
                System.out.println("======================第" + k + "/" +zjs.size() + "章======================");
                if (k%20==0){//每100章写入文件并清空result字符串
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
    public static void main(String[] args) {
        // https://www.daocaorenshuwu.com/book/wodemeimeishiouxiang/
        txtDownLoad("E:\\download\\txt\\daocaoren\\","https://www.daocaorenshuwu.com/book/wodemeimeishiouxiang/");

    }
}

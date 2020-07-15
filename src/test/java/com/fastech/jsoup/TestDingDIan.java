package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 顶点小说爬虫 https://www.booktxt.net/
 *  java.io.IOException: Mark invalid
 *  使用Jsoup 1.12.1 时多次 Jsoup.connect().get()的时候偶尔会出现此报错，换 1.13.3 后无错
 */
public class TestDingDIan {

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

        String start = "9";//顶点小说的第一章的标签index是9
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();//小说首页
            url = doc.baseUri();
            result = "网址：" + url + "\n";
            //#info > h1
            Element name = doc.select("#info > h1").first();//小说名
            System.out.println(name);
            //#info > p:nth-child(2)
            Element author = doc.select("#info > p:nth-child(2)").first();//作者
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
                System.out.println(element.attr("href"));//章节uri
                Connection conn = Jsoup.connect(url + element.attr("href"));//访问具体章节页面
                Document docs = conn.timeout(30000).get();
                Element content = docs.getElementById("content");//章节正文
                result += content.text()
                        .replaceAll("请记住本书首发域名：booktxt.net。顶点小说手机版阅读网址：m.booktxt.net", "") + "\n";
                System.out.println("======================第" + i++ + "/" +zjs.size() + "章======================");
                if (i%100==0){//每100章写入文件并清空result字符串
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
     * 爬小说至文件
     * 追更小说时使用
     * @param path 小说存放路径
     * @param url 小说网址
     * @param from 从第几章开始(包含) 顶点网实际章节数，非小说目录显示的章节数
     */
    public static void txtDownLoad(String path, String url, int from){
        String result = "";
        String file_name = path;//存放路径

        String start = from + 9 + "";//顶点小说的第一章的标签index是9
        int new_zj = from;
        long st = System.currentTimeMillis();
        String up_time = "2020-02-02";
        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();//小说首页
            //#info > h1
            Element name = doc.select("#info > h1").first();//小说名
            System.out.println(name);
            //#info > p:nth-child(2)
            Element author = doc.select("#info > p:nth-child(2)").first();//作者
            System.out.println(author);

            up_time = doc.select("#info > p:nth-child(4)").first().text().split(" ")[0].replaceAll("最后更新：", "");//更新时间
            System.out.println(up_time);

            //#list > dl > dd:nth-child(9) > a  第一章的css标签 nth-child(n+9) n+9表示从9开始往后所有
            Elements zjs = doc.select("#list > dl > dd:nth-child(n+" + start + ") > a");
            System.out.println(zjs.size());//总章节数
            new_zj = new_zj + zjs.size();
            System.out.println(file_name + " 新更新章节数为：" + zjs.size());
            File file = new File(file_name);
            if (!file.exists()) file.createNewFile();

            int i = 1;
            for (Element element: zjs) {
                result += element.text() + "\n";//章节名
                System.out.println(element.attr("href"));//章节uri
                Connection conn = Jsoup.connect(url + element.attr("href"));//访问具体章节页面
                Document docs = conn.timeout(30000).get();
                Element content = docs.getElementById("content");//章节正文
                result += content.text()
                        .replaceAll("请记住本书首发域名：booktxt.net。顶点小说手机版阅读网址：m.booktxt.net", "") + "\n";
                System.out.println("======================第" + i++ + "/" +zjs.size() + "章======================");
                if (i%100==0){//每100章写入文件并清空result字符串
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
            File file = new File(file_name);
            String newfile_name = file_name;
            if (file_name.contains("-")){//E:\download\txt\tmp\我的妹妹是偶像_赵青杉_2017-11-07_1498.txt
                newfile_name = file_name.substring(0,file_name.lastIndexOf("_"));//E:\download\txt\tmp\我的妹妹是偶像_赵青杉_2017-11-07
                newfile_name = newfile_name.substring(0,newfile_name.lastIndexOf("_") + 1);//E:\download\txt\tmp\我的妹妹是偶像_赵青杉_
            }else{//E:\download\txt\tmp\我的妹妹是偶像_赵青杉_1498.txt
                newfile_name = file_name.substring(0,file_name.lastIndexOf("_") + 1);//E:\download\txt\tmp\我的妹妹是偶像_赵青杉_
            }
            newfile_name = newfile_name + up_time + "_" + new_zj + ".txt";
            if (file.exists()) file.renameTo(new File(newfile_name));
        }
    }
    /**
     * 写入文件
     * @param content 内容
     * @param path 路径
     * @param isAppend true：追加；false：覆盖
     * @throws Exception
     */
    public static void fileModify(String content, String path, boolean isAppend) throws Exception {
        FileWriter fw = new FileWriter(path,isAppend);
        fw.write(content, 0, content.length());
        fw.flush();
        fw.close();
    }

    /**
     * 检测是否有更新
     * @param path
     * @return
     * @throws Exception
     */
    public static void updateTxts(String path) throws Exception {

        String url = "";
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //getFileList(files[i].getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.endsWith("txt") && fileName.contains("_")) { // 以***结尾的文件
                    String strFileName = files[i].getAbsolutePath();
                    int now = Integer.parseInt(strFileName.substring(strFileName.lastIndexOf("_") +1, strFileName.lastIndexOf(".")));
                    File file = new File(strFileName);
                    if (file.isFile()){
                        BufferedReader br = new BufferedReader(new FileReader(strFileName));
                        String line = "";
                        while((line = br.readLine()) != null)
                        {
                            break;
                        }

                        br.close();
                        url = line.substring(line.indexOf("https"));
                        if (!url.contains("https://www.booktxt.net/")) continue;
                        System.out.println("开始检查小说：" + strFileName + " 更新情况...");
                        txtDownLoad(strFileName,url,now);
                    }
                } else {
                    continue;
                }
            }
        }

    }

    /**
     * 删除异常txt
     * 删除下载异常（章节与大小不匹配）的小说  假设1000章的小说>5M为正常
     * @param path
     * @throws Exception
     */
    public static void delErrTxts(String path) throws Exception {
        String url = "";
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //getFileList(files[i].getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.endsWith("txt") && fileName.contains("_")) { // 以***结尾的文件
                    String strFileName = files[i].getAbsolutePath();
                    int now = Integer.parseInt(strFileName.substring(strFileName.lastIndexOf("_") +1, strFileName.lastIndexOf(".")));
                    File file = new File(strFileName);
                    if (file.isFile()){
                        long size = file.length()/1024/1024;
//                        System.out.println(strFileName + " : " + size);
                        if ((now/100)/size > 2){
                            System.out.println(fileName + " : " + size);
                            file.delete();
                        }
                    }
                } else {
                    continue;
                }
            }
        }
    }
    /**
     * 获取顶点首页所有推荐位小说uri
     * @param url
     * @return
     * @throws Exception
     */
    public static Set<String> getHomeTxts(String url) throws Exception {
        Set<String> urls = new HashSet<>();
        Document doc = Jsoup.connect(url).timeout(30000).get();//首页
        //#hotcontent > div.l > div:nth-child(1) > dl > dt > a
        //#hotcontent > div.l > div:nth-child(4) > dl > dt > a
        Elements sts = doc.select("#hotcontent > div.l > div:nth-child(n+1) > dl > dt > a");//首推小说
        for (Element st :
                sts) {
            urls.add(st.attr("href"));
        }
        //#hotcontent > div.r > ul > li:nth-child(1) > span.s2 > a
        //#hotcontent > div.r > ul > li:nth-child(8) > span.s2 > a
        Elements qts = doc.select("#hotcontent > div.r > ul > li:nth-child(n+1) > span.s2 > a");//强推小说
        for (Element qt :
                qts) {
            urls.add(qt.attr("href"));
        }
        //#main > div:nth-child(2) > div:nth-child(1) > div > dl > dt > a
        //#main > div:nth-child(2) > div:nth-child(3) > div > dl > dt > a
        //#main > div:nth-child(3) > div:nth-child(3) > div > dl > dt > a
        Elements cts = doc.select("#main > div:nth-child(n+2) > div:nth-child(n+1) > div > dl > dt > a");//次推小说
        for (Element ct :
                cts) {
            urls.add(ct.attr("href"));
        }
        //#main > div:nth-child(2) > div:nth-child(1) > ul > li:nth-child(1) > a
        Elements rts = doc.select("#main > div:nth-child(n+2) > div:nth-child(n+1) > ul > li:nth-child(n+1) > a");//弱推小说
        for (Element rt :
                rts) {
            urls.add(rt.attr("href"));
        }

        return urls;
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
        //body > body" > div.wrap.rank > div:nth-child(n+1) > ul > li:nth-child(n+1) > a
        Elements sts = doc.select("* > div.wrap.rank > div:nth-child(n+1) > ul > li:nth-child(n+1) > a");
        for (Element st :
                sts) {
            urls.add(st.attr("href"));
        }

        return urls;
    }
    /**
     * 移除掉已下载过的小说链接
     * 读取小说目录下所有小说的链接，与传入的链接集比较
     * @param urls 链接集
     * @param path 小说目录
     * @throws Exception
     */
    public static void disList(Set<String> urls, String path) throws Exception {
        List<File> filelist = new ArrayList<>();
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //getFileList(files[i].getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.endsWith("txt") && fileName.contains("_")) { // 以***结尾的文件
                    String strFileName = files[i].getAbsolutePath();
                    File file = new File(strFileName);
                    if (file.isFile()){
                        BufferedReader br = new BufferedReader(new FileReader(strFileName));
                        String line = "";
                        while((line = br.readLine()) != null)
                        {
                            break;
                        }

                        br.close();
                        Iterator<String> it = urls.iterator();
                        for(int j=0; j<urls.size(); j++){
                            String name = it.next();
                            if(line.contains(name)){
//                                System.out.println(line + " : " + name);
                                it.remove();
                                j--;
                            }
                        }
                    }
                    filelist.add(files[i]);
                } else {
                    filelist.add(files[i]);
                    continue;
                }
            }
        }

    }

    /**
     * 查找小说
     * 只返回查到的第一条
     * @param url 搜索API网址
     * @param key 搜索关键字
     * @return
     * @throws Exception
     */
    public static String search(String url, String key) throws Exception {
        Document doc = Jsoup.connect(url + URLEncoder.encode(key, "GBK")).timeout(30000).get();
//        System.out.println(doc);
        //#search-main > div.search-list > ul > li:nth-child(2) > span.s2 > a
        Elements els = doc.select("#search-main > div.search-list > ul > li:nth-child(2) > span.s2 > a");
        if (null != els && els.size() > 0){
            return els.first().attr("href");
        }
        return null;
    }
    public static void main(String[] args) {

        //下载排行榜所有推荐小说
//        try {
//            String file_name = "E:\\download\\txt\\dingdian\\";//存放路径
//            String file_url = "https://www.booktxt.net";//首页
//            Set<String> urls = getRankTxts(file_url + "/paihangbang/");//获取排行榜上所有小说
//            disList(urls,file_name);//去除本地已下载的小说
//            System.out.println("共有" + urls.size() + "本小说未下载");
//            for (int i = 0; i < urls.size(); i++) {
//                String uri = urls.toArray()[i].toString();
//                System.out.println("开始下载第" + i + "本小说：" + uri);
//                if (i > 0 && i%20 == 0) Thread.sleep(600000l);//启动20个线程后睡眠10分钟
//                new Thread(() -> txtDownLoad(file_name, file_url + uri)).start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        //下载首页所有推荐小说
//        try {
//            String file_name = "E:\\download\\txt\\dingdian\\";//存放路径
//            String file_url = "https://www.booktxt.net";//首页
//            Set<String> urls = getHomeTxts(file_url);//获取网页上所有小说
//            disList(urls,file_name);//去除本地已下载的小说
//            System.out.println("共有" + urls.size() + "本小说未下载");
//            for (int i = 0; i < urls.size(); i++) {
//                String uri = urls.toArray()[i].toString();
//                System.out.println("开始下载第" + i + "本小说：" + uri);
//                if (i > 0 && i%20 == 0) Thread.sleep(600000l);//启动20个线程后睡眠10分钟
//                new Thread(() -> txtDownLoad(file_name, file_url + uri)).start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }




          //更新所有小说
          String path= "X:\\OneDrive\\Documents\\dingdian\\";
          try {
              updateTxts(path);
          } catch (Exception e) {
              e.printStackTrace();
          }

        //删除下载异常（章节与大小不匹配）的小说  假设1000章的小说>5M为正常
//        String path= "E:\\download\\txt\\dingdian";
//        try {
//            delErrTxts(path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //查找小说返回网址
//        try {
//            System.out.println(search("https://so.biqusoso.com/s1.php?ie=gbk&siteid=booktxt.net&s=2758772450457967865&q=","浑沌记"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

                //下载单本小说
//        String file_name = "E:\\download\\txt\\dingdian\\";//存放路径
//        String file_url = "https://www.booktxt.net/2_2799/";//小说地址
//        //查找小说返回网址
//        try {
//            file_url = search("https://so.biqusoso.com/s1.php?ie=gbk&siteid=booktxt.net&s=2758772450457967865&q=","诸天万界神龙系统");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (null != args && args.length > 0){
//            if (args.length == 1)file_url = args[0];
//            if (args.length == 2){
//                file_name = args[0];
//                file_url = args[1];
//            }
//        }
//        txtDownLoad(file_name, file_url);
    }
}

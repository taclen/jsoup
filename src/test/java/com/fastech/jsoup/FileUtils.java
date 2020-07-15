package com.fastech.jsoup;

import java.io.*;

public class FileUtils {

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

    public static String readFile2(String path) throws Exception {
        File file = new File(path);
        if (file.isFile()) {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            return sb.toString();
        }
        return null;
    }
    public static String readFile(String path) throws Exception {
        File file = new File(path);
        if (file.isFile()) {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(replace(line) + "\n");
            }

            br.close();
            return sb.toString();
        }
        return null;
    }
    public static String replace(String str){
        return str = str.replaceAll("[阿甘手机站:m.agxsw.net]", "")
                .replaceAll("（）免费电子书下载","")
                .replaceAll("（）x电子书下载 ","")
                .replaceAll("（）免费x小说下载","")
                .replaceAll("（）好看的x电子书","")
                .replaceAll("快捷c作: 按键盘上方向键 ← 或 → 可快速上下翻页 按键盘上的 enter 键可回到本书目录页 按键盘上方向键 ↑ 可回到本页顶部! 如果本书没有阅读完，想下次继续接着阅读，可使用上方 ”收藏到我的浏览器” 功能 和 ”加入书签” 功能！","")
                .replaceAll("快捷c作: 按键盘上方向键 ← 或 → 可快速上下翻页 按键盘上的 ener 键可回到本书目录页 按键盘上方向键 ↑ 可回到本页顶部! 如果本书没有阅读完，想下次继续接着阅读，可使用上方 ≈ap;quo;收藏到我的浏览器≈ap;quo; 功能 和 ≈ap;quo;加入书签≈ap;quo; 功能！","")
                .replaceAll("快捷c作 按键盘上方向键 ← 或 → 可快速上下翻页 按键盘上的 r 键可回到本书目录页 按键盘上方向键 ↑ 可回到本页顶部! 如果本书没有阅读完，想下次继续接着阅读，可使用上方 ”收藏到我的浏览器” 功能 和 ”加入书签” 功能！","")
//                .replaceAll("","")
                ;
    }

    public static boolean fileExists(String path, String name){
        File dir = new File(path);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //getFileList(files[i].getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.contains(name)) { // 以***结尾的文件
                    return true;
                }
            }
        }
        return false;
    }
    public static void main(String[] args) {
        File dir = new File("E:\\download\\txt\\h\\");
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    //getFileList(files[i].getAbsolutePath()); //遍历子文件夹里面的东西
                } else if (fileName.endsWith("txt")) { // 以***结尾的文件
                    String strFileName = files[i].getAbsolutePath();
                    try {
                        fileModify(readFile(strFileName), strFileName, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

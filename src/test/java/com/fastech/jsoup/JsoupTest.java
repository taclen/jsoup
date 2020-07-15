package com.fastech.jsoup;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Jsoup爬虫特性测试
 */
public class JsoupTest {
//
//    jsoup 的过人之处——选择器
//    前面我们已经简单的介绍了 jsoup 是如何使用选择器来对元素进行检索的。本节我们把重点放在选择器本身强大的语法上。下表是 jsoup 选择器的所有语法详细列表。
//
//    表 2. 基本用法：
//            tagname	使用标签名来定位，例如 a
//            ns|tag	使用命名空间的标签定位，例如 fb:name 来查找 <fb:name> 元素
//            #id	使用元素 id 定位，例如 #logo
//                        .class	使用元素的 class 属性定位，例如 .head
//            [attribute]	使用元素的属性进行定位，例如 [href] 表示检索具有 href 属性的所有元素
//            [^attr]	使用元素的属性名前缀进行定位，例如 [^data-] 用来查找 HTML5 的 dataset 属性
//            [attr=value]	使用属性值进行定位，例如 [width=500] 定位所有 width 属性值为 500 的元素
//            [attr^=value], [attr$=value], [attr*=value]	这三个语法分别代表，属性以 value 开头、结尾以及包含
//            [attr~=regex]	使用正则表达式进行属性值的过滤，例如 img[src~=(?i)\.(png|jpe?g)]
//                        *	定位所有元素
//    以上是最基本的选择器语法，这些语法也可以组合起来使用，下面是 jsoup 支持的组合用法：
//
//    表 3：组合用法：
//            el#id	定位 id 值某个元素，例如 a#logo -> <a id=logo href= … >
//            el.class	定位 class 为指定值的元素，例如 div.head -> <div class=head>xxxx</div>
//            el[attr]	定位所有定义了某属性的元素，例如 a[href]
//            以上三个任意组合	例如 a[href]#logo 、a[name].outerlink
//            ancestor child	这五种都是元素之间组合关系的选择器语法，其中包括父子关系、合并关系和层次关系。
//            parent > child
//            siblingA + siblingB
//            siblingA ~ siblingX
//            el, el, el
//    除了一些基本的语法以及这些语法进行组合外，jsoup 还支持使用表达式进行元素过滤选择。下面是 jsoup 支持的所有表达式一览表：
//
//    表 4. 表达式：
//            :lt(n)	例如 td:lt(3) 表示 小于三列
//            :gt(n)	div p:gt(2) 表示 div 中包含 2 个以上的 p
//            :eq(n)	form input:eq(1) 表示只包含一个 input 的表单
//            :has(seletor)	div:has(p) 表示包含了 p 元素的 div
//            :not(selector)	div:not(.logo) 表示不包含 class=logo 元素的所有 div 列表
//            :contains(text)	包含某文本的元素，不区分大小写，例如 p:contains(oschina)
//            :containsOwn(text)	文本信息完全等于指定条件的过滤
//            :matches(regex)	使用正则表达式进行文本过滤：div:matches((?i)login)
//            :matchesOwn(regex)	使用正则表达式找到自身的文本

    public static void main(String[] args) throws IOException {
        System.out.println("hi");
        Document doc = Jsoup.connect("http://www.cnblogs.com/").get();
//        Elements links = doc.select("a[href]"); // 具有 href 属性的链接
//        for (Element link : links) {
//            System.out.println(link);
//        }
//        System.out.println("================================================");
//        Elements pngs = doc.select("img[src$=.png]");// 所有引用 png 图片的元素
//        for (Element link : pngs) {
//            System.out.println(link);
//        }
//        System.out.println("================================================");
//        Element masthead = doc.select("div.box").first();
//        // 找出定义了 class=masthead 的元素
//
//        System.out.println(masthead);
//        System.out.println("================================================");
//        Elements resultLinks = doc.select("h3.r > a"); // direct a after h3
//
//        for (Element link : resultLinks) {
//            System.out.println(link);
//        }
        System.out.println("================================================");

        //像js一样，通过标签获取title
        System.out.println(doc.getElementsByTag("title").first());
        //像js一样，通过id 获取文章列表元素对象
        Element postList = doc.getElementById("post_list");
        //像js一样，通过class 获取列表下的所有博客
        Elements postItems = postList.getElementsByClass("post_item");
        //循环处理每篇博客
        for (Element postItem : postItems) {
            //像jquery选择器一样，获取文章标题元素
            Elements titleEle = postItem.select(".post_item_body a[class='titlelnk']");
            System.out.println("文章标题:" + titleEle.text());;
            System.out.println("文章地址:" + titleEle.attr("href"));
            //像jquery选择器一样，获取文章作者元素
            Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
            System.out.println("文章作者:" + footEle.text());;
            System.out.println("作者主页:" + footEle.attr("href"));

            Elements footElepl = postItem.select(".post_item_foot .article_view a[class='gray']");
            System.out.println("阅读数:" + footElepl.text().replaceAll("[^0-9]", ""));;
            System.out.println("*********************************");

        }

        Document doc2 = Jsoup.connect("https://www.cnblogs.com/com3/p/11725053.html").get();
        Elements titleEle = doc2.select("#cnblogs_post_body h1");
        System.out.println(titleEle.text());
        Element titleElep = doc2.select("#cnblogs_post_body p").first();
        System.out.println(titleElep.text());
        Element titleElep2 = doc2.select("#cnblogs_post_body p").get(1);
        System.out.println(titleElep2.text());
        Elements liEle = doc2.select("#cnblogs_post_body li");
        int i =1;
        for (Element li:liEle) {
            System.out.println(i++ + ": " + li.text());
        }
        System.out.println(13222/1000);
    }
}

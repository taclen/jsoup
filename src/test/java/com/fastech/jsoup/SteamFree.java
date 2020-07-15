package com.fastech.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SteamFree {

    public static void main(String[] args) throws IOException {
        //https://steamdb.info/upcoming/free/?__cf_chl_captcha_tk__=d8e3bbb7643fa71e45513107cb148ce600afda59-1586064837-0-ASnF3j5W6QfA4lxyZQd3MHWl88BYdaQEjWurEYsl_W8hnTfgP5DE96mY4cl0bg4wvJDBP36Z5tAwMg8u-yoLsV-DELb8EIBlUmxPap2vnKfJkCI1KoBY7Be_aPFhHApEfVd33JnmMqByEgS9yAfzpeidES53R8STwHTxvluf9_cK3HdAHceomz15Wbi49KYr0kseq2jBi-mioXBots-UwplULem0yh1LrZl7DS7K1u0qOl1CpD4XDOItdzy8LEBaToZ8BQ78EjdGwvEd456q4_2ohbDT66rIWAjodiPf_qPXEjgkh47zs7CAYYn3wEHbdg
        String url = "https://steamdb.info/upcoming/free/";
        //body > div.footer-wrap > div.body-content > div.container > table:nth-child(3) > tbody > tr:nth-child(11) > td.price-discount > b
        //body > div.footer-wrap > div.body-content > div.container > table:nth-child(3) > tbody > tr:nth-child(15) > td.price-discount > b
        //#js-app-install
        //body > div.footer-wrap > div.body-content > div.container > table:nth-child(3) > tbody > tr:nth-child(11) > td.text-center
        Set<String> urls = new HashSet<>();
//        Document doc = Jsoup.connect(url).timeout(60000).get();//首页
        Connection con = Jsoup.connect("https://steamdb.info/upcoming/free/")
                .postDataCharset("gbk").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
//        //发送参数
//        con.data("r", "a398397caecd1b104154c3af3a9a9cc1a2c19ef9-1586064837-0-ASR+Ypy/uLsWlp/TGAorNwW4UkQlpxP/cVpnyDZHqv4RXp270blPyGxOqFumIJ7s6t58/h6vCLRLfby/C66KZOCxZcFdra3/2QRnKNntaFEwAHY3mRyb8ls5kPz+KCP1xxjV+cHo1sHZ7VtqoDFWut4Imp6hajvPZV4LkiC/LKrC+IRBTt5p6hfU8zDanRmk0HAtxE9r4f1ZrQ1KeqIO23/NVKVPSgavoFwsj0fBjp5gNpBrihSBTHCL831tzTrpHocIRXowLBw41XxB/Q2qWzKVv9TrwSHmtKjyMRrExgBv06aIwI6Z5wZCGTfGoszh0loIqNQ6fj3T69HHxZmo9TTDiyTi6/O3bxRMcttB/6qiIBgmqqilaBGECsdCH/X0DnzusZk7LODqtxhsuOZGyoDx7OIjocl27YoJH39dNDw5TlJJRUghslb64bw3m4D6Af/eJT9fSAHSFpH5wCjTnYEwZD+MbcanQcXzbJRowZ+3F91bcpYCN4gjjfcxpTUFzUhHyAdpHTwPFxptJUpq44paALe19JcDkPkd/HrZsBikwUwFe9mtbzB3S6utU+NSKNfshrqeRFJbx6RoCoVoMlxQqoiSFXI3ytQmh2NRocPzfkzxOzI0MBSSNbuodXK4El9d1Mmy+3pv+GpIcPwtzHhcxfLiOWmDlARXFmV0zH02Uue87iw0tZ5GTVfhZKmIH66Tplpkxr22zPAEGx4NG7bwIgt+LDBfk9K/hbe0/0tFTarsSDD3Y6vTXKzVy43p1J8MFr3xR+LxVJ5pObICBdCUHGAoR3GzHjRdrH0u+1in0F3dHSq62SikrvVbEZPTEOQkhYxjYJJf+3w6aqJJ0K6Oyk8dKIn4xkk88TSfOhn4g/ZXeXboenrXul1XYXIC7EAB0xKAarUezrXN+CgxJXqQJ65cWmgGZF3BTAl94oXQQcmBSlMsNfFWlZRB2L4y7C1iyEpV4DUEXrFxXadc8XGi17QRc9fCv+oH0GknMyhsYW39ThIH0yVUiyx7a7AAItS/Cw+fk/V5EoBIsmEA1zF12a4KE4FRU2DeFq3PU6I8D5g3xg+0zMhJtL9QEBa33DIlqifkCLgeFUFFCiOBVuZFXJC6EJDl4f68LJB5CoxJnupkqev8JxQXWNfiQvvw1O1IQ0gAChNQj1U3Fs1g+4IkNM5aeBUl6i/Q8j98kw57+iWDuVnYd5buHmTJ/g3j1dLZLA7X1RZVrotqO9lHAsPI0R3DiQTjUrSWE9TM4T4Ea+qqLXOdNZQS1s267/inI+oMhlstI1lwHF7pXcl0343u+7RSPB9A4N2jS0CGDazGFpPHNXzQOkx8L0gsQOc35oa/ILDe+LQ2Ru/my2VTzvM+cCq2qXXKFAPoM7bhLzjb9RwoqMHm1O6v+GHAJ9FdmOhbn2mI6+s2GBDzVgfhOk8BIvngkfZxBMICLpiauMAaQlHiBNxDPsCAm1lgxNCfjz486FF49SuwpsvObEESrZkL/dKDEbm0Fjmry6oukWM//1wd5L5OWor4DfmevBlkWc8PxFUT34TdGCJpY0zRO+Nu4JQ1WsrWZB9zzi0fwUnqOOtp+4KS8utIGnNxsz9DE1bMrTVpIHZmrY+vbDkgPZZL8lntuJ6sSqgoLv+4eCLdR3yPDl/InUb9t/odQd9wnWJFjEe5lnWQSqvzpZ0dA0xgN5SQVlTrP/AfFetuilxn3aDnCwl8KolofFdj4QmozmLFn603y+MVzXhFEFDA7rGvPf0L6LHqSIy7REWi6mZn6uLCa0hkanSytxw5goGkHfeV+48/LeMnXc0jGzLLuVuWoFhSZ8n4qutVDuX11Jjy");
//        con.data("id","57f0e5b3f970d37a");
//        con.data("captcha_challenge_field", "E6C78044F0795B6A071C4A96872F75EEBFE6484767C5A8524A77599AEC4C881F1D735E9378A75B76F8A2853BA84EE6E9");
//        con.data("manual_captcha_challenge_field", "mua83");
//        con.data("__cf_chl_captcha_tk__", "d8e3bbb7643fa71e45513107cb148ce600afda59-1586064837-0-ASnF3j5W6QfA4lxyZQd3MHWl88BYdaQEjWurEYsl_W8hnTfgP5DE96mY4cl0bg4wvJDBP36Z5tAwMg8u-yoLsV-DELb8EIBlUmxPap2vnKfJkCI1KoBY7Be_aPFhHApEfVd33JnmMqByEgS9yAfzpeidES53R8STwHTxvluf9_cK3HdAHceomz15Wbi49KYr0kseq2jBi-mioXBots-UwplULem0yh1LrZl7DS7K1u0qOl1CpD4XDOItdzy8LEBaToZ8BQ78EjdGwvEd456q4_2ohbDT66rIWAjodiPf_qPXEjgkh47zs7CAYYn3wEHbdg");
//
//        Document doc = con.post();
        con.header("authority", "steamdb.info");
        con.header("method", "GET");
        con.header("path", "/upcoming/free/");
        con.header("scheme", "https");
        con.header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        con.header("accept-encoding", "gzip, deflate, br");
        con.header("accept-language", "zh-CN,zh;q=0.9");
        con.header("cache-control", "max-age=0");
        con.header("cookie", "__cfduid=d01663ed9abf0c536cb1c8803285168131585568079; _ga=GA1.2.341966877.1585568081; _gid=GA1.2.167031413.1586006268; cf_clearance=dc1dcd24b2918e6421cff2a69e1249e05e7f49cc-1586064845-0-250; _gat=1");
        con.header("sec-fetch-mode", "navigate");
        con.header("sec-fetch-site", "none");
        con.header("sec-fetch-user", "1");
        con.header("upgrade-insecure-requests", "1");
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36");
        Document doc = con.get();
        System.out.println(doc);
        //body > div.footer-wrap > div.body-content > div.container > table:nth-child(3) > tbody > tr:nth-child(1) > td.price-discount > b
        Elements sts = doc.select("body > div.footer-wrap > div.body-content > div.container > table:nth-child(3) > tbody > tr:nth-child(n+1) > td.price-discount > b");
        System.out.println(sts.size());
        for (Element st :
                sts) {
            if (st.text().contains("Keep")){
                System.out.println(st);
                System.out.println(st.parent().parent().child(0).child(0).attr("href"));
            }
//            urls.add(st.attr("href"));
        }
    }
}

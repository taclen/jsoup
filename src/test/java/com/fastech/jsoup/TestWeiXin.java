package com.fastech.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web .bind.annotation.RestController;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qcl on 2019-03-28
 * 微信：2501902696
 * desc: 模版消息推送模拟
 */
@RestController
public class TestWeiXin {


    /*
     * 微信测试账号推送
     * */
    @GetMapping("/push")
    public void push() {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId("wx7c465cb28c4226cd");
        wxStorage.setSecret("42b09373faff2e9c95a28cd16e3bd8f4");
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);

        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("o5kho6DgC7SDry8zCmXuvHJGvrgI")//要推送的用户openid
                .templateId("https://mp.weixin.qq.com/mp/homepage?__biz=MzIwMDA5Mzk3NA==&hid=1&sn=01fb8b03fb10cfad73c6e247e914c4d7")//模版id
                .url("https://30paotui.com/")//点击模版消息要访问的网址
                .build();
        //3,如果是正式版发送模版消息，这里需要配置你的信息
        //        templateMessage.addData(new WxMpTemplateData("name", "value", "#FF00FF"));
        //                templateMessage.addData(new WxMpTemplateData(name2, value2, color2));
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }

    }
    public static void sendMsg(Map<String, String> cookie, String content,
                               String fakeId) throws IOException {

//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("tofakeid", fakeId);
//        map.put("content", content);
//        map.put("error", "false");
//        map.put("token", TOKEN);
//        map.put("type", "1");
//        map.put("ajax", "1");
//        String referrerUrl = "https://mp.weixin.qq.com/cgi-bin/singlesendpage?t=message/send&action=index&tofakeid="+fakeId+"&token="+TOKEN+"&lang=zh_CN";
//        Document document = Jsoup.connect(SEND_MSG).header(USER_AGENT_H, USER_AGENT).header(REFERER_H, INDEX_URL2).referrer(referrerUrl).data(map).cookies(cookie)
//                .post();
//        Element body = document.body();
//        document.hashCode();
//        document.hasText();
//        System.out.println(body.text());

    }

    public static void main(String[] args) {

    }
}

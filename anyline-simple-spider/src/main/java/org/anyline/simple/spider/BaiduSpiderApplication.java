package org.anyline.simple.spider;

import org.anyline.baidu.site.util.BaiduSiteClient;
import org.anyline.baidu.site.util.BaiduSiteConfig;
import org.anyline.baidu.site.util.SubmitResponse;
import org.anyline.net.HttpResponse;
import org.anyline.net.HttpUtil;
import org.anyline.spider.SpiderClient;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaiduSpiderApplication {
    public static void main(String[] args) {
         push();
    }

    /**
     * 推送给百度收录
     */
    public static void push() {
        //以下地址获取site与token
        //https://ziyuan.baidu.com/linksubmit/index?site=https://www.anyline.org
        String site = "https://www.anyline.org";
        String token = "";
        BaiduSiteConfig.register(site, token);
        BaiduSiteClient client = BaiduSiteClient.getInstance();
        List<String> urls = new ArrayList<>();
        urls.add("https://www.anyline.org");
        SubmitResponse response = client.submit(urls);
        System.out.println("提交成功数量:"+response.getSuccess() + " 当天剩余额度:"+response.getRemain());
    }

}

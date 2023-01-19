package org.anyline.simple.spider;

import org.anyline.baidu.site.util.BaiduSeoClient;
import org.anyline.baidu.site.util.BaiduSeoConfig;
import org.anyline.baidu.site.util.SubmitResponse;
import java.util.ArrayList;
import java.util.List;


public class BaiduSpiderApplication {

    /**
     * 推送给百度收录
     */
    public static void push() {
        //以下地址获取site与token
        //https://ziyuan.baidu.com/linksubmit/index?site=https://www.anyline.org
        String site = "https://www.anyline.org";
        String token = "";
        BaiduSeoConfig.register(site, token);
        BaiduSeoClient client = BaiduSeoClient.getInstance();
        List<String> urls = new ArrayList<>();
        urls.add("https://www.anyline.org");
        SubmitResponse response = client.submit(urls);
        System.out.println("提交成功数量:"+response.getSuccess() + " 当天剩余额度:"+response.getRemain());
    }

}

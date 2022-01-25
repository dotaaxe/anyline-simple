package org.anyline.simple.regular;

import org.anyline.net.HttpResult;
import org.anyline.net.HttpUtil;
import org.anyline.util.regular.RegularUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegularApplication {
    private static Logger log = LoggerFactory.getLogger(RegularApplication.class);
    public static void main(String[] args) throws Exception{
        start();
    }
    public static void start() throws Exception{
        String url = "http://doc.anyline.org/";
        //请求一个地址
        HttpResult result = HttpUtil.get(url);
        //响应状态
        int status = result.getStatus();
        //200代表成功
        if(status != 200){
            return;
        }
        //读取html源码
        String html = result.getText();

        //获取所有超链接(a标签)
        /*
         * 提取单标签+双标签
         * 不区分大小写
         * 0:全文 1:开始标签 2:标签name 3:标签体 (单标签时null) 4:结束标签 (单标签时null)
         * 注意标签体有可能是HTML片段，而不是纯文本
         */
        List<List<String>> list = RegularUtil.fetchAllTag(html,"a");
        log.warn("标签数量:"+list.size());
        for(List<String> item:list){
            log.warn("全文:"+item.get(0));
            log.warn("开始标签:"+item.get(1));
            log.warn("标签名称:"+item.get(2));
            log.warn("标签体:"+item.get(3));
            log.warn("结束标签:"+item.get(4));
        }

        //抽取所有 a标签和li标签
        //一定注意:这里的a有可能被包含在li内部，这时的a不会再抽取
        list = RegularUtil.fetchAllTag(html,"a","li");

        //如果
    }
}

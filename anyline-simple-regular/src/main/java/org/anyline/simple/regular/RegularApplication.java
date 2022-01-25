package org.anyline.simple.regular;

import org.anyline.net.HttpResult;
import org.anyline.net.HttpUtil;
import org.anyline.util.DateUtil;
import org.anyline.util.regular.RegularUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegularApplication {
    private static Logger log = LoggerFactory.getLogger(RegularApplication.class);
    public static void main(String[] args) throws Exception{


        //通过正则抽取标签
        //start();

        //如果不熟悉正则可能通过更简单的方式
        //截取String
        cut();

        //如果有许多相同的内容需要提取，如果商品列表中需要提取所有商品url
        //cuts();
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
    }

    public static void cut(){
        //放多情况下我们并不需要复杂的标签内容，只需要截取几个关键字
        //如提取商品名称和商品价格,而这两个值有可能是根其他内容混在一块的
        //如以下这段源码
        String html ="<div class='title' data-product='1001'>商品名称(限时)</div>"
        +"<div class='price'>一个货币符号:100.00</div>";
        //这时可以通过字符串截取的方式提取出价格
        //第0个参数:源数据
        //第1个到倒数第2个参数:100.00(就是我们要提取的价格) 之前出现的关键字
        //最后1个参数:100.00之后出现的第1个关键字

        //参数顺序:  源码,k1,k2,k3,kn-1,内容,kn

        String price = RegularUtil.cut(html, "price",":","</div>");
        log.warn("价格:{}",price);

        //许多情况下price有可能在源码中出现多次，这时需要多个关键字的组合来确认100.00的位置

        html = DateUtil.format("yyyy-MM-dd")+ "<div class='title' data-product='1001'>商品名称(限时)</div>" +
                "div class='src-price price'></div>" +
                "<div class='price'>一个货币符号:100.00</div>元";

        price = RegularUtil.cut(html,"src-price","price", "price",":","</div>");
        log.warn("价格:{}",price);

        //如果需要提取的内容在最后 如上面的单位:元
        String unit = RegularUtil.cut(html,"src-price","price", "price",":","</div>", RegularUtil.TAG_END);
        log.warn("单位:{}", unit);

        //同样的如果需要提取的内容在最开始位置 如上面的日期

        String ymd = RegularUtil.cut(html, RegularUtil.TAG_BEGIN, "<");
        log.warn("日期:{}", ymd);
    }


    //如果有许多相同的内容需要提取，如果商品列表中需要提取所有商品url
    public static void cuts(){
        String html = "<li><a href='a.html'>A</a></li>"
                +"<li><a href='b.html'>B</a></li>"
                +"<li><a href='c.html'>C</a></li>"
                +"<li><a href='d.html'>D</a></li>"
                +"<li><a href='e.html'>E</a></li>"
                ;
        List<String> urls = RegularUtil.cuts(html, "href","'","'");
        for(String url:urls){
            log.warn("url:{}",url);
        }
    }

}

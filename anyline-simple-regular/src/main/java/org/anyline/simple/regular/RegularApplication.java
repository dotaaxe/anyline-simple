package org.anyline.simple.regular;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.anyline.net.HttpResponse;
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
        //cut();

        //如果有许多相同的内容需要提取，如果商品列表中需要提取所有商品url
        //cuts();

        //针对html标签的匹配
        //html();
        //国家统计局 行政区 划分
        district();
    }

    /**
     * 行政区域解析
     * 为什么不用dom解析?
     * 格式要求太严格,大部分页面不符合要求
     */
    public static void district(){

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<ch.qos.logback.classic.Logger> logs =  lc.getLoggerList();
        for(ch.qos.logback.classic.Logger log:logs){
            log.setLevel(Level.ERROR);
        }

        String txt = HttpUtil.get("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/index.html").getText();
        //行截取出 内容部分 如果内容比较少 这一步可以省略

        String table = RegularUtil.cut(txt,"provincetable","</table");
        //System.out.println(table);
        //再截取出所有包含省的tr
        int idx = 0;
        List<String> trs = RegularUtil.cuts(table, "provincetr","</tr");
        for(String tr:trs){
            //System.out.println(tr);
            //从tr取出td 每个td中有一个省
            //这里不要写成<td> 标签内可能还有其他属性
            List<String> tds = RegularUtil.cuts(tr,"<td","</td");
            for(String td:tds){
                //只解析一个省 测试一下行了 不要给服务器造成太大压力
                if(idx ++ >0) {
                    return;
                }
                //><a href="11.html">北京市<br /></a>
                //System.out.println("province td:"+td);
                //province td:><a href="11.html">北京市<br /></a>
                //取出其中的id和名称
                //注意这里的href=中间可能有空格 所以 href 和 "(引号)分开写
                String id = RegularUtil.cut(td,"href","\"", ".html");
                String name = RegularUtil.cut(td, "html",">","<");
                System.out.println("province:"+id+":"+name);

                province(id, name);
            }
        }
    }
    //解析省页面的市
    public static void province(String id, String name){
        System.out.println("解析:"+id+":"+name);
        String txt = HttpUtil.get("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2021/"+id+".html").getText();
        //取出内容部分 这里有个表头需要跳过去
        String table = RegularUtil.cut(txt,"citytable","cityhead","</tr","</table");
        //从table中取出所有tr,每个tr代表一个市
        List<String> trs = RegularUtil.cuts(table, "<tr","</tr");
        int idx = 0;
        for(String tr:trs ){
            if(idx ++ >0) {
                return;
            }
            //tr中有两个td分别包含id和名称
            //<tr class="citytr"><td><a href="37/3702.html">370200000000</a></td>
            // <td><a href="37/3702.html">青岛市</a></td></tr>
            List<String> tds = RegularUtil.cuts(tr, "<td","</td");
            if(tds.size() < 2){
                continue;
            }
            id = RegularUtil.cut(tds.get(0), "href", "/", ".html");
            String code = RegularUtil.cut(tds.get(0), "href", ".html", ">", "<");
            //这里的name区域格式与id区域类似 所以取两次 /
            name = RegularUtil.cut(tds.get(1), "href", ".html", ">", "<");

            System.out.println("city:"+id+":"+code+":"+name);
            //这里也可能不解析td直接从tr中提取id code name
            id = RegularUtil.cut(tr, "href", "/", ".html");
            code = RegularUtil.cut(tr, "href", "/", ".html", ">", "<");
            name = RegularUtil.cut(tr, ".html", ".html", ">", "<");
            System.out.println("city:"+id+":"+code+":"+name);
        }

    }
    public static void start() throws Exception{

        String url = "http://doc.anyline.org/";
        //请求一个地址
        HttpResponse result = HttpUtil.get(url);
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
    public static void html() throws Exception{
        String url = "http://doc.anyline.org/";
        //请求一个地址
        HttpResponse result = HttpUtil.get(url);
        //响应状态
        int status = result.getStatus();
        //200代表成功
        if(status != 200){
            return;
        }
        //读取html源码
        String html = result.getText();
        //提取其中的一个url
        url = RegularUtil.fetchUrl(html);
        log.warn("提取单个url:{}", url);
        //提取全部url
        List<String> urls = RegularUtil.fetchUrls(html);
        for(String item:urls){
            log.warn("提取url:{}", item);
        }

        //抽取所有单标签
        //提取单标签 如<img> <br/>
        // 如果传入div等带有结束标签的参数 则只取出开始标签 <div> 不区分大小写 0:全文 1::标签name
        List<List<String>> tags = RegularUtil.fetchSingleTag(html,"img","br");
        for(List<String> tag:tags){
            log.warn("提取单标签:{},标签名:{}", tag.get(0), tag.get(1));
        }
        //提取双标签 如<div>content<div>
        // 依次取出p,table,div中的内容
        // 有嵌套时只取外层 只能提取同时有 开始结束标签的内容，不能提取单标签内容如<img> <br/>
        // 支持不同标签嵌套，但不支持相同标签嵌套
        // 不区分大小写
        // 0:全文 1:开始标签 2:标签name 3:标签体 4:结束标签
        tags = RegularUtil.fetchPairedTag(html,"a");
        for(List<String> tag:tags){
            log.warn("提取双标签:{},标签名:{}", tag.get(0), tag.get(2));
        }
	    //提取单标签+双标签
        //不区分大小写
        //0:全文 1:开始标签 2:标签name 3:标签体 (单标签时null) 4:结束标签 (单标签时null)

        tags = RegularUtil.fetchAllTag(html, "a","b","div");
        for(List<String> tag:tags){
            log.warn("提取单标签+双标签:{},标签名:{}", tag.get(0), tag.get(2));
        }

        //清除所有标签，只保留标签体
        String txt = RegularUtil.removeAllTag(html);
        log.warn("text:{}", txt);

        /*
         * 获取所有 包含attribute属性 的标签与标签体,不支持相同标签嵌套
         * [
         * 	[0:整个标签含标签体,1:开始标签,2:结束标签,3:标签体,4:标签名称],
         * 	[整个标签含标签体,开始标签,结束标签，标签体，标签名称]
         * ]
         */
        //所有包含href属性的标签
        tags = RegularUtil.getTagAndBodyWithAttribute(html, "href");
        for(List<String> tag:tags){
            log.warn("所有包含href属性的标签:{},标签名:{}", tag.get(0), tag.get(4));
        }

        //提取一个属性值
        html  = "<a href='a.html' class='item-a'>word</a>";
        String value = RegularUtil.fetchAttributeValue(html,"class");
        log.warn("属性class值:{}", value);

        //如果有多个相同的标签可以取出一个集合
        //提取class属性   0全文 1:属性name 2:引号('|") 3:属性值
        List<String> atr = RegularUtil.fetchAttribute(html,"class");
        log.warn("全文:{},属性key:{},属性value:{}", atr.get(0), atr.get(1), atr.get(3));

        html  = "<a href='a.html' class='item-a'>word</a><a href='b.html' class='item-b'>excel</a>";
        //取出所有属性值
        // 0全文  1:属性name 2:引号('|") 3:属性值
        List<List<String>> atrs = RegularUtil.fetchAttributeList(html,"class");
        for(List<String> item:atrs){
            log.warn("全文:{},属性key:{},属性value:{}", item.get(0), item.get(1), item.get(3));
        }
    }

}

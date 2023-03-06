package org.anyline.simple.special;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.HtmlUtil;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class HtmlTest {
    private Logger log = LoggerFactory.getLogger(HtmlTest.class);

    /**
     * 根据单元格内容生成表格<br/>
     * 什么情况下需要，通过OCR识别内容时，如果原文是表格形式，识别出来的结果排列会乱<br/>
     * <pre>
     * 如：原文是这样
     * ---------------
     * | A | B | C |
     * ---------------
     * | 1 | 2 | 3 |
     * ---------------
     * 识别结果可能会是这样
     * A
     * B
     * C
     * 1
     * 2
     * 3
     * 这时需要HtmlUtil.table(String 识别结果, int 每行几列, String 分隔符) 把识别出来的内容还原成表格形式
     * <pre/>
     */
    @Test
    public void table(){
        String s ="编码\n" +
                "\n" +
                "定义\n" +
                "\n" +
                "方向\n" +
                "\n" +
                "0x01\n" +
                "\n" +
                "车辆登入\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x02\n" +
                "\n" +
                "实时信息上报\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x03\n" +
                "\n" +
                "补发信息上报\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x04\n" +
                "\n" +
                "车辆登出\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x05~0x06\n" +
                "\n" +
                "平台传输数据占用\n" +
                "\n" +
                "自定义\n" +
                "\n" +
                "0x07\n" +
                "\n" +
                "心跳\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x08\n" +
                "\n" +
                "终端校时\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x09~0x7F\n" +
                "\n" +
                "上行数据系统预留\n" +
                "\n" +
                "上行\n" +
                "\n" +
                "0x80\n" +
                "\n" +
                "查询命令\n" +
                "\n" +
                "下行\n" +
                "\n" +
                "0x81\n" +
                "\n" +
                "设置命令\n" +
                "\n" +
                "下行\n" +
                "\n" +
                "0x82\n" +
                "\n" +
                "车载终端控制命令\n" +
                "\n" +
                "下行\n" +
                "\n" +
                "0x83--0xBF\n" +
                "\n" +
                "下行数据系统预留\n" +
                "\n" +
                "下行\n" +
                "\n" +
                "0xC0~0xFE\n" +
                "\n" +
                "平台交换自定义数据\n" +
                "\n" +
                "自定义\n" +
                "\n";
        String table = HtmlUtil.table(s,3,"\n\n");
        System.out.println(table);
    }
}

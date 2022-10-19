package org.anyline.simple.doc;

import org.anyline.data.entity.Column;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.office.docx.entity.WDocument;
import org.anyline.office.docx.entity.Wtable;
import org.anyline.office.docx.util.DocxUtil;
import org.anyline.service.AnylineService;
import org.anyline.util.FileUtil;
import org.dom4j.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootTest
public class DocTest {
    private static Logger log = LoggerFactory.getLogger(DocTest.class);

    @Autowired
    private AnylineService service;
    @Test
    public void init() throws Exception{
        String[] dbs = "lsbz_v2_system".split(",");
        for(String db:dbs){
            init(db);
        }
    }
    public void init(String db) throws Exception{
        //如果作一个基础服务，可能需要用户提交数据库连接信息
/*
        String user = "root";
        String pwd = "root";
        String driver = "com.mysql.cj.jdbc.Driver";
        String pool = "com.zaxxer.hikari.HikariDataSource";
        String url = "jdbc:mysql://192.168.220.100:3306/simple?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        DataSourceHolder.reg("default", pool, driver, url, user, pwd);
*/

        //模板文件
        File src = new File("D:\\db_tmp.docx");
        //根据模板文件创建一个新文件
        File tar = new File("D:\\db_"+db+"_"+System.currentTimeMillis()+".docx");
        FileUtil.copy(src, tar);
        //创建WDocument
        WDocument doc = new WDocument(tar);
        //获取带书签的word 表格
        Wtable wtable = doc.getTable("mk_tables");
        //当前数据库所有表

        LinkedHashMap<String, Table> tables = service.metadata().tables(db, null, null, null);
        int index = wtable.getTrSize();//在原有的序号基础上累加
        for(Table table:tables.values()){
            String name = table.getName();
            String comment = table.getComment();
            wtable.append(index++ + "",name, comment);
        }
        wtable = doc.getTable("mk_columns");   //找到columns表标题位置, 上一级在段落
        Element point = wtable.getSrc();
        for(Table table:tables.values()) {
            Element t = doc.after(point, "<span style='font-family-east:等线;font-family-ascii:Arial;font-size:小三;font-weight:700'>"+table.getComment()+"("+table.getName()+")</span>");
            LinkedHashMap<String, Column> columns = service.metadata().columns(table);
            Wtable tab = wtable.copy();
            point = doc.after(t, tab);
            //序号	属性中文名	属性英文名	属性类型、长度、精度	属性值域(换成备注)	约束
            index = 0;
            for(Column column:columns.values()){
                index ++;
                String comment = column.getComment();
                String name = comment;
                if(null != comment){
                    comment = comment.trim();
                    if(comment.contains(" ")){
                        name = comment.substring(0,comment.indexOf(" "));
                        comment = comment.substring(comment.indexOf(" "));
                    }
                }
                tab.insert(index+"", name, column.getName(), column.getFullType(), comment, column.isNullable()==1?"":"非空");
            }

            tab.remove(1); //删除第一行空行
        }


        StringBuilder builder = new StringBuilder();
        for(Table table:tables.values()) {
            LinkedHashMap<String, Index> indexs = service.metadata().indexs(table);
            for(Index item:indexs.values()){
                builder.append("<div>");
                builder.append(item.getTable());
                builder.append(".").append(item.getName()).append("(");
                LinkedHashMap<String, Column> columns = item.getColumns();
                index = 0;
                for(Column column:columns.values()){
                    if(index>0){
                        builder.append(",");
                    }
                    builder.append(column.getName());
                    index ++;
                }
                builder.append(")");
                if(item.isUnique()){
                    builder.append(" unique");
                }
                builder.append("</div>");
            }
        }

        doc.replace("mk_index", builder.toString());


        //操作完成后需要保存一下才会持久到磁盘
        doc.save();
    }
}

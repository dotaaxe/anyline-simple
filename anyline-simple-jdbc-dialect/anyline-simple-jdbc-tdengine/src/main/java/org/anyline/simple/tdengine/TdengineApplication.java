package org.anyline.simple.tdengine;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.STable;
import org.anyline.jdbc.entity.Table;
import org.anyline.jdbc.entity.Tag;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.anyline.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.LinkedHashMap;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class TdengineApplication {
    private static Logger log = LoggerFactory.getLogger(TdengineApplication.class);
    private static AnylineService service;
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(TdengineApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = context.getBean(AnylineService.class);
        ConfigTable.IS_DDL_AUTO_DROP_COLUMN = true;
        //table();
        stable();
       //tags();
    }
    public static void tags() throws Exception{
        //columns中包含tag
        LinkedHashMap<String, Column> columns = service.metadata().columns("s_table_user_0");
        for(Column col:columns.values()){
            System.out.println(col.getClass().getSimpleName()+":"+col.getName()+" " + col.getTypeName() + ":" + col.getComment());
        }
        System.out.println("===========================");
        LinkedHashMap<String, Tag> tags = service.metadata().tags("s_table_user_0");
        for(Tag tag:tags.values()){
            System.out.println("TAG:"+tag.getName()+" " + tag.getTypeName() + ":" + tag.getComment());
        }
        System.out.println("===========================");
        tags = service.metadata().tags(new STable("s_table_user"));
        for(Tag tag:tags.values()){
            System.out.println("TAG:"+tag.getName()+" " + tag.getTypeName() + ":" + tag.getComment());
        }
        Tag tag = new Tag();
    }
    public static void stable() throws Exception{
        System.out.println("\n-------------------------------- start stable  --------------------------------------------\n");

        LinkedHashMap<String,STable> tables = service.metadata().stables();
        log.warn(LogUtil.format("检索超表数量:"+tables.size(),36));
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }


        STable table = service.metadata().stable("s_table_user");
        if(null != table){
            log.warn("LogUtil.format(查询表结构:"+table.getName(),36);
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",36));
            service.ddl().drop(table);
        }

        table.addColumn("ID", "TIMESTAMP");
        table.addColumn("CODE","NCHAR(10)").setComment("编号");

        table.addTag("I", "int").setComment("int tag");
        table.addTag("D", "double");
        table.addTag("S", "nchar(10)");
        table.setName("s_table_user");

        log.warn(LogUtil.format("创建超表",36));
        service.ddl().save(table);

        table.addColumn("age","int");
        table.addColumn("NAME","NCHAR(10)");
        table.addTag("NT", "int");
        log.warn(LogUtil.format("超表添加列与标签",36));
        service.ddl().save(table);
        //创建子表
        for(int i=0;i < 10;i++){
            Table item = new Table();
            item.setName("s_table_user_"+i);
            item.setStableName("s_table_user");
            item.addTag("I", null,i);
            item.addTag("D", null, 10);
            item.addTag("S", null, "S"+i);
            log.warn(LogUtil.format("删除子表",36));
            service.ddl().drop(item);
            log.warn(LogUtil.format("创建子表",36));
            service.ddl().create(item);
        }

        DataSet set = new DataSet();
        for(int i=0;i<10;i++){
            DataRow row = new DataRow();
            row.put("ID", System.currentTimeMillis()+i);
            row.put("CODE", "C"+i);
            row.put("AGE", (i+1)*10);
            set.add(row);
        }
        log.warn(LogUtil.format("子表直接插入数量",36));
        service.insert("s_table_user_1", set);
        log.warn(LogUtil.format("通过超表向子表插入数量",36));

        System.out.println("\n-------------------------------- end stable  --------------------------------------------\n");
    }
    public static void table() throws Exception{
        System.out.println("\n-------------------------------- start table  --------------------------------------------\n");

        LinkedHashMap<String,Table> tables = service.metadata().tables();
        log.warn(LogUtil.format("检索表数量:"+tables.size(),36));
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }


        Table table = service.metadata().table("a_test");
        if(null != table){
            log.warn(LogUtil.format("查询表结构:"+table.getName(),36));
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",36));
            service.ddl().drop(table);
        }
        log.warn(LogUtil.format("创建表,第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键",36));
        table = new Table("a_test");
        //第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键
        table.addColumn("ID", "TIMESTAMP");
        table.addColumn("CODE","NCHAR(10)");
        table.addColumn("age","int");

        service.ddl().save(table);

        DataSet set = new DataSet();
        for(int i=0; i<10; i++){
            DataRow row = new DataRow();
            row.put("ID", System.currentTimeMillis()+i);
            row.put("CODE","C"+i);
            row.put("age", ""+i*10);
            set.add(row);
        }
        log.warn(LogUtil.format("批量插入数据",36));
        service.insert("a_test", set);

        int total = service.count("a_test");
        log.warn(LogUtil.format("统计数量:"+total,36));
        PageNavi navi = new PageNaviImpl();
        navi.setTotalRow(total);
        navi.setCurPage(2);
        navi.setPageRows(3);
        set = service.querys("a_test",navi);
        log.warn(LogUtil.format("分页查询",36));
        System.out.println(set);
        System.out.println("\n-------------------------------- end table  --------------------------------------------\n");
    }
}

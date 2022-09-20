package org.anyline.simple.tdengine;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.jdbc.entity.*;
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
        try {
            table();
            mtable();
            column();
            mcolumn();
            tag();
        }catch (Exception e){
            e.printStackTrace();
        }
        data();
       // clear();
    }
    public static void data(){

        DataSet set = new DataSet();
        for(int i=0;i<10;i++){
            DataRow row = new DataRow();
            row.put("ID", System.currentTimeMillis()+i);
            row.put("CODE", "C"+i);
            row.put("AGE", (i+1)*10);
            set.add(row);
        }
        log.warn(LogUtil.format("子表直接插入数量",34));
        service.insert("s_table_user_1", set);
        log.warn(LogUtil.format("通过超表向子表插入数量",34));
        set = new DataSet();
        set.tag("d","10");
    }
    public static void clear() throws Exception{

        Table table = service.metadata().table("a_test");
        log.warn(LogUtil.format("查看表Column(不含Tag)",34));
        for(Column col:table.getColumns().values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        service.ddl().drop(new Table("a_test"));

        MasterTable stable = service.metadata().mtable("s_table_user");
        log.warn(LogUtil.format("查看超表Column(不含Tag)",34));
        for(Column col:stable.getColumns().values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        log.warn(LogUtil.format("查看超表TAG",34));
        for(Tag tag:stable.getTags().values()){
            log.warn(LogUtil.format(tag.toString(),34));
        }
        service.ddl().drop(new MasterTable("s_table_user"));
    }
    public static void mcolumn() throws Exception{
        System.out.println("\n-------------------------------- start master table column  --------------------------------------------\n");

        LinkedHashMap<String, Column> columns = service.metadata().columns("s_table_user");
        log.warn(LogUtil.format("查看超表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        columns = service.metadata().columns("s_table_user_0");
        log.warn(LogUtil.format("查看壗子表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        Column column = new Column();
        column.setName("add_column");
        column.setTypeName("int");
        column.setTable("s_table_user");
        log.warn(LogUtil.format("超表添加Column:"+column.toString(),34));
        service.ddl().save(column);

        log.warn(LogUtil.format("超表修改column名:"+column.toString(),34));
        log.warn(LogUtil.format("超表不支持侯column名会抛出异常",31));
        try {
            column.update().setName("RENAME_COLUMN");
            service.ddl().save(column);
        }catch (Exception e){

        }

        log.warn(LogUtil.format("超表删除column:"+column.toString(),34));
        service.ddl().drop(column);
        System.out.println("\n-------------------------------- end master table column  --------------------------------------------\n");
    }
    public static void column() throws Exception{
        System.out.println("\n-------------------------------- start column  --------------------------------------------\n");

        LinkedHashMap<String, Column> columns = service.metadata().columns("a_test");
        log.warn(LogUtil.format("查看表Column(不含Tag)",34));
        for(Column col:columns.values()){
            log.warn(LogUtil.format(col.toString(),34));
        }
        Column column = new Column();
        column.setName("add_column");
        column.setTypeName("int");
        column.setTable("a_test");
        log.warn(LogUtil.format("表添加Column:"+column.toString(),34));
        service.ddl().save(column);

        log.warn(LogUtil.format("表修改column名:"+column.toString(),34));
        column.update().setName("RENAME_COLUMN");
        service.ddl().save(column);

        log.warn(LogUtil.format("表删除column:"+column.toString(),34));
        service.ddl().drop(column);
        System.out.println("\n-------------------------------- end column  --------------------------------------------\n");
    }
    public static void tag() throws Exception{
        System.out.println("\n-------------------------------- start tag  --------------------------------------------\n");

        LinkedHashMap<String, Tag> tags = service.metadata().tags("s_table_user");
        log.warn(LogUtil.format("查看超表TAG",34));
        for(Tag tag:tags.values()){
            log.warn(LogUtil.format(tag.toString(),34));
        }
        tags = service.metadata().tags("s_table_user_0");
        log.warn(LogUtil.format("查看子表TAG",34));
        for(Tag tag:tags.values()){
            log.warn(LogUtil.format(tag.toString(),34));
        }
        Tag tag = new Tag();
        tag.setName("add_tag");
        tag.setTypeName("int");
        tag.setTable("s_table_user");
        log.warn(LogUtil.format("超表添加tag:"+tag.toString(),34));
        service.ddl().save(tag);

        log.warn(LogUtil.format("超表修改tag名:"+tag.toString(),34));
        tag.update().setName("RENAME_TAG");
        service.ddl().save(tag);

        log.warn(LogUtil.format("超表删除tag:"+tag.toString(),34));
       // service.ddl().drop(tag);
        System.out.println("\n-------------------------------- end tag  --------------------------------------------\n");
    }
    public static void mtable() throws Exception{
        System.out.println("\n-------------------------------- start stable  --------------------------------------------\n");

        LinkedHashMap<String,MasterTable> tables = service.metadata().mtables();
        log.warn(LogUtil.format("检索超表数量:"+tables.size(),34));
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }


        MasterTable table = service.metadata().mtable("s_table_user");
        if(null != table){
            log.warn(LogUtil.format("查询表结构:"+table.getName(),34));
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",34));
            service.ddl().drop(table);
        }else{
            table = new MasterTable("s_table_user");

        }
        //table.setComment("表备注");
        table.addColumn("ID", "TIMESTAMP");
        table.addColumn("CODE","NCHAR(10)").setComment("编号");

        table.addTag("I", "int").setComment("int tag");
        table.addTag("D", "double");
        table.addTag("S", "nchar(10)");
        table.setName("s_table_user");

        log.warn(LogUtil.format("创建超表",34));
        service.ddl().save(table);

        table.addColumn("age","int");
        table.addColumn("NAME","NCHAR(10)");
        table.addTag("NT", "int");
        log.warn(LogUtil.format("超表添加列与标签",34));
        service.ddl().save(table);
        //创建子表
        for(int i=0;i < 10;i++){
            PartitionTable item = new PartitionTable();
            item.setName("s_table_user_"+i);
            item.setMasterName("s_table_user");
            item.addTag("I", null,i);
            item.addTag("D", null, 10);
            item.addTag("S", null, "S"+i);
            log.warn(LogUtil.format("删除子表",34));
            service.ddl().drop(item);
            log.warn(LogUtil.format("创建子表",34));
            service.ddl().create(item);
        }

        LinkedHashMap<String,PartitionTable> items = service.metadata().ptables(table);
        log.warn(LogUtil.format("根据主表查询子表",34));
        for(Table item:items.values()){
            log.warn("子表:"+item.getName());
        }
        System.out.println("\n-------------------------------- end stable  --------------------------------------------\n");
    }
    public static void table() throws Exception{
        System.out.println("\n-------------------------------- start table  --------------------------------------------\n");

        LinkedHashMap<String,Table> tables = service.metadata().tables();
        log.warn(LogUtil.format("检索表数量:"+tables.size(),34));
        for(Table table:tables.values()){
            log.warn("表:"+table.getName());
        }


        Table table = service.metadata().table("a_test");
        if(null != table){
            log.warn(LogUtil.format("查询表结构:"+table.getName(),34));
            LinkedHashMap<String,Column> columns = table.getColumns();
            for(Column column:columns.values()){
                log.warn("列:"+column.toString());
            }
            log.warn(LogUtil.format("删除表",34));
            service.ddl().drop(table);
        }
        log.warn(LogUtil.format("创建表,第一个字段必须是 TIMESTAMP，并且系统自动将其设为主键",34));
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
        log.warn(LogUtil.format("批量插入数据",34));
        service.insert("a_test", set);

        int total = service.count("a_test");
        log.warn(LogUtil.format("统计数量:"+total,34));
        PageNavi navi = new PageNaviImpl();
        navi.setTotalRow(total);
        navi.setCurPage(2);
        navi.setPageRows(3);
        set = service.querys("a_test",navi);
        log.warn(LogUtil.format("分页查询",34));
        System.out.println(set);
        System.out.println("\n-------------------------------- end table  --------------------------------------------\n");
    }
}

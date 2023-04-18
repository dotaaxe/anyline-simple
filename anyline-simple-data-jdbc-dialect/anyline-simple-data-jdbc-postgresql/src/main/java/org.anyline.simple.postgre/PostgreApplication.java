package org.anyline.simple.postgre;

import org.anyline.data.entity.Table;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class PostgreApplication {
    private static  AnylineService service;
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(PostgreApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        service = (AnylineService) ctx.getBean("anyline.service");
        DataRow.DEFAULT_KEY_KASE = KeyAdapter.KEY_CASE.SRC;
        DataRow.DEFAULT_PRIMARY_KEY = "id";
        init();
        dml();
       // metadata();
        //type();
        System.exit(0);
    }
    public static void dml(){
        //序列查询

        BigDecimal next = service.sequence("SIMPLE_SEQ");
        DataRow nexts = service.sequences("SIMPLE_SEQ", "SIMPLE_SEQ2");
        BigDecimal cur = service.sequence(false,"SIMPLE_SEQ");
        DataRow curs = service.sequences(false, "SIMPLE_SEQ", "SIMPLE_SEQ2");

        DataRow row = new DataRow();
        row.put("name", "张三");
        row.put("id", BasicUtil.getRandomString(32));
        service.insert("tb_user", row);
        System.out.println(row);
    }
    public static void init() throws Exception{
        Table table = service.metadata().table("tb_user");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("tb_user");
        table.addColumn("name", "varchar(32)");
        table.addColumn("id", "varchar(32)");
        table.addColumn("remark", "varchar(32)");
        service.ddl().create(table);
    }
    public static void metadata(){

        List<String> mts = service.columns("tb_user");
        System.out.println(BeanUtil.object2json(mts));
        //DataSet set = service.querys("tb_user(email)",0,1);
        //System.out.println(set);

        List<String> tables = service.tables();
        System.out.println(tables);
        tables = service.tables("TABLE");
        System.out.println(tables);
        tables = service.tables("ts_%","TABLE");
        System.out.println(tables);
        tables = service.tables("public","ts_%","TABLE");
        System.out.println(tables);
    }
    public static void type(){

        DataRow user = new DataRow();
        user.put("id", UUID.randomUUID());
        user.put("created_time", System.currentTimeMillis());
        user.put("first_name", BasicUtil.getRandomString(10));
        user.put("t1", new Date())      ; //date
        user.put("t2", new Date())      ; //timestamp
        user.put("t3", new Date())      ; //time
        user.put("t4", new Date())      ; //timestamptz
        user.put("t5", new Date())      ; //timetz
        user.put("t6", new Date())      ; //text 会执行 DateUtil.format(date);

        //日期类型会转换失败会换成null
        user.put("t1", "")              ; //date
        user.put("t2", "")              ; //timestamp
        user.put("t3", "")              ; //time
        user.put("t4", "")              ; //timestamptz
        user.put("t5", "")              ; //timetz
        //string类型不变
        user.put("t6", "")              ; //text 会执行 DateUtil.format(date);


        user.put("+t7", "100")          ; //numeric 如果传入“”会转换成null
        user.put("t8", "{\"a\":1}")     ; //json
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><config></config>";
        user.put("t9",xml)              ; //xml
        user.put("c1", 1)               ; //bit
        user.put("c2", true)            ; //bool
        user.put("c3","(1,1),(1,1)")    ; //box
        user.put("c4", new byte[0])     ; //bytea
        user.put("c5", "abc")           ; //char
        user.put("c6","192.168.0.1")    ; //cidr
        user.put("c7","1,1,5")          ; //circle

        user.put("v1",null)             ; //在执行插入更新时忽略
        user.put("v2", "")              ; //在执行插入更新时忽略
        user.put("+v3", null)           ; //强制参与更新和插入
        user.setIsNew(true);
        //t7 列的类型是numeric直接save会报错
        try {
            service.save("tb_user", user);
        }catch (Exception e){
            e.printStackTrace();
        }
        //保存前需要检测数据类型
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        service.save("tb_user", user);
        //只更新t6 t7
        service.update("tb_user",user,"t6","t7");
        //t6强制更新 其他按默认情况
        service.update("tb_user",user,"+t6");
        //只更新值有变化的列
        service.update("tb_user",user);
        //System.out.println(BeanUtil.object2json(service.metadatas("tb_user")));

    }
}

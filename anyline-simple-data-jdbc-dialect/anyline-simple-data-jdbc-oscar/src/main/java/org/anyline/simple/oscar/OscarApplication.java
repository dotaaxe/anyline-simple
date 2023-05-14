package org.anyline.simple.oscar;


import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.Table;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class OscarApplication {
    private static AnylineService service;
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(OscarApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService) context.getBean("anyline.service");
        init();
    }
    public static void init() throws Exception{

        Table table = new org.anyline.data.entity.Table("HR_EMPLOYEE").setComment("");
        //注意以下数据类型
        table.addColumn("ID"            , "BIGINT"       ).setComment("主键").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ).setComment("姓名")     ; // String          : nm
        table.addColumn("CODE"          , "varchar(50)"  ).setComment("工号")     ; // String          : workCode
        table.addColumn("BIRTHDAY"      , "DATE"         ).setComment("出生日期")  ; // Date            : birthday
        table.addColumn("JOIN_YMD"      , "varchar(10)"  ).setComment("入职日期")  ; // String          : joinYmd
        table.addColumn("SALARY"        , "float(10,2)"  ).setComment("工资")     ; // float           : salary
        table.addColumn("REMARK"        , "blob"         ).setComment("备注")     ; // byte[]          : remark
        table.addColumn("DATA_STATUS"   , "int"          ).setComment("状态").setDefaultValue(1);
        //注意SQL Server中一个表只能有一列TIMESTAMP， 不能设置默认值
        table.addColumn("CREATE_TIME"   , "DATETIME"    ).setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("UPDATE_TIME"   , "TIMESTAMP"   ).setComment("更新时间");
        service.ddl().create(table);
        Index index = new Index();
        index.addColumn("NAME");
        index.addColumn("CODE");
        index.setTable(table);
        service.ddl().add(index);
    }
}

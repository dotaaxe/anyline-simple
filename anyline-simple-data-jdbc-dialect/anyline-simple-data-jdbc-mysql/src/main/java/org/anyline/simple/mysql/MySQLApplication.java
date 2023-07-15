package org.anyline.simple.mysql;


import org.anyline.entity.DataRow;
import org.anyline.metadata.Column;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MySQLApplication {
    private static JdbcTemplate jdbc;
    private static AnylineService service;
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MySQLApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        jdbc = context.getBean(JdbcTemplate.class);
        service = context.getBean(AnylineService.class);
        /*
        try {
            System.out.println("getCatalog:" + jdbc.getDataSource().getConnection().getCatalog());
            System.out.println("getSchema:" + jdbc.getDataSource().getConnection().getSchema());
            DatabaseMetaData dbmd = jdbc.getDataSource().getConnection().getMetaData();
            ResultSet set =dbmd.getTables("simple", "def", null, null);
            while (set.next()) {
                System.out.println(set.getString("TABLE_NAME"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }*/
        type();
    }
    public static void type(){
        /*UPDATE `simple`.`t_check` SET `C_REAL` = 1, `C_SET` = 'B,C',
        `C_SMALL_INT` = 1, `C_TEXT` = '1', `C_TIME` = '22:37:55',
        `C_TIMESTAMP` = '2023-04-20 22:37:53',
        `C_TINY_INT` = 1, `C_TINY_TEXT` = '1', `C_VARCHAR` = '1' WHERE `ID` = 1
        * */
        DataRow row = service.query("t_check");
        for(String key:row.keys()){
            Column column = row.getMetadata(key);
            Object value = row.get(key);
            System.out.println("\n-----------------------\nCOLUMN:"+key);
            System.out.println("数据类型:"+column.getFullType());
            System.out.println("value:"+value);
            if(null != value){
                System.out.println("class:"+value.getClass().getName()+":"+value.getClass().getSimpleName());
            }
        }
    }
}

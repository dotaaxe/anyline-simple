package org.anyline.simple.oracle;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;


@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
public class OracleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(OracleApplication.class);
        ConfigurableApplicationContext context = application.run(args);
       AnylineService service = (AnylineService) context.getBean("anyline.service");
        DataSet set = service.querys("PRE_CASE_INFO");
        DataRow row = service.query("PRE_CASE_INFO");

        row.remove("ROW_NUMBER");
        set.remove("ROW_NUMBER");
        set.setPrimaryKey("SID");
        service.delete(set);
        System.out.println(set);
        for(DataRow r:set) {
            r.put("SID", BasicUtil.getRandomNumberString(10));
        }
        service.insert("PRE_CASE_INFO", set);

    }
}

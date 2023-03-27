package org.anyline.simple.id;


import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.*;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.Base64Util;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class IDApplication {
    private static AnylineService service;


    public static void main(String[] args) throws Exception{
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        SpringApplication application = new SpringApplication(IDApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService)context.getBean("anyline.service");

        DataRow row = new DataRow();
        row.put("NAME", "al");
        service.insert("CRM_USER", row);
    }


}

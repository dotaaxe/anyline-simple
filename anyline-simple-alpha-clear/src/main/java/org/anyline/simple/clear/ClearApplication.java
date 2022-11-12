package org.anyline.simple.clear;


import org.anyline.service.AnylineService; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan; 

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class ClearApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(ClearApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        //这里可以验证一下 service 有没有成功注入
        AnylineService service = context.getBean(AnylineService.class);
    }
}

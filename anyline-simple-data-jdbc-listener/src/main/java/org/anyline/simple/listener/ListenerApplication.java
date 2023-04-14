package org.anyline.simple.listener;


import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class ListenerApplication {

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(ListenerApplication.class);
        ConfigurableApplicationContext context = application.run(args);

        AnylineService service = context.getBean(AnylineService.class);
        service.query("CRM_USER");
    }
}

package org.anyline.data.jdbc.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MongoApplication {
    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(MongoApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        MongoTemplate mongo = context.getBean(MongoTemplate.class);


    }
}

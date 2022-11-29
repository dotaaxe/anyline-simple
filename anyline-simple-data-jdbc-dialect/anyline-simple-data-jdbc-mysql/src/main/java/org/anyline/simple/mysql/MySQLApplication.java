package org.anyline.simple.mysql;


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
    }
}

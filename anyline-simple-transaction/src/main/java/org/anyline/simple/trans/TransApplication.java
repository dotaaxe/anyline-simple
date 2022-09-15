package org.anyline.simple.trans;


import org.anyline.jdbc.ds.DataSourceHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class TransApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(TransApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(TransApplication.class);

        ConfigurableApplicationContext context = application.run(args);
        try {
            String url = "jdbc:mysql://192.168.220.100:3306/simple_sso?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
            DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
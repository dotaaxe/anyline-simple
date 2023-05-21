package org.anyline.data.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MongoApplication {
    public static void main1(String[] args) {

        SpringApplication application = new SpringApplication(MongoApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        MongoTemplate mongo = context.getBean(MongoTemplate.class);


    }
    public static void main(String args[]) throws Exception{ try {
        // 创建 MongoDB 连接
        MongoClient mongo = new MongoClient( "192.168.220.100" , 27017 );
        // 连接到 MongoDB
        MongoCredential credential;
        credential = MongoCredential.createCredential("root", "simple", "root".toCharArray());
        System.out.println("Connected to the database successfully");
        // 访问数据库
        MongoDatabase database = mongo.getDatabase("myDb");
        System.out.println("Credentials ::"+ credential);
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}

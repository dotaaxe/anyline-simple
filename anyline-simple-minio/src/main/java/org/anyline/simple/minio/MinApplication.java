package org.anyline.simple.minio;

import io.minio.messages.Bucket;
import org.anyline.mimio.util.MinioUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class MinApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MinApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        MinioUtil util = MinioUtil.getInstance();
        try{

            List<Bucket> buckets =  util.getAllBuckets();
            for(Bucket bucket:buckets){
                System.out.println(bucket.name());
            }
            String url = util.putObject("alcdn","a/b.txt",new File("D:\\a.txt"));
            System.out.println(url);
               url = MinioUtil.getInstance().getObjectURL("alcdn","a/b.txt");
            System.out.println(url);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

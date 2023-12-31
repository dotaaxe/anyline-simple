package org.anyline.simple.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class SQLApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SQLApplication.class);
		application.run(args);
	}

}

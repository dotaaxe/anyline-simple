package org.anyline.simple.sql;

import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class SQLApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SQLApplication.class);
		ConfigurableApplicationContext context = application.run(args);
		System.exit(0);
	}

}

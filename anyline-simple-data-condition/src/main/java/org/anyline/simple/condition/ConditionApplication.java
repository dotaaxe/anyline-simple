package org.anyline.simple.condition;

import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
@Import(DynamicDataSourceRegister.class) //2023-03-31之后的版本不需要这一行
public class ConditionApplication {

	private static Logger log = LoggerFactory.getLogger(ConditionApplication.class);
	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(ConditionApplication.class);

		ConfigurableApplicationContext context = application.run(args);

	}
}

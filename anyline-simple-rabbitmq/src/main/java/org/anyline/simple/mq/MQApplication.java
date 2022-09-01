package org.anyline.simple.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MQApplication  {


	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MQApplication.class);

		ConfigurableApplicationContext context = application.run(args);

	}
}

package org.anyline.simple.init;

import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class InitApplication {

	private static AnylineService service;

	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(InitApplication.class);
		ConfigurableApplicationContext context = application.run(args);
	}


}

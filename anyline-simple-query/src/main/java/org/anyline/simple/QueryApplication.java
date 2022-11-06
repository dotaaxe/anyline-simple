package org.anyline.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class QueryApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(QueryApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(QueryApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}

}

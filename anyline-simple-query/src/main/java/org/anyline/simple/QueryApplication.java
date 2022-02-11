package org.anyline.simple;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class QueryApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(QueryApplication.class);
	private static AnylineService service;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(QueryApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(QueryApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}

}

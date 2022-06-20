package org.anyline.simple;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.DateUtil;
import org.anyline.util.FileUtil;
import org.anyline.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.Date;
import java.util.List;


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

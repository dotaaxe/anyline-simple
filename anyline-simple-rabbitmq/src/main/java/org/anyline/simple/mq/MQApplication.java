package org.anyline.simple.mq;

import com.rabbitmq.client.*;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.net.HttpUtil;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.IOException;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MQApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(MQApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(MQApplication.class);

		ConfigurableApplicationContext context = application.run(args);

	}
}

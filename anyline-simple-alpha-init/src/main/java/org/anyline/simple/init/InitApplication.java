package org.anyline.simple.init;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.adapter.JDBCAdapter;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
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

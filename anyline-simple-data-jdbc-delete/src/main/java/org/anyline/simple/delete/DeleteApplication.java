package org.anyline.simple.delete;


import org.anyline.data.jdbc.ds.DataSourceHolder;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class DeleteApplication {

	private static AnylineService service;
	private static Logger log = LoggerFactory.getLogger(DeleteApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DeleteApplication.class);
		application.run(args);

	}

}

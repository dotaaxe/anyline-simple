package org.anyline.simple.doc;

import org.anyboot.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class DocApplication {

	private static Logger log = LoggerFactory.getLogger(DocApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DocApplication.class);

		ConfigurableApplicationContext context = application.run(args);

	}

}

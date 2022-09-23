package org.anyline.simple;

import org.anyline.entity.DataSet;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class DmApplication {



	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DmApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		AnylineService service = (AnylineService) context.getBean("anyline.service");
		DataSourceHolder.setDataSource("jt");

		DataSet set = service.querys("all_tables",0,10);
		set = service.querys("all_tables",0,10);
		System.out.println(set);
	}
}

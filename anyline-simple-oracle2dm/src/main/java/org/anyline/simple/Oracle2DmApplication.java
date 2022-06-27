package org.anyline.simple;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class Oracle2DmApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(Oracle2DmApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Oracle2DmApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		AnylineService service = (AnylineService) context.getBean("anyline.service");
		DataSourceHolder.setDataSource("jt");

		DataSet set = service.querys("all_tables",0,10);
		DataSourceHolder.setDataSource("oracle");
		set = service.querys("all_tables",0,10);
		System.out.println(set);
	}
}

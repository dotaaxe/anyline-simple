package org.anyline.simple.delimiter;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
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
public class DelimiterApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(DelimiterApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(DelimiterApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");
		ds(service); //开启前SQL中不加界定符
		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		ds(service);
		System.exit(0);

	}

	//切换数据源 以及动态注册数据源
	public static void ds(AnylineService service){
		//用<>表示数据源,执行完成后会自动切换回默认数据源
		service.query("<crm>crm_customer");

		service.query("HR_DEPARTMENT"); //这里查的还是默认数据源

		service.query("<erp>mm_material");
	}
}

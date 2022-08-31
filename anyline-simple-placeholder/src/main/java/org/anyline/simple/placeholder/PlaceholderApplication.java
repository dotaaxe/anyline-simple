package org.anyline.simple.placeholder;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class PlaceholderApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(PlaceholderApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		service.query("SELECT [ID] AS CD FROM HR_DEPARTMENT"); //未开启占位符配置，执行SQL时不会替换[] SQL执行异常
		ConfigTable.IS_SQL_DELIMITER_PLACEHOLDER_OPEN = true;
		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		//默认的占位符用 `
		service.query("SELECT `ID` AS CD FROM HR_DEPARTMENT");

		ConfigTable.SQL_DELIMITER_PLACEHOLDER = "[]";
		service.query("SELECT [ID] AS CD FROM HR_DEPARTMENT");
		service.query("[HR_DEPARTMENT]");

		System.exit(0);

	}
}

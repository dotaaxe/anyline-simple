package org.anyline.simple.tables;

import org.anyline.entity.DataSet;
import org.anyline.jdbc.config.TableBuilder;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class TablesApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(TablesApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");
		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		DataSet set = service.querys(TableBuilder.init().setTable("HR_SALARY(M.ID, M.BASE_PRICE, FE.NM AS EMPLOYEE_NM, FD.NM AS DEPARTMENT_NM) AS M")
				.left("HR_EMPLOYEE AS FE","M.EMPLOYEE_ID = FE.ID")
				.left("HR_DEPARTMENT AS FD", "FE.DEPARTMENT_ID = FD.ID")
				.build()
				,"M.yyyy:2017"
		);
		System.out.println(set);
		System.exit(0);
	}

}

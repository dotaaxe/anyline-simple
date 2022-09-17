package org.anyline.simple.dml;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.jdbc.ds.DataSourceHolder;
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
public class DMLApplication {

	private static AnylineService service;
	private static Logger log = LoggerFactory.getLogger(DMLApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DMLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);

		check(null, "MySQL");
		///check("pg", "PostgreSQL");
		//check("ms", "SQL Server");
		//check("oracle", "Oracle 11G");
		//check("db2", "DB2");

	}

	public static void check(String ds, String title) throws Exception{
		System.out.println("\n=============================== START " + title + "=========================================\n");
		if(null != ds) {
			DataSourceHolder.setDataSource(ds);
		}
		insert();
		query();
		page();
		delete();
		System.out.println("\n=============================== END " + title + "=========================================\n");
	}
	public static void insert() throws Exception{
		System.out.println("\n-------------------------------- start insert  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end insert  ----------------------------------------------\n");
	}

	public static void query() throws Exception{
		System.out.println("\n-------------------------------- start index  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end index  ----------------------------------------------\n");
	}

	public static void page() throws Exception{
		System.out.println("\n-------------------------------- start page  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end page  ----------------------------------------------\n");
	}
	public static void delete() throws Exception{
		System.out.println("\n-------------------------------- start delete  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end delete  ----------------------------------------------\n");
	}

}

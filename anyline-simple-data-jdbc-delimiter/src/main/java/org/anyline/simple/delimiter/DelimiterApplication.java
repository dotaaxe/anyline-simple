package org.anyline.simple.delimiter;

import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class DelimiterApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(DelimiterApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");
		//开启前SQL中不加界定符
		ds(service);
		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		//开启后自动生成的SQL中加界定符，但原生SQL不作任何修改
		ds(service);
		System.exit(0);

	}

	//切换数据源 以及动态注册数据源
	public static void ds(AnylineService service){
		//用<>表示数据源,执行完成后会自动切换回默认数据源
		service.query("<crm>crm_customer");

		//SELECT `ID`,`NM` FROM `HR_DEPARTMENT`
		//WHERE( `NM` LIKE concat('%',?,'%') AND  `ID` >= ? AND  CODE>1)
		service.query("HR_DEPARTMENT(ID, NM)","NM:%财务部%", "ID:>=1", "CODE>1"); //这里查的还是默认数据源

		//SELECT `ID`,NM AS NAME FROM `HR_DEPARTMENT`
		//这里的 NM AS NAME 也不加界定符
		service.query("HR_DEPARTMENT(ID, NM AS NAME)");


	}
}

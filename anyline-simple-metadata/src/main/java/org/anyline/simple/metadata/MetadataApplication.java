package org.anyline.simple.metadata;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class MetadataApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(MetadataApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		DataRow row = new DataRow();
		row.put("NM","TEST");
		row.put("AGE","20");

		try {
			//AGE 属性在表中不存在,直接插入会SQL异常
			service.insert("HR_DEPARTMENT", row);
		}catch (Exception e){
			e.printStackTrace();
		}

		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		//开启检测后，会先检测表结构，将不表中未出现的列过滤
		row.remove("ID");
		service.insert("HR_DEPARTMENT", row);
		row.remove("ID");
		//相同的表结构会有一段时间缓存，不会每次都读取物理表
		service.insert("HR_DEPARTMENT", row);

		row.put("REG_TIME","");	//类型转换失败会按null处理
		row.put("DATA_STATUS","");
		service.save("HR_DEPARTMENT", row);

		List<String> tables = service.tables();
		System.out.println(tables);
		tables = service.tables("TABLE");
		System.out.println(tables);
		tables = service.tables("bs_%","TABLE");
		System.out.println(tables);
		tables = service.tables("root","bs_%","TABLE");
		System.out.println(tables);

	}
}

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

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class MetadataApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(MetadataApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		DataRow row = new DataRow();
		row.put("NM","TEST");
		row.put("AGE","20");
		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		//开启检测后，会先检测表结构，将不表中未出现的列过滤
		service.insert("HR_DEPARTMENT", row);
		row.remove("ID");
		//相同的表结构会有一段时间缓存，不会每次都读取物理表
		service.insert("HR_DEPARTMENT", row);

		ConfigTable.IS_AUTO_CHECK_METADATA = false;
		row.remove("ID");
		//AGE 属性在表中不存在,直接插入会SQL异常
		service.insert("HR_DEPARTMENT", row);

		System.exit(0);

	}
}

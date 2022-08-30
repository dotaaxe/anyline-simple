package org.anyline.simple.special;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.config.ConfigStore;
import org.anyline.jdbc.config.impl.ConfigStoreImpl;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})

public class SpecialApplication {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(SpecialApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		//根据 ID 删除多行
		//DELETE FROM HR_EMPLOYEE WHERE ID IN(100,200)
		service.deletes("HR_EMPLOYEE", "ID", "100","200");

		//根据多列条件删除
		//DELETE FROM HR_EMPLOYEE WHERE ID = 1 AND NM = 'ZH'
		DataRow row = new DataRow();
		row.put("ID","1");
		row.put("NM", "ZH");
		service.delete("hr_empoyee", row, "ID","NM");




		//关于几个 空值 的查询条件
		ConfigStore store = new ConfigStoreImpl();
		store.addCondition("+ID", null);                // ID IS NULL
		store.addCondition("+REMARK", "");              // REMARK = ''
		store.addCondition("+IDX", "".split(","));      // IDX = ''
		store.addCondition("+CODE", new ArrayList<>());       // CODE IS NULL
		store.addCondition("+VAL", new String[]{});            // VAL IS NULL
		DataSet set = service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", store);


		System.exit(0);

	}
}

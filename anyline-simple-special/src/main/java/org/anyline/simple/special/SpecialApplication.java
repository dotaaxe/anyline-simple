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
		row.put("-REMARK","不更新");	//添加到row中 但不参与插入(更新)
		row.put("CODE", null);		//默认情况这值不参与插入(更新)， +表示强制参与插入(更新)
		service.delete("hr_empoyee", row, "ID","NM");




		//关于几个 空值 的查询条件
		ConfigStore store = new ConfigStoreImpl();
		store.addCondition("+ID", null);                // ID IS NULL
		store.addCondition("+REMARK", "");              // REMARK = ''
		store.addCondition("+IDX", "".split(","));      // IDX = ''
		store.addCondition("+CODE", new ArrayList<>());       // CODE IS NULL
		store.addCondition("+VAL", new String[]{});            // VAL IS NULL
		DataSet set = service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", store);



		//只更新CODE REMARK
		service.update("BS_VALUE",row, "CODE", "REMARK");
		//CODE强制更新 其他按默认情况
		service.update("BS_VALUE",row,"+CODE");
		//只更新值有变化的列
		service.update("BS_VALUE",row);

		System.exit(0);

	}
}

package org.anyline.simple.dml;


import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
@Import(DynamicDataSourceRegister.class)
public class DMLApplication {

	private static AnylineService service;
	private static Logger log = LoggerFactory.getLogger(DMLApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DMLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);
		boolean s = null instanceof  List;
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
		DataRow row = new DataRow();
		row.put("NM", "张三");
		row.put("DEPARTMENT_ID", 1);
		service.insert("HR_EMPLOYEE", row);
		//执行insert后row如果数据库自动生成ID这时会row中会有ID值
		//在有主键值的情况下执行save最终会调用update
		service.save("HR_EMPLOYEE", row);
		row.remove("ID");
		//如果没有主键值则执行insert
		service.save("HR_EMPLOYEE", row);

		DataSet set = new DataSet();
		for(int i=0; i<3; i++){
			DataRow item = new DataRow();
			item.put("NM", "name_"+i);
			item.put("DEPARTMENT_ID", i%2);
			set.add(item);
		}
		//注意因为没有主键值，所有以下两行都可以执行insert
		//区别是save需要逐行执行,因为需要逐行检测主键值, insert会批量执行
		service.insert("HR_EMPLOYEE", set);
		service.save("HR_EMPLOYEE", set);

		System.out.println("\n-------------------------------- end insert  ----------------------------------------------\n");
	}

	public static void query() throws Exception{
		System.out.println("\n-------------------------------- start query  --------------------------------------------\n");
		//查询情况比较灵活请参考  anyline-simple-query中的controller 约定格式参考 http://doc.anyline.org/s?id=1059
		//经常继承AnylineController 调用其中的里的condition()生成ConfigStore
		ConfigStore configs = new DefaultConfigStore();
		//查询总行数
		//int qty = service.count("CRM_USER");

		//查询全部
		//DataSet set = service.querys("CRM_USER");

		//按条件查询
		//set = service.querys("CRM_USER", configs,"ID:1");

		//FIND_IN_SET
		//如果从request中取值  condition("CODE:(code)")
		configs.and("CODE","9,0".split(","));
		configs.and("NM","a,b".split(","));
		//configs.and(Compare.FIND_IN_SET, "TYPES", "9");
		//传多个值时FIND_IN_SET默认与FIND_IN_SET_OR效果一样
		configs.and(Compare.FIND_IN_SET, "TYPES", "A,B".split(","));
		configs.and(Compare.FIND_IN_SET_OR, "TYPES", "1,2,3".split(","));
		configs.and(Compare.FIND_IN_SET_AND, "TYPES", "1,2,3".split(","));
		configs.or(Compare.FIND_IN_SET_OR, "TYPES", "4,5,6".split(","));
/*
		configs.or(Compare.FIND_IN_SET_AND, "TYPES", "4,5,6".split(","));
		configs.ors(Compare.FIND_IN_SET_OR, "TYPES", "4,5,6".split(","));
		configs.ors(Compare.FIND_IN_SET_AND, "TYPES", "4,5,6".split(","));*/
		//find_in_set 只在mysql中有实现 FIND_IN_SET(?,TYPES)
		service.querys("HR_EMPLOYEE", configs
		);

		System.out.println("\n-------------------------------- end query  ----------------------------------------------\n");
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

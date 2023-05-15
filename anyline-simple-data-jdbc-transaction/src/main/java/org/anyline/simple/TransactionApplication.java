package org.anyline.simple;


import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class TransactionApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(TransactionApplication.class);
	}

	/* *************************************************       先看说明         ********************************************************************************
                项目运行下 先创建数据库执行SQL
	 			sql目录下的下的sql脚本都执行一下，文件名对应数据库名主要用到simple,其他的(simple_sso,simple_crm,simple_erp是用来测试切换数据库的)
                插入基础测试数据(部门，职员数据)
     *********************************************************************************************************************************************************/

	public static void main(String[] args) {
		ConfigTable.IS_OPEN_PRIMARY_TRANSACTION_MANAGER = true;
		String json = "[{\"factory\":\"l82c9v019f6acebf1d2dd5d5f4d1e0e5089297b69d2a305c8139\",\"sd\":\"l8v012c99f6acebf1d2d0ca5740564add16f942380c56661c051\",\"receive\":\"德州春晓\",\"symd\":\"2023-05-09\",\"rymd\":\"\",\"remark\":\"\",\"items\":[{\"pci\":\"l82c99v01f6acebf1d2d32e60dd2c71c4a3c8603d8850f94a0ff\",\"color\":\"l8v012c99f6acebf1d2d991a5f1b7e78696119acc231ccaeaaad\",\"bm\":\"lv0182c99f6acebf1d2d7a0e9303142d7f072dfdd936dc57bade\",\"qty\":\"644\"}]}]";
		DataSet set = DataSet.parseJson(json);
		SpringApplication application = new SpringApplication(TransactionApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		//测试切换数据源
		AnylineService service = (AnylineService)context.getBean("anyline.service");
		ds(service);
	}

	//切换数据源 以及动态注册数据源
	public static void ds(AnylineService service){
		//用<>表示数据源,执行完成后会自动切换回默认数据源
		service.query("<crm>crm_customer");

		service.query("HR_DEPARTMENT"); //这里查的还是默认数据源

		service.query("<erp>mm_material");
		try {
			//动态注册一个数据源
			//数据要设置更多参数 放到map里
			String url = "jdbc:mysql://192.168.220.100:3306/simple_sso?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";


/*
			HikariDataSource ds = new com.zaxxer.hikari.HikariDataSource();
			ds.setJdbcUrl(url);
			ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
			ds.setUsername("root");
			ds.setPassword("root");
			DataSourceHolder.reg("sso", ds);*/
			Map<String,Object> params = new HashMap<>();
			params.put("url", url);
			params.put("poool", "com.zaxxer.hikari.HikariDataSource");
			params.put("driver", "com.mysql.cj.jdbc.Driver");
			params.put("user", "root");
			params.put("password", "root");
			DataSourceHolder.reg("sso", params);

		}catch (Exception e){
			e.printStackTrace();
		}
		service.query("<sso>sso_user");
		//固定数据源
		DataSourceHolder.setDataSource("crm");
		service.query("crm_customer"); //这一行执行完成后，数据源还是crm
		//切换回默认数据源
		DataSourceHolder.setDefaultDataSource();
		service.query("HR_DEPARTMENT");
	}
}

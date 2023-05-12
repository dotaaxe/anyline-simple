package org.anyline.simple;


import org.anyline.data.entity.Column;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.anyline.util.FileUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
//
public class DatasourceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(DatasourceApplication.class);
	}

	/* *************************************************       先看说明         ********************************************************************************
                项目运行下 先创建数据库执行SQL
	 			sql目录下的下的sql脚本都执行一下，文件名对应数据库名主要用到simple,其他的(simple_sso,simple_crm,simple_erp是用来测试切换数据库的)
                插入基础测试数据(部门，职员数据)
                有没有切换成功参考dao输出的日志[SQL:*][thread:*][ds:crm]
     *********************************************************************************************************************************************************/

	public static void main(String[] args) { 
		ConfigTable.IS_MULTIPLE_SERVICE = false;
		SpringApplication application = new SpringApplication(DatasourceApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		AnylineService service = (AnylineService)context.getBean("anyline.service");

		//切换数据源
		ds(service);
	}
	//切换数据源 以及动态注册数据源
	public static void ds(AnylineService service){
		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		DataSourceHolder.setDataSource("sso");
		DataRow row = service.query("sso_user");
		row = new DataRow();
		row.put("CODE",1);
		service.save("<crm>crm_customer", row);
		//查询表结构
		Table user = service.metadata().table("sso_user");
		//如果同一个数据源中可以操作多个数据库(要注意catalog、schema中不同的数据库中的区别)(mysql中catalog可以理解成数据库名)
		//前一行没有指定catalog则取当前连接中的catalog(就是配置文件中设置的数据库名)
		user = service.metadata().table("simple_sso",null,"sso_user");
		LinkedHashMap<String, Column> columns = user.getColumns();
		//直接查columns
		columns = service.metadata().columns("sso_user");
		for(Column column:columns.values()){
			System.out.println(column.getName());
		}

		ServiceProxy.execute("update sso_user set code = '123'");

		DataSourceHolder.setDefaultDataSource();

		//用<>表示数据源,执行完成后会自动切换回 切换前的数据库(而不是默认数据源)
		DataRow cust = service.query("<crm>crm_customer");
		cust.put("NM", 1);
		service.save(cust);
		service.query("HR_DEPARTMENT"); //这里查的还是默认数据源

		service.query("<erp>mm_material");
		try {
			//动态注册一个数据源(配置文件中配置过的将被这里覆盖)
			String url = "jdbc:mysql://192.168.220.100:3306/simple_sso?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
			DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");

			//如果需要设置更多参数 放到map里 参数名参考连接池类型(就是连接池配置文件中用的参数名)
			Map params = new Hashtable<>();
			params.put("url", url);
			params.put("type", "com.zaxxer.hikari.HikariDataSource");
			params.put("driver-class-name", "com.mysql.cj.jdbc.Driver");
			params.put("user-name", "root");
			params.put("password", "root");
			DataSourceHolder.reg("sso2", params);
		}catch (Exception e){
			e.printStackTrace();
		}
		service.query("<sso>sso_user");
		//固定数据源
		DataSourceHolder.setDataSource("crm");
		service.query("crm_customer"); //这一行执行完成后，数据源还是crm

		DataSourceHolder.setDataSource("sso", true);	//true表示SQL执行成功后就切换回上一次的数据源(不是默认数据源)
		service.query("sso_user"); //这一行执行完成后，数据源切换回crm
		service.query("crm_customer");

		//切换回默认数据源
		DataSourceHolder.setDefaultDataSource();
		service.query("HR_DEPARTMENT");



		//覆盖一个数据源
		/**************************************************************************************************************************
		 *
		 *                         注意如果需要覆盖数据源 先设置 spring.main.allow-bean-definition-overriding=true
		 *
		 *                         否则会 抛出异常
		 *
		 ***************************************************************************************************************************/
		try {
			String url = "jdbc:mysql://192.168.220.100:3306/simple_crm?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
			DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
		}catch (Exception e){
			e.printStackTrace();
		}

		//注意这里的sso实际已经指向了simple_crm数据库了
		service.query("<sso>crm_customer");

	}
}

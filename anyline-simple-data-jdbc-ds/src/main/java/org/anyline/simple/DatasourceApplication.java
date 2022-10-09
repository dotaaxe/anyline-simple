package org.anyline.simple;

import org.anyboot.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
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

		SpringApplication application = new SpringApplication(DatasourceApplication.class);

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
			DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
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

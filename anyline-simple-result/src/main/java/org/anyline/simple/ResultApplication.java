package org.anyline.simple;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class ResultApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(ResultApplication.class);
	}

	/* *************************************************       先看说明         ********************************************************************************
                项目运行下 先创建数据库执行SQL
	 			resources下的sql都执行一下，文件名对应数据库名主要用到simple,其他的(simple_sso,simple_crm,simple_erp是用来测试切换数据库的)
                插入基础测试数据(部门，职员数据)
     *********************************************************************************************************************************************************/

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(ResultApplication.class);
		//给每个人随机生成1-12月份薪资
		application.addListeners(new ApplicationAware());
		ConfigurableApplicationContext context = application.run(args);
	}

}

package org.anyline.simple;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class ResultApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(ResultApplication.class);
	private static AnylineService service;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(ResultApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(ResultApplication.class);
		//给每个人随机生成1-12月份薪资
		application.addListeners(new ApplicationAware());
		ConfigurableApplicationContext context = application.run(args);
		service = (AnylineService)context.getBean("anyline.service");
		result();
	}
	public static void result(){
		//薪资列表
		DataSet set = service.querys("HR_SALARY");

		//抽取 不重复的 姓名+年月
		DataSet yms = set.distinct("EMPLOYEE_NM","YM");
		for(DataRow ym:yms){
			log.warn("[姓名:{}][年月:{}]", ym.getString("EMPLOYEE_NM"), ym.getString("YM"));
		}
	}

}

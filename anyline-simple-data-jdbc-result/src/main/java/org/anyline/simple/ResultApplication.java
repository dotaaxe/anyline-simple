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
import org.springframework.context.annotation.Import;

import java.util.List;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

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

		//result();


	}


	//更多参考 http://doc.anyline.org/a?id=p298pn6e9o1r5gv78ac1vice624c62387fc25603ac848d20e44d3e0a75448cfe7d
	public static void result(){
		//薪资列表
		DataSet set = service.querys("V_HR_SALARY");
		log.warn("集合长度:{}",set.size());
		//抽取不重复的年月列表
		List<String> yms = set.getDistinctStrings("YM");
		int i=0;
		for(String ym:yms){
			log.warn("ym:{}", ym);
			if(i++>=10) {
				break;
			}
		}
		//如果需要多列 不重复的 姓名+年月
		DataSet eyms = set.distinct("EMPLOYEE_NM","YM");
		i = 0;
		for(DataRow eym:eyms){
			log.warn("[姓名:{}][年月:{}]", eym.getString("EMPLOYEE_NM"), eym.getString("YM"));
			if(i++>=10) {
				break;
			}
		}
		//根据部门ID和部门名称分组
		DataSet groups = set.group("DEPARTMENT_ID","DEPARTMENT_NM");
		log.warn("groups:{}", groups.size());
		for(DataRow group:groups){
			DataSet items = group.getItems();
			log.warn("[部门:{}][条目:{}]\n[top:{}]", group.getString("DEPARTMENT_NM"), items.size(), items.getRow(0).toJSON());
		}

		//筛选符合条件的集合
		//注意如果String类型 1与1.0比较不相等, 可以先调用convertNumber转换一下数据类型
		//key1,value1,key2:value2,key3,value3
		//"NM:zh%","AGE:>;20","NM","%zh%"

		//抽取人员ID=1的薪资列表
		DataSet items = set.getRows("EMPLOYEE_ID","1");
		log.warn("size:{}", items.size());
		//也可以用:分隔
		items = set.getRows("EMPLOYEE_ID:1");
		log.warn("size:{}", items.size());

		//多条件筛选
		items = set.getRows("EMPLOYEE_ID","2","DEPARTMENT_ID","1");
		log.warn("size:{}", items.size());

		//模糊查询
		items = set.getRows("BASE_PRICE:>12000","EMPLOYEE_NM:%雪%");
		log.warn("size:{}", items.size());

		//更复杂的条件可以通过select筛选
		items = set.select.between("BASE_PRICE", 10000,12000);
		log.warn("size:{}", items.size());

		items = set.select.like("EMPLOYEE_NM","%雪%");
		log.warn("size:{}", items.size());

		//指定佛为空的集合(任何一列不为空的都不返回)
		items = set.select.empty("EMPLOYEE_NM","EMPLOYEE_ID");
		log.warn("size:{}", items.size());

		items = set.select.lessEqual("BASE_PRICE",10000);
		log.warn("size:{}", items.size());

		items = set.select.in("DEPARTMENT_ID","1","2");
		log.warn("size:{}", items.size());

	}

}

package org.anyline.simple.hive;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class HiveApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(HiveApplication.class);
		ConfigurableApplicationContext context = application.run(args);
		org.slf4j.impl.StaticLoggerBinder s;
		AnylineService service = (AnylineService) context.getBean("anyline.service");
		String sql = "${CREATE TABLE crm_user(ID INT, NAME varchar(100))}";
		//service.execute(sql);
		DataSet set = service.querys("crm_user",0,10);
		System.out.println(set);
		DataSet users = new DataSet();
		for(int i=0; i<1;i++) {
			DataRow user = new DataRow();
			user.put("ID", (i+1));
			user.put("NAME", "USER_"+(i+1));
			users.add(user);
		}
		service.insert("crm_user", users);
		set = service.querys("crm_user",0,1);
		System.out.println(set);
	}
}

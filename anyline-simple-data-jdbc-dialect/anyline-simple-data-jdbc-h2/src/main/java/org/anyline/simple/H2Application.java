package org.anyline.simple;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class H2Application {



	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(H2Application.class);
		ConfigurableApplicationContext context = application.run(args);
		org.slf4j.impl.StaticLoggerBinder s;
		AnylineService service = (AnylineService) context.getBean("anyline.service");
		String sql = "${DROP TABLE IF EXISTS hr_user ; CREATE TABLE hr_user(ID INT, NAME TEXT)}";
		service.execute(sql);
		DataSet users = new DataSet();
		for(int i=0; i<20;i++) {
			DataRow user = new DataRow();
			user.put("ID", (i+1));
			user.put("NAME", "USER_"+(i+1));
			users.add(user);
		}
		service.insert("hr_user", users);
		DataSet set = service.querys("hr_user",0,1);
		System.out.println(set);
	}
}

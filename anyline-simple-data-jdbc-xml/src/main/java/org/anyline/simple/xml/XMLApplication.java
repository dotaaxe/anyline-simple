package org.anyline.simple.xml;

import org.anyline.data.param.TableBuilder;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class XMLApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(XMLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		DataSet users = service.querys("crm.hr.user:USER_LIST");
		System.out.println(users);
		System.exit(0);
	}

}

package org.anyline.simple.xml;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.TableBuilder;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.anyline.util.regular.Regular;
import org.anyline.util.regular.RegularUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class XMLApplication {

	public static void main(String[] args)  throws Exception{


		SpringApplication application = new SpringApplication(XMLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");


		service.querys("crm.hr.user:USER_LIST" , "ID:[1,2,3]" , "NAME:张三" , "CODE:A101", "CD:A1");

		// 如果是web环境 通常这样接收参数值 http://localhost?id=1&id=2&id=3或id=1,2,3 或 post提交 {id:[1,2,3]}
		// 参数格式参考 http://doc.anyline.org/s?id=1059
		// ConfigStore conditions = condition("ID:[id]);
		ConfigStore conditions = new DefaultConfigStore().param("ID", "1,2,3".split(","));
		service.querys("crm.hr.user:USER_LIST" , conditions, "NAME:张三" , "CODE:A101", "CD:A1");



		System.out.println("变量标识 :CODE");
		//SELECT * FROM CRM_USER  WHERE CODE = 'A101' AND  CODE = 'A1' AND NAME LIKE '%张三%' AND ID IN(?,?,?)
		service.querys("crm.hr.user:USER_LIST1",   "ID:[1,2,3]", "NAME:张三" , "CODE:A101", "CD:A1");

		System.out.println("变量标识 #{CODE}");
		//SELECT * FROM CRM_USER WHERE CODE = ? AND  CODE = 'A1' AND NAME LIKE '%张三%' AND ID IN(?,?,?)
		service.querys("crm.hr.user:USER_LIST2", "ID:[1,2,3]", "NAME:张三" , "CODE:A101", "CD:A1");


		System.out.println("变量标识 ${CODE}");
		//SELECT * FROM CRM_USER WHERE CODE = 'A101'  AND  CODE = 'A1' AND NAME LIKE '%张三%' AND ID IN(1,2,3)
		service.querys("crm.hr.user:USER_LIST3",   "ID:[1,2,3]", "NAME:张三" , "CODE:A101", "CD:A1");

		System.exit(0);
	}

}

package org.anyline.simple.mq;

import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class MQApplication  {


	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(MQApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		RabbitTemplate template = context.getBean(RabbitTemplate.class);
		while (true){
			DataRow row = new DataRow();
			row.put("ID", System.currentTimeMillis());
			row.put("TIME", DateUtil.format());
			template.convertAndSend("anyline_rout", row.toJSON());
			Thread.sleep(3000);
		}
	}
}

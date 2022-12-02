package org.anyline.simple.rabbitmq;

import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class ProducerApplication {


	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(ProducerApplication.class);

		ConfigurableApplicationContext context = application.run(args);
		Producer producer = new Producer();
		while (true){
			DataRow msg = new DataRow();
			msg.put("FLAG", System.currentTimeMillis());
			msg.put("LOCATION_TIME", DateUtil.format());
			producer.send(msg);
			Thread.sleep(3000);
		}
	}

}

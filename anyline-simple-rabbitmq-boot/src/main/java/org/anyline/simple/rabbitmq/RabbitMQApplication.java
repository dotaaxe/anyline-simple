package org.anyline.simple.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class RabbitMQApplication {


	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(RabbitMQApplication.class);
		application.run(args);
	}
}

package org.anyline.simple.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(RedisApplication.class);
		application.run(args);
	}

}

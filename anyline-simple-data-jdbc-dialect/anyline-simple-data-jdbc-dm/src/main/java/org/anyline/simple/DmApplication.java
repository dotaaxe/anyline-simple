package org.anyline.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DmApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DmApplication.class);
		application.run(args);
	}
}

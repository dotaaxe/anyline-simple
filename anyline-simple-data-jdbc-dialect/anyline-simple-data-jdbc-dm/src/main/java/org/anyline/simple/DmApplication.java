package org.anyline.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("org.anyline")
@SpringBootApplication
public class DmApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(DmApplication.class);
		application.run(args);
	}
}

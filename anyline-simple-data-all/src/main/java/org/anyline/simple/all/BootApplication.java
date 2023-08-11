package org.anyline.simple.all;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootApplication {


	public static void main(String[] args){
		SpringApplication application = new SpringApplication(BootApplication.class);
		application.run(args);


	}

}

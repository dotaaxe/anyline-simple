package org.anyline.simple.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DDLApplication {


	public static void main(String[] args){
		SpringApplication application = new SpringApplication(DDLApplication.class);
		application.run(args);


	}

}

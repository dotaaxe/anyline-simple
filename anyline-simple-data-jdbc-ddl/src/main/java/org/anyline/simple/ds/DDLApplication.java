package org.anyline.simple.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.anyline")
public class DDLApplication {


	public static void main(String[] args){
		SpringApplication application = new SpringApplication(DDLApplication.class);
		application.run(args);


	}

}

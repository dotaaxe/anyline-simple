package org.anyline.simple.doc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class DocApplication {

	private static Logger log = LoggerFactory.getLogger(DocApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DocApplication.class);

		ConfigurableApplicationContext context = application.run(args);

	}

}

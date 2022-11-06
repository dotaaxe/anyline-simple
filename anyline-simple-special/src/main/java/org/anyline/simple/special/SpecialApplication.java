package org.anyline.simple.special;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class SpecialApplication {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(SpecialApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}
}

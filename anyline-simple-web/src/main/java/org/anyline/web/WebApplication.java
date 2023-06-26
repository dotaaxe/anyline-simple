package org.anyline.web;

import org.anyline.dao.init.springjdbc.DefaultDao;
import org.anyline.data.jdbc.ds.EnvironmentListener;
import org.anyline.service.init.DefaultService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"}
		,excludeFilters = @ComponentScan.Filter(
			type = FilterType.ASSIGNABLE_TYPE,
			classes = {DefaultDao.class, DefaultService.class, EnvironmentListener.class}
		)
)
public class WebApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(WebApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(WebApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}

}

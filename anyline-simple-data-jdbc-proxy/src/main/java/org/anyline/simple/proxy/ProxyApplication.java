package org.anyline.simple.proxy;


import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class ProxyApplication {
	private static Logger log = LoggerFactory.getLogger(ProxyApplication.class);
	public static void main(String[] args) throws Exception{
		SpringApplication application = new SpringApplication(ProxyApplication.class);
		ConfigurableApplicationContext context = application.run(args);
		//返回一个对应cms数据源的service
		AnylineService service = ServiceProxy.service("cms");
		//以下操作都是针对cms数据源，运行过程中不能切换数据源
		service.query("a_test");

		//返回一个对应默认数据源的service
		service = ServiceProxy.service();
		//以下操作都是针对默认数据源
		service.query("a_test");

		//ServiceProxy有两个应用场景
		//1.用来代替AnylineService
		//2.用来切换数据源
		//详细说明参考 http://doc.anyline.org/s?id=p298pn6e9o1r5gv78acvic1e624c62387f51d08504f16eef5d2bbd26817f54f0e3
	}
}

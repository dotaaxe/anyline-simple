package org.anyline.simple;


import com.alibaba.druid.pool.DruidDataSource;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.util.DataSourceUtil;
import org.anyline.entity.DataRow;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
//
public class DatasourceApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(DatasourceApplication.class);
	}

	/* *************************************************       先看说明         ********************************************************************************
                项目运行下 先创建数据库执行SQL
	 			sql目录下的下的sql脚本都执行一下，文件名对应数据库名主要用到simple,其他的(simple_sso,simple_crm,simple_erp是用来测试切换数据库的)
                插入基础测试数据(部门，职员数据)
                有没有切换成功参考dao输出的日志[SQL:*][thread:*][ds:crm]
     *********************************************************************************************************************************************************/

	public static void main(String[] args) {
		//ConfigTable.IS_MULTIPLE_SERVICE = false;
		SpringApplication application = new SpringApplication(DatasourceApplication.class);

		ConfigTable.IS_PRINT_EXCEPTION_STACK_TRACE = true;
		ConfigurableApplicationContext context = application.run(args);
		AnylineService service = (AnylineService)context.getBean("anyline.service");
		//切换数据源
		//ds(service);
		temporary();
	}

	/**
	 * 临时数据源，用完后被GC自动回收，默认不支持事务
	 */
	public static  void temporary(){
		String url = "jdbc:mysql://localhost:23306/simple?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
		DruidDataSource ds = new DruidDataSource();
		ds.setUrl(url);
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("root");
		ds.setConnectionErrorRetryAttempts(3);
		ds.setBreakAfterAcquireFailure(true);
		ds.setConnectTimeout(3000);
		ds.setMaxWait(30000);
		AnylineService service = ServiceProxy.temporary(ds);
		//AnylineService service = (AnylineService) SpringContextUtil.getBean("anyline.service");

		LinkedHashMap<String, Table> tables = service.metadata().tables();
		//测试有没有泄漏 没有发现
		for(String key:tables.keySet()){
			System.out.println(key);
			for(int i=0; i<100;i++){
				service.query(key);
			}
			service.metadata().table(key);
		}

		DataSource ds1 = DataSourceUtil.build("com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
	    service = ServiceProxy.temporary(ds1);
		tables = service.metadata().tables();
		for(String key:tables.keySet()){
			System.out.println(key);
		}
		//如果需要管理事务
		DataSourceTransactionManager dstm = new DataSourceTransactionManager(ds1);
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		// 定义事务传播方式 以及 其他参数都在definition中设置
		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = dstm.getTransaction(definition);
		//service.insert();
		dstm.commit(status);


	}
	//切换数据源 以及动态注册数据源
	public static void ds(AnylineService service){
		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		DataSourceHolder.setDataSource("sso");
		DataRow row = service.query("sso_user");
		row = new DataRow();
		row.put("CODE",1);
		service.save("<crm>crm_customer", row);
		//查询表结构
		Table user = service.metadata().table("sso_user");
		//如果同一个数据源中可以操作多个数据库(要注意catalog、schema中不同的数据库中的区别)(mysql中catalog可以理解成数据库名)
		//前一行没有指定catalog则取当前连接中的catalog(就是配置文件中设置的数据库名)
		user = service.metadata().table("simple_sso",null,"sso_user");
		LinkedHashMap<String, Column> columns = user.getColumns();
		//直接查columns
		columns = service.metadata().columns("sso_user");
		for(Column column:columns.values()){
			System.out.println(column.getName());
		}

		ServiceProxy.execute("update sso_user set code = '123'");

		DataSourceHolder.setDefaultDataSource();

		//用<>表示数据源,执行完成后会自动切换回 切换前的数据库(而不是默认数据源)
		DataRow cust = service.query("<crm>crm_customer");
		cust.put("NM", 1);
		service.save(cust);
		service.query("HR_DEPARTMENT"); //这里查的还是默认数据源

		service.query("<erp>mm_material");
		try {
			//动态注册一个数据源(配置文件中配置过的将被这里覆盖)
			String url = "jdbc:mysql://localhost:13306/simple_sso?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
			DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");

			//如果需要设置更多参数 放到map里 参数名参考连接池类型(就是连接池配置文件中用的参数名)
			Map params = new Hashtable<>();
			params.put("url", url);
			params.put("type", "com.zaxxer.hikari.HikariDataSource");
			params.put("driverClassName", "com.mysql.cj.jdbc.Driver");
			params.put("userName", "root");
			params.put("password", "root");
			DataSourceHolder.reg("sso2", params);
		}catch (Exception e){
			e.printStackTrace();
		}
		AnylineService service_sso = ServiceProxy.service("sso");
		service_sso.query("sso_user");
		service_sso.query("sso_user");
		service.query("<sso>sso_user");
		//固定数据源
		DataSourceHolder.setDataSource("crm");
		service.query("crm_customer"); //这一行执行完成后，数据源还是crm

		DataSourceHolder.setDataSource("sso", true);	//true表示SQL执行成功后就切换回上一次的数据源(不是默认数据源)
		service.query("sso_user"); //这一行执行完成后，数据源切换回crm
		service.query("crm_customer");

		//切换回默认数据源
		DataSourceHolder.setDefaultDataSource();
		service.query("HR_DEPARTMENT");



		//覆盖一个数据源
		/**************************************************************************************************************************
		 *
		 *                         注意如果需要覆盖数据源 先设置 spring.main.allow-bean-definition-overriding=true
		 *
		 *                         否则会 抛出异常
		 *
		 ***************************************************************************************************************************/
		String url = "jdbc:mysql://localhost:13306/simple_crm?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
		try {
			DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
		}catch (Exception e){
			e.printStackTrace();
		}

		//注意这里的sso实际已经指向了simple_crm数据库了
		service.query("<sso>crm_customer");
		try {
			///druid数据源
			DruidDataSource ds_druid1 = new DruidDataSource();
			ds_druid1.setUrl(url);
			ds_druid1.setDriverClassName("com.mysql.cj.jdbc.Driver");
			ds_druid1.setUsername("root");
			ds_druid1.setPassword("root");
			DataSourceHolder.reg("ds_druid1", ds_druid1);
			DataSourceHolder.setDataSource("ds_druid1");
			service.query("crm_customer");
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			//动态注册一个数据源(配置文件中配置过的将被这里覆盖)
			DataSourceHolder.reg("ds_druid2", "com.alibaba.druid.pool.DruidDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");

			DataSourceHolder.setDataSource("ds_druid2");
			service.query("crm_customer");
		}catch (Exception e){
			e.printStackTrace();
		}
		try {
			//如果需要设置更多参数 放到map里 参数名参考连接池类型(就是连接池配置文件中用的参数名)
			Map params = new Hashtable<>();
			params.put("url", url);
			params.put("type", com.alibaba.druid.pool.DruidDataSource.class);
			params.put("driverClass", "com.mysql.cj.jdbc.Driver");
			params.put("userName", "root");
			params.put("password", "root");
			params.put("beanName", "ds_druid3");
			DataSourceHolder.reg("ds_druid3", params);
			DataSourceHolder.setDataSource("ds_druid3");
			service.query("crm_customer");
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void check(AnylineService service) throws Exception{
		Table table = service.metadata().table("a_test");
		if(null != table){
			service.ddl().drop(table);
		}
		table = new Table("a_test");
		table.setComment("表备注");
		table.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
		table.addColumn("REG_TIME", "TIME");
		service.ddl().save(table);
		DataRow row = new DataRow();
		row.put("REG_TIME", LocalTime.now());
		//service.insert("a_test", row);

	}
}

package org.anyline.simple.dml;


import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.anyline.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class DMLApplication {

	private static AnylineService service;
	private static String seq = null;
	private static Logger log = LoggerFactory.getLogger(DMLApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DMLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = (AnylineService)SpringContextUtil.getBean("anyline.service");
		check(null, "MySQL");
		//check("pg", "PostgreSQL");
		//check("ms", "SQL Server");
		//check("ms2000", "SQL Server 2000");
		//check("oracle", "Oracle 11G");
		//check("db2", "DB2");

	}

	public static void check(String ds, String title) throws Exception{
		System.out.println("\n=============================== START " + title + "=========================================\n");
		if(null != ds) {
			DataSourceHolder.setDataSource(ds);
		}
		seq = null;
		if("oracle".equals(ds)){
			seq = "SIMPLE_SEQ";
			if(null != service.query("USER_SEQUENCES","SEQUENCE_NAME:" + seq)) {
				service.execute("DROP SEQUENCE " + seq);
			}
			String sql = "CREATE SEQUENCE "+seq+" MINVALUE 0 START WITH 0 NOMAXVALUE INCREMENT BY 1 NOCYCLE CACHE 100";
 			service.execute(sql);
		}
		init();
		//date();
		insert();
		query();
		page();
		delete();
		System.out.println("\n=============================== END " + title + "=========================================\n");
	}
	public static void init(){
		try {
			Table table = service.metadata().table("CRM_USER");
			if (null != table) {
				service.ddl().drop(table);
			}
			table = new Table("CRM_USER");
			table.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
			table.addColumn("CODE", "varchar(20)");
			table.addColumn("NAME", "varchar(20)");
			table.addColumn("AGE", "int");
			table.addColumn("YMD", "DATE");
			table.addColumn("YMD_HMS", "DATETIME");
			table.addColumn("HMS", "TIME");
			table.addColumn("L","LONG").setComment("long");
			table.addColumn("NM","varchar(50)").setComment("名称");
			table.addColumn("my","money").setComment("金额");
			table.addColumn("REG_TIME","DATETIME").setComment("日期");
			table.addColumn("CREATE_TIME","DATE").setComment("日期");
			table.addColumn("DEPARTMENT_ID","INT");
			table.addColumn("performance","decimal(10,2)");
			service.ddl().save(table);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	//日期类型
	public static void date() throws Exception{
 

		//ConfigTable.IS_AUTO_CHECK_METADATA = true;
		DataRow row = new DataRow();
		row.put("YMD", new Date());
		row.put("YMD_HMS", new Date());
		row.put("HMS", new Date());
		if(null != seq){
			row.put("ID", "${"+seq+".NEXTVAL}");
		}
		service.insert("CRM_DATE", row);
	}
	public static void insert() throws Exception{

		DataRow r  = service.query("CRM_USER");
		//ConfigTable.IS_AUTO_CHECK_METADATA = true;
		System.out.println("\n-------------------------------- start insert  --------------------------------------------\n");


		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		ConfigTable.IS_INSERT_EMPTY_COLUMN = true;
		ConfigTable.IS_UPDATE_EMPTY_COLUMN = true;
		DataRow row = new DataRow();
		row.put("REG_TIME", "2020-01-01");
		row.put("NM","ZH");
		row.put("age","");
		row.put("performance", 12.2d);
		row.put("my","10.1"); //money
		row.put("CREATE_TIME", new Date());
		row.put("DEPARTMENT_ID", 1);
		if(null != seq){
			row.put("ID", "${"+seq+".NEXTVAL}");
		}
		//row.setOverride(true);
		ServiceProxy.insert("CRM_USER", row);

		row = service.query("CRM_USER");

		//执行insert后row如果数据库自动生成ID这时会row中会有ID值
		//在有主键值的情况下执行save最终会调用update
		ServiceProxy.save("CRM_USER", row);
		row.remove("ID");

		//如果没有主键值则执行insert
		service.save("CRM_USER", row);


		DataSet set = new DataSet();
		for(int i=0; i<3; i++){
			DataRow item = new DataRow();
			item.put("NM", "name_"+i);
			item.put("DEPARTMENT_ID", i%2);
			set.add(item);
		}

		//注意因为没有主键值，所有以下两行都可以执行insert
		//区别是save需要逐行执行,因为需要逐行检测主键值, insert会批量执行
		if(null != seq){
			set.put("ID", "${"+seq+".NEXTVAL}");
		}
		service.insert("CRM_USER", set);
		service.save("CRM_USER", set);

		System.out.println("\n-------------------------------- end insert  ----------------------------------------------\n");
	}

	public static void query() throws Exception{
		System.out.println("\n-------------------------------- start query  --------------------------------------------\n");
		//查询情况比较灵活请参考
		// web环境不需要new DefaultConfigStore 参考 anyline-simple-query中的controller
		//经常继承AnylineController 调用其中的里的condition()生成ConfigStore condition约定格式参考 http://doc.anyline.org/s?id=1059
		ConfigStore configs = new DefaultConfigStore();
		//查询总行数
		long qty = service.count("CRM_USER");

		//查询全部
		DataSet set = service.querys("CRM_USER");

		//按条件查询
		set = service.querys("CRM_USER", configs,"ID:1");

		//FIND_IN_SET
		//如果从request中取值  condition("[CODE]:CODE");condition("[CODE]:split(CODE)")

		configs.and("ID","9,0".split(","));
		configs.and("NM","a,b".split(","));
		configs.and(Compare.NOT_LIKE ,"NM", "ZH");
		//如果传入的值为空，则生成 WHERE CODE IS NULL
		configs.and(Compare.EMPTY_VALUE_SWITCH.NULL, "CODE" , "");
		//如果传入的值为空，按原样处理 会生成 NM=''或NM IS NULL
		configs.and(Compare.EMPTY_VALUE_SWITCH.SRC, "NM" ,null);
		Column ctype = service.metadata().column("CRM_USER","TYPES");
		if(null == ctype){
			ctype = new Column("CRM_USER","TYPES").setType("varchar(100)");
			service.ddl().add(ctype);
		}
		configs.and(Compare.FIND_IN_SET, "TYPES", "9");
		//传多个值时FIND_IN_SET默认与FIND_IN_SET_OR效果一样
		//configs.and(Compare.FIND_IN_SET, "TYPES", "A,B".split(","));
		//configs.and(Compare.FIND_IN_SET_OR, "TYPES", "1,2,3".split(","));
		//configs.and(Compare.FIND_IN_SET_AND, "TYPES", "1,2,3".split(","));
		//configs.or(Compare.FIND_IN_SET_OR, "TYPES", "4,5,6".split(","));

		//configs.or(Compare.FIND_IN_SET_AND, "TYPES", "4,5,6".split(","));
		//configs.ors(Compare.FIND_IN_SET_OR, "TYPES", "4,5,6".split(","));
		//configs.ors(Compare.FIND_IN_SET_AND, "TYPES", "4,5,6".split(","));
		//find_in_set 只在mysql中有实现 FIND_IN_SET(?,TYPES)
		service.querys("CRM_USER", configs, "ID:");

		System.out.println("\n-------------------------------- end query  ----------------------------------------------\n");
	}

	public static void page() throws Exception{
		System.out.println("\n-------------------------------- start page  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end page  ----------------------------------------------\n");
	}
	public static void delete() throws Exception{
		System.out.println("\n-------------------------------- start delete  --------------------------------------------\n");
		System.out.println("\n-------------------------------- end delete  ----------------------------------------------\n");
	}

}

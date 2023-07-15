package org.anyline.simple.metadata;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.metadata.Column;
import org.anyline.metadata.Index;
import org.anyline.metadata.Table;
import org.anyline.metadata.View;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class MetadataApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(MetadataApplication.class);
	private static AnylineService service = null;
	private static String seq = null;
	public static void main(String[] args) throws Exception{
		org.postgresql.util.PGobject s;
		SpringApplication application = new SpringApplication(MetadataApplication.class);
		ConfigurableApplicationContext context = application.run(args);

		service = (AnylineService)context.getBean("anyline.service");

		check(null, "MySQL");
		//check("pg", "PostgreSQL");
		//check("ms", "SQL Server");
		//check("oracle", "Oracle 11G");

		//check("td", "TDengine");
		//check("db2", "DB2");

	}

	public static void check(String ds, String title) throws Exception{
		System.out.println("=============================== START " + title + "=========================================");
		if(null != ds) {
			DataSourceHolder.setDataSource(ds);
		}
		seq = null;
		if("oracle".equals(ds)){
			seq = "SIMPLE_SEQ";
			if(service.querys("USER_SEQUENCES","SEQUENCE_NAME:" + seq).size()>0) {
				service.execute("DROP SEQUENCE " + seq);
			}
			String sql = "CREATE SEQUENCE "+seq+" MINVALUE 0 START WITH 0 NOMAXVALUE INCREMENT BY 1 NOCYCLE CACHE 100";

			service.execute(sql);
		}
		view();
		table();
		tables();
		column();
		tag();
		index();
		exception();
		System.out.println("=============================== END " + title + "=========================================");
	}
	public static void table() throws Exception{
		System.out.println("-------------------------------- start  stable  ------------------------------------------");

		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		Table table = service.metadata().table("hr_department");
		try {
			if(null != table){
				service.ddl().drop(table);
			}
			table = new Table("hr_department");
			table.addColumn("ID", "INT").setPrimaryKey(true).setAutoIncrement(true);
			table.addColumn("NM", "varchar(50)");
			table.addColumn("REG_TIME", "datetime");
			table.addColumn("DATA_STATUS", "int");
			table.addColumn("QTY", "int");
			service.ddl().create(table);
		}catch (Exception e){
			e.printStackTrace();
		}


		DataRow row = new DataRow();
		row.put("NM","TEST");
		row.put("AGE","20");
		if(null != seq){
			row.put("ID", "${"+seq+".NEXTVAL}");
		}

		try {
			//AGE 属性在表中不存在,直接插入会SQL异常
			service.insert("hr_department", row);
		}catch (Exception e){
			log.error("AGE 属性在表中不存在,直接插入会SQL异常:"+e.getMessage());
		}

		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		//开启检测后，会先检测表结构，将不表中未出现的列过滤
		row.remove("ID");

		if(null != seq){
			row.put("ID", "${"+seq+".NEXTVAL}");
		}

		service.insert("hr_department", row);
		row.put("TMP_COLUMN","TMP");
		service.save("hr_department", row);


		row.remove("ID");
		if(null != seq){
			row.put("ID", "${"+seq+".NEXTVAL}");
		}

		//相同的表结构会有一段时间缓存，不会每次都读取物理表
		service.insert("hr_department", row);

		row.put("REG_TIME","");						//datetime 类型转换失败会按null处理
		row.put("AGE",1);							//数据库中没有的列 不会参与更新
		row.put("QTY","");							//int 类型转换失败会按null处理
		row.put("DATA_STATUS","1");					//int 类型转换成int
		service.save("hr_department", row);

		//所有表名,支持模糊匹配
		List<String> tables = service.tables();
		System.out.println(tables);
		tables = service.tables("TABLE");
		System.out.println(tables);
		tables = service.tables("bs_%","TABLE");
		System.out.println(tables);
		tables = service.tables("root","bs_%","TABLE");
		System.out.println(tables);

		//所有表(不包含列、索引等结构)
		LinkedHashMap<String, Table> tbls = service.metadata().tables();
		//表结构(不包含列、索引等结构)
		table = service.metadata().table("hr_department");
		LinkedHashMap<String, Column> columns = table.getColumns();
		System.out.println(table.getName()+" 属性:");
		for(Column column:columns.values()){
			System.out.println("\t"+column.toString());
		}
		List<Column> pks = table.primarys();

		System.out.println(table.getName()+" 主键:");
		for(Column column:pks){
			System.out.println("\t"+column.toString());
		}

		LinkedHashMap<String, Index> indexs = table.getIndexs();
		for(Index index:indexs.values()){
			System.out.println(table.getName()+"所引:"+index.getName()+ " 类型:"+index.getType()+ " 唯一:"+index.isUnique()+" 包含列:");
			columns = index.getColumns();
			for(Column column:columns.values()){
				System.out.println("\t"+column.toString());
			}
		}
		System.out.println("-------------------------------- end  stable  --------------------------------------------");
	}
	public static void tag() throws Exception{
		System.out.println("-------------------------------- start tag  ----------------------------------------------");
		System.out.println("-------------------------------- end tag  ------------------------------------------------");
	}
	public static void index() throws Exception{
		System.out.println("-------------------------------- start index  --------------------------------------------");
		System.out.println("-------------------------------- end index  ---------------------------------------------");
	}
	public static void view() throws Exception{
		Table table = service.metadata().table("hr_department");
		if(null == table){
			table = new Table("hr_department");
			table.addColumn("ID", "INT").setPrimaryKey(true).setAutoIncrement(true);
			table.addColumn("NM", "varchar(50)");
			table.addColumn("REG_TIME", "datetime");
			table.addColumn("DATA_STATUS", "int");
			table.addColumn("QTY", "int");
			table.addColumn("REG_DATE", "DATE");
			service.ddl().create(table);
		}
		View view = service.metadata().view("v_hr_department");
		if(null != view){
			service.ddl().drop(view);
		}
		view = new View("v_hr_department");
		view.setDefinition("SELECT * FROM hr_department");
		view.setComment("视图备注");
		service.ddl().create(view);
		Map<String,View> views = service.metadata().views();
		System.out.println(views);
	}
	public static void tables() throws Exception{
		System.out.println("-------------------------------- start tables  --------------------------------------------");
		LinkedHashMap<String,Table> tables = service.metadata().tables();
		for(String key:tables.keySet()){
			Table table = tables.get(key);
			log.warn("table:"+table.getName());
			log.warn("comment:"+table.getComment());
		}

		//当前schema中没有的表 默认查不到
		Table table = service.metadata().table("art_comment");
		if(null != table) {
			System.out.println(table.getCatalog() + ":" + table.getSchema() + ":" + table.getName());
		}
		//当前schema中没有的表 greedy=rue 可以查到其他schema中的表
		table = service.metadata().table(true,"art_comment");
		if(null != table) {
			System.out.println(table.getCatalog() + ":" + table.getSchema() + ":" + table.getName());
		}

		System.out.println("-------------------------------- end tables  ----------------------------------------------");
	}
	public static void column() throws Exception{
		System.out.println("-------------------------------- start column  -------------------------------------------");
		Column col = service.metadata().column("hr_department","ID");
		log.warn("column: ID, type:{}", col.getFullType());
		LinkedHashMap<String,Column> columns = service.metadata().columns("hr_department");
		for(Column column: columns.values()){
			log.warn("column:{}\ttype:{}\tauto increment:{}",column.getName(), column.getFullType(), column.isAutoIncrement()==1);
		}
		System.out.println("-------------------------------- end column  --------------------------------------------");
	}
	public static void exception() throws Exception{
		System.out.println("-------------------------------- start exception  ----------------------------------------");
		System.out.println("-------------------------------- end  exception  -----------------------------------------");
	}



}

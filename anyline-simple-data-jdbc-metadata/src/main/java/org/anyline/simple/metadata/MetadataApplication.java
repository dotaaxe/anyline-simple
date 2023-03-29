package org.anyline.simple.metadata;

import org.anyline.data.entity.Column;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
@Import(DynamicDataSourceRegister.class)
public class MetadataApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(MetadataApplication.class);
	private static AnylineService service = null;

	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(MetadataApplication.class);
		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);

		//check(null, "MySQL");
		//check("pg", "PostgreSQL");
		check("ms", "SQL Server");
		//check("oracle", "Oracle 11G");
		//check("td", "TDengine");
		//check("db2", "DB2");

	}

	public static void check(String ds, String title) throws Exception{
		System.out.println("=============================== START " + title + "=========================================");
		if(null != ds) {
			DataSourceHolder.setDataSource(ds);
		}
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
		DataRow row = new DataRow();
		row.put("NM","TEST");
		row.put("AGE","20");

		try {
			//AGE 属性在表中不存在,直接插入会SQL异常
			service.insert("HR_DEPARTMENT", row);
		}catch (Exception e){
			log.error("AGE 属性在表中不存在,直接插入会SQL异常:"+e.getMessage());
		}

		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		//开启检测后，会先检测表结构，将不表中未出现的列过滤
		row.remove("ID");
		service.insert("HR_DEPARTMENT", row);
		row.put("TMP_COLUMN","TMP");
		service.save("HR_DEPARTMENT", row);


		row.remove("ID");
		//相同的表结构会有一段时间缓存，不会每次都读取物理表
		service.insert("HR_DEPARTMENT", row);

		row.put("REG_TIME","");						//datetime 类型转换失败会按null处理
		row.put("AGE",1);							//数据库中没有的列 不会参与更新
		row.put("QTY","");							//int 类型转换失败会按null处理
		row.put("DATA_STATUS","1");					//int 类型转换成int
		service.save("HR_DEPARTMENT", row);

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
		Table table = service.metadata().table("HR_DEPARTMENT");
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

	public static void tables() throws Exception{
		System.out.println("-------------------------------- start tables  --------------------------------------------");
		LinkedHashMap<String,Table> tables = service.metadata().tables();
		for(String key:tables.keySet()){
			Table table = tables.get(key);
			log.warn("table:"+table.getName());
			log.warn("comment:"+table.getComment());
		}
		System.out.println("-------------------------------- end tables  ----------------------------------------------");
	}
	public static void column() throws Exception{
		System.out.println("-------------------------------- start column  -------------------------------------------");
		Column col = service.metadata().column("HR_DEPARTMENT","ID");
		log.warn("column: ID, type:{}", col.getFullType());
		LinkedHashMap<String,Column> columns = service.metadata().columns("HR_DEPARTMENT");
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

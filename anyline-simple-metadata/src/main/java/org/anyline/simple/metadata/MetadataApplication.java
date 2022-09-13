package org.anyline.simple.metadata;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.Index;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class MetadataApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(MetadataApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");

		//JdbcTemplate jdbc = context.getBean(JdbcTemplate.class);
		//test(jdbc);



		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		DataRow row = new DataRow();
		row.put("NM","TEST");
		row.put("AGE","20");

		try {
			//AGE 属性在表中不存在,直接插入会SQL异常
			service.insert("HR_DEPARTMENT", row);
		}catch (Exception e){
			System.out.println("AGE 属性在表中不存在,直接插入会SQL异常:"+e.getMessage());
			e.printStackTrace();
		}

		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		//开启检测后，会先检测表结构，将不表中未出现的列过滤
		row.remove("ID");
		service.insert("HR_DEPARTMENT", row);
		row.remove("ID");
		//相同的表结构会有一段时间缓存，不会每次都读取物理表
		service.insert("HR_DEPARTMENT", row);

		row.put("REG_TIME","");	//类型转换失败会按null处理
		row.put("DATA_STATUS","1");//类型转换成int
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
		List<Table> tbls = service.metadata().tables();
		//表结构(不包含列、索引等结构)
		Table table = service.metadata().table("HR_DEPARTMENT");
		LinkedHashMap<String, Column> columns = table.getColumns();
		System.out.println(table.getName()+" 属性:");
		for(Column column:columns.values()){
			System.out.println("\t"+column.toString());
		}
		List<Column> pks = table.getPrimaryKeys();

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
	}

	public static void test(JdbcTemplate jdbc){
		try {
			Connection con = DataSourceUtils.getConnection(jdbc.getDataSource());
			DatabaseMetaData metaData = con.getMetaData();

			ResultSet rs = metaData.getColumns(con.getCatalog(), con.getSchema(), "HR_DEPARTMENT", null);
			ResultSetMetaData rsm = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i < rsm.getColumnCount(); i++) {
					System.out.println(rsm.getColumnName(i) + "=" + rs.getObject(i));
				}
				System.out.println("==================");
			}
			rs = metaData.getIndexInfo(con.getCatalog(), con.getSchema(), "HR_DEPARTMENT",false, false);

			ResultSetMetaData md = rs.getMetaData();
			LinkedHashMap<String, Column> cols = null;
			System.out.println("========PK==========");
			while (rs.next()) {
				for(int i=1; i<md.getColumnCount(); i++){
					System.out.println(md.getColumnName(i)+"="+rs.getObject(i));
				}

			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}

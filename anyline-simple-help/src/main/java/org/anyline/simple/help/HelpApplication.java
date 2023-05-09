package org.anyline.simple.help;


import org.anyline.entity.DataRow;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.entity.Column;
import org.anyline.data.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.ClassUtil;
import org.anyline.util.SnowflakeWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})

public class HelpApplication {

	private static AnylineService service;
	private static JdbcTemplate jdbc;
	private static DataSource ds = null;
	private static Connection con = null;

	public static void main(String[] args) throws Exception{


		SpringApplication application = new SpringApplication(HelpApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = (AnylineService)context.getBean("anyline.service");
		jdbc = context.getBean(JdbcTemplate.class);
		//DataSourceHolder.setDataSource("pg");
		ds = jdbc.getDataSource();
		con = DataSourceUtils.getConnection(ds);
		//td();
		//tdtags();
		//convert();
		type();
	}
	public static void type(){
		List<Map<String,Object>> maps = service.maps("type_check");
		for(Map map:maps) {
			for (Object key : map.keySet()) {
				Object value = map.get(key);
				System.out.print(key+":");
				if(null != value){
					System.out.println(ClassUtil.type(value.getClass()));
				}else {
					System.out.println();
				}
			}
		}
	}
	public static void convert(){
		String[] types = (
				"String\n" +
						"java.util.Date\n" +
						"java.sql.Date\n" +
						"java.sql.Time\n" +
						"java.sql.Timestamp\n" +
						"java.time.LocalDate\n" +
						"java.time.LocalTime\n" +
						"java.time.LocalDateTime\n"+
						"oracle.sql.DATE\n"+
						"oracle.sql.TIMESTAMP\n"+
						"oracle.sql.TIMESTAMPTZ\n"+
						"oracle.sql.TIMESTAMPLTZ\n"+
						"oracle.sql.DATE\n"
		).split("\n");
		for(String t1:types){
			for(String t2:types){
				t1 = t1.trim();
				t2 = t2.trim();
				if(t1.contains("oracle") && t2.contains("oracle")){
					continue;
				}
				if(!t1.contains("oracle") && !t2.contains("oracle")){
					continue;
				}
				if(!t1.equals(t2)){
					String str = "ConvertAdapter.reg(new AbstractConvert("+t1+".class, "+t2+".class) {\n" +
							"\t@Override\n" +
							"\tpublic Object exe(Object value, Object def) throws ConvertException {\n" +
							"\t\ttry {\n" +
							"\t\t} catch (Exception e) {\n" +
							"\t\t\treturn value;\n" +
							"\t\t}\n" +
							"\t}\n" +
							"});";
					System.out.println(str);
				}
			}
		}
	}
	/*
	javaSQLTimestamp_javaSQLTimestampstamp(java.sql.Timestamp.class, java.sql.Time.class){
	@Override
	public Object exe(Object value, Object def) throws ConvertException {
		Date date = DateUtil.parse(value);
		return DateUtil.sqlTime(date);
	}
},*/
	public static void tdtags() throws Exception{
		DataSourceTransactionManager s;
		String sql = "DESCRIBE s_table_user";
		List<Map<String,Object>> list = jdbc.queryForList(sql);
		for(Map<String,Object> map:list){
			DataRow row = new DataRow(map);
			System.out.println(row);
		}
	}
	public static void td() throws Exception{
		String sql = null;


		System.out.println("----------  getMetaData().getTables ------------------");
		ResultSet rs = con.getMetaData().getTables(con.getCatalog(), con.getSchema(), null, "STABLE".split(","));
		ResultSetMetaData md = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i < md.getColumnCount(); i++) {
				System.out.println(md.getColumnName(i) + "=" + rs.getObject(i));

			}
			System.out.println("----------------------------");
		}

		System.out.println("----------  getMetaData().getColumns ------------------");
		rs = con.getMetaData().getColumns(con.getCatalog(),con.getSchema(), "s_table_user",null);
		md = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i < md.getColumnCount(); i++) {
				System.out.println(md.getColumnName(i) + "=" + rs.getObject(i));

			}
			System.out.println("----------------------------");
		}
	}
	public static void tables(String table) throws Exception{
		DataSource ds = null;
		Connection con = null;
		try {
			ds = jdbc.getDataSource();
			con = DataSourceUtils.getConnection(ds);

			ResultSet rs = con.getMetaData().getTables(con.getCatalog(), con.getSchema(), table, "TABLE".split(","));
			ResultSetMetaData md = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i < md.getColumnCount(); i++) {
					System.out.println(md.getColumnName(i) + "=" + rs.getObject(i));
				}
			}
		}finally {
			if(!DataSourceUtils.isConnectionTransactional(con, ds)){
				DataSourceUtils.releaseConnection(con, ds);
			}
		}
	}
	public static void columns(String table){
		try {
			Connection con = DataSourceUtils.getConnection(jdbc.getDataSource());
			DatabaseMetaData metaData = con.getMetaData();

			ResultSet rs = metaData.getColumns(con.getCatalog(), con.getSchema(), table, null);
			ResultSetMetaData rsm = rs.getMetaData();
			while (rs.next()) {
				System.out.println("========column==========");
				for (int i = 1; i < rsm.getColumnCount(); i++) {
					System.out.println(rsm.getColumnName(i) + "=" + rs.getObject(i));
				}
				System.out.println("========end column==========");
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
			System.out.println("========end PK==========");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static List<Table> tables(String catalog, String schema, String name, String types){
		List<Table> tables = new ArrayList<>();
		DataSource ds = null;
		Connection con = null;
		try{
			ds = jdbc.getDataSource();
			con = DataSourceUtils.getConnection(ds);
			if(null == catalog){
				catalog = con.getCatalog();
			}
			if(null == schema){
				schema = con.getSchema();
			}
			String[] tps = null;
			if(null != types){
				tps = types.toUpperCase().trim().split(",");
			}
			ResultSet rs = con.getMetaData().getTables(catalog, schema, name, tps );
			ResultSetMetaData rsmd = rs.getMetaData();
			List<String> keys = new ArrayList<>();
			for(int i=1; i<rsmd.getColumnCount(); i++){
				keys.add(rsmd.getColumnName(i).toUpperCase());
			}
			while(rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				if(BasicUtil.isEmpty(tableName)){
					continue;
				}
				Table table = new Table();
				table.setCatalog(BasicUtil.evl(rs.getString("TABLE_CAT"), catalog));
				table.setSchema(BasicUtil.evl(rs.getString("TABLE_SCHEM"), schema));
				table.setName(tableName);
				table.setType(rs.getString("TABLE_TYPE"));
				table.setComment(rs.getString("REMARKS"));
				table.setTypeCat(rs.getString("TYPE_CAT"));
				table.setTypeName(rs.getString("TYPE_NAME"));
				table.setSelfReferencingColumn(rs.getString("SELF_REFERENCING_COL_NAME"));
				table.setRefGeneration(rs.getString("REF_GENERATION"));
				System.out.println(table.getName());
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {

			if(!DataSourceUtils.isConnectionTransactional(con, ds)){
				DataSourceUtils.releaseConnection(con, ds);
			}
		}
		return tables;
	}


}

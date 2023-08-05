package org.anyline.simple.help;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.ClassUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.LogUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

		json();
		//td();
		//tdtags();
		//convert();
		//type();
		//dm8();

		//data(); //准备测试数据
		/*for(int i=0; i<100; i++){
			memory();
		}*/
	}
	public static void json(){
		ConfigTable.IS_AUTO_CHECK_METADATA = true;
		DataSet set = service.querys("HR_EMPLOYEE(ID,TITLES)",0,0);
		service.insert("HR_EMPLOYEE1", set);

	}
	public static void memory(){
		long total = service.count("A_TEST_Q");
		long page = (total -1)/ 2000;
		for(int p=0; p<page; p++){
			service.maps("A_TEST_Q", new DefaultPageNavi(p, 2000).setLazy(100000000));
			System.out.println("可用内存:"+LogUtil.format(Runtime.getRuntime().freeMemory()/1024/1024, 31));
		}
	}
	public static void data() throws Exception{
		Table table = service.metadata().table("A_TEST_Q");
		if(null == table){
			table = new Table("A_TEST_Q");
			table.addColumn("ID","INT").setAutoIncrement(true).setPrimaryKey(true);
			for(int i=0; i<100;i++){
				table.addColumn("C"+i, "varchar(100)");
			}
			service.ddl().save(table);
		}

		for(int i=0; i<1000; i++){
			DataSet set = new DataSet();
			for(int j=0; j<100; j++){
				DataRow row = new DataRow();
				for(int c=0; c<10; c++){
					row.put("C"+c, "VALUE"+c);
				}
				set.add(row);
			}
			service.insert("A_TEST_Q", set);
		}
	}
	public static void dm8(){
		//主键
		String sql = "SELECT A.*, '---------', B.* FROM ALL_CONSTRAINTS A, ALL_CONS_COLUMNS B WHERE   B.OWNER =A.OWNER AND A.TABLE_NAME     =B.TABLE_NAME";
		//索引
		sql = "SELECT M.*, F.COMMENTS AS COLUMN_COMMENT FROM USER_TAB_COLUMNS    M \n" +
				"LEFT JOIN USER_COL_COMMENTS F ON M.TABLE_NAME = F.TABLE_NAME AND M.COLUMN_NAME = F.COLUMN_NAME\n" ;
		//索引
		sql ="SELECT *  from DBA_IND_COLUMNS";
		DataSet set = service.querys(sql, 0, 10);
		for(DataRow row:set){
			System.out.println("\n-------------------------------------------");
			for(String key:row.keySet()){
				Object value = row.get(key);
				String type = "";
				if(null != value){
					type = value.getClass().getName();
				}
				System.out.println(BasicUtil.fillLChar(key," ", 30)+"\t=\t"+value+"\t["+type+"]");
			}
			System.out.println(" ");
		}
		set = new DataSet();
		for(int i=0;i<10;i ++){
			DataRow row = new DataRow();
			row.put("CODE","C"+i);
			row.put("NAME","N"+i);
			set.add(row);
		}
		service.insert("HR_EMPLOYEE", set);
	}

	public static void type(){
		List<Map<String,Object>> maps = service.maps("chk_column");
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

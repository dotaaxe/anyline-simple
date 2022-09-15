package org.anyline.simple.help;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class HelpApplication {

	private static AnylineService service;
	private static JdbcTemplate jdbc;
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(HelpApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);
		jdbc = context.getBean(JdbcTemplate.class);
		DataSourceHolder.setDataSource("td");
		td();
	}
	public static void td() throws Exception{
		String sql = "SHOW STABLES";

		jdbc.execute("CREATE STABLE IF NOT EXISTS stable_abc(ID TIMESTAMP, CODE INT ) TAGS(TAG1 INT, TAG2 INT)");
		List<Map<String,Object>> list = jdbc.queryForList(sql);
		DataSet set = new DataSet();
		for(Map<String,Object> map:list){
			DataRow row = new DataRow(map);
			set.add(row);
		}
		System.out.println(set);

		DataSource ds = null;
		Connection con = null;
		ds = jdbc.getDataSource();
		con = DataSourceUtils.getConnection(ds);

		ResultSet rs = con.getMetaData().getTables(con.getCatalog(), con.getSchema(), null, "STABLE".split(","));
		ResultSetMetaData md = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i < md.getColumnCount(); i++) {
				System.out.println(md.getColumnName(i) + "=" + rs.getObject(i));

			}
			System.out.println("----------------------------");
		}
		sql = "INSERT INTO s_table_user_2(ID,CODE,AGE) VALUES (?,?,?)";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setObject(1, System.currentTimeMillis());
		ps.setObject(2, "c1");
		ps.setObject(3,10);
		ps.execute();
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
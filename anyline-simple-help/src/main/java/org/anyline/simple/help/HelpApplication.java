package org.anyline.simple.help;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

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
		//DataSourceHolder.setDataSource("pg");
		tables();
		tables(null, null, "A_TEST_AAAA","TABLE" );
	}
	public static void tables() throws Exception{
		DataSource ds = null;
		Connection con = null;
		try {
			ds = jdbc.getDataSource();
			con = DataSourceUtils.getConnection(ds);

			ResultSet rs = con.getMetaData().getTables(con.getCatalog(), con.getSchema(), "A_TEST_AAAA", "TABLE".split(","));
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

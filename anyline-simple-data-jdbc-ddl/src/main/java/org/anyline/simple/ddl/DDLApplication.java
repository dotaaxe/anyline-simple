package org.anyline.simple.ddl;

import org.anyline.data.entity.Column;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.data.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.LinkedHashMap;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
@Import(DynamicDataSourceRegister.class)
public class DDLApplication {

	private static AnylineService service;
	private static Logger log = LoggerFactory.getLogger(DDLApplication.class);
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DDLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);

		//check(null, "MySQL");
		check("pg", "PostgreSQL");
		//check("ms", "SQL Server");
		//check("oracle", "Oracle 11G");
		//check("db2", "DB2");

	}
	public static void check(String ds, String title) throws Exception{
		System.out.println("\n=============================== START " + title + "=========================================\n");
		if(null != ds) {
			DataSourceHolder.setDataSource(ds);
		}
		table();
		column();
		index();
		exception();
		System.out.println("\n=============================== END " + title + "=========================================\n");
	}
	public static void table() throws Exception{
		System.out.println("\n-------------------------------- start table  --------------------------------------------\n");
		clear();

		LinkedHashMap<String,Table> tables = service.metadata().tables();
		log.warn("检索表数量:"+tables.size());
		for(Table table:tables.values()){
			log.warn("表:"+table.getName());
		}


		Table table = service.metadata().table("a_test");
		if(null != table) {
			log.warn("查询表结构:" + table.getName());
			LinkedHashMap<String, Column> columns = table.getColumns();
			for (Column column : columns.values()) {
				log.warn("列:" + column.toString());
			}

		}
		if(null != table){
			log.warn("删除表:"+table.getName());
			service.ddl().drop(table);
		}
		log.warn("创建表");
		table = new Table();
		table.setName("A_TEST");
		table.setComment("表备注");
		table.addColumn("ID", "int").setAutoIncrement(true).setComment("主键说明");

		table.addColumn("NAME","varchar(50)").setComment("名称");
		table.addColumn("A_CHAR","varchar(50)");
		service.ddl().save(table);
		table.getColumn("NAME").setComment("新备注名称");
		service.ddl().save(table);
		System.out.println("\n-------------------------------- end table  ----------------------------------------------\n");
	}
	public static void column() throws Exception{
		System.out.println("\n-------------------------------- start column  -------------------------------------------\n");
		clear();
		Column column = new Column();
		column.setTable("A_TEST");
		column.setName("A_CHAR");
		column.setTypeName("int");	//没有数据的情况下修改数据类型
		column.setPrecision(0);
		column.setScale(0);
		column.setDefaultValue("1");
		//添加新列
		log.warn("添加列");
		service.ddl().save(column);

		//修改列
		//没有值的属性 默认同步原有的数据库结构
		//如果不修改列名，直接修改column属性
		column.setTypeName("varchar(10)");
		column.setComment("测试备注1"+ DateUtil.format());
		column.setDefaultValue("2");
		log.warn("修改列");
		service.ddl().save(column);

		//修改列名2种方式
		//注意:修改列名时，不要直接设置name属性,修改数据类型时，不要直接设置typeName属性,因为需要原属性
		// 1.可以设置newName属性(注意setNewName返回的是update)
		column.setNewName("B_TEST").setTypeName("varchar(20)");
		log.warn("修改列名");
		service.ddl().save(column);

		// 2.可以在update基础上修改
		//如果设置了update, 后续所有更新应该在update上执行
		column.update().setName("C_TEST").setPosition(0).setTypeName("VARCHAR(20)");
		log.warn("修改列名");
		service.ddl().save(column);

		log.warn("删除列");
		service.ddl().drop(column);
		System.out.println("\n-------------------------------- end column  ---------------------------------------------\n");
	}
	public static void exception() throws Exception{
		System.out.println("\n-------------------------------- start exception  ----------------------------------------\n");
		clear();
		//ConfigTable.AFTER_ALTER_COLUMN_EXCEPTION_ACTION
		// 0:中断执行
		// 1:直接修正
		// n:行数<n时执行修正  >n时触发另一个监听(默认返回false)

		Column column = new Column();
		column.setTable("A_TEST");
		column.setName("A_CHAR");
		column.setTypeName("varchar(50)");
		//添加 新列
		service.ddl().save(column);

		//表中有数据的情况下
		DataRow row = new DataRow();
		//自增列有可能引起异常
		try {
			row.put("ID", BasicUtil.getRandomNumber(0, 100000));
			row.put("A_CHAR", "123A");
			row.setIsNew(true);
			service.save("A_TEST", row);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		try {
			row = new DataRow();
			row.put("A_CHAR", "123A");
			service.save("A_TEST", row);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		log.warn("表中有数据的情况下修改列数据类型");
		column = new Column();
		column.setTable("A_TEST");
		column.setName("A_CHAR");
		//这一列原来是String类型 现在改成int类型
		//如果值不能成功实现殷式转换(如123A转换成int)会触发一次默认的DDListener.afterAlterException
		//如果afterAlterException返回true，会再执行一次alter column如果还失败就会抛出异常
		//如果不用默认listener可以column.setListener
		column.setTypeName("int");
		column.setPrecision(0);
		service.ddl().save(column);
		System.out.println("\n-------------------------------- end  exception  -----------------------------------------\n");
	}
	public static void index() throws Exception{
		System.out.println("\n-------------------------------- start index  --------------------------------------------\n");
		//添加索引
		Index index = new Index();
		index.setName("index_code_name");
		index.setTableName("crm_user");
		index.setUnique(true);
		index.addColumn(new Column("CODE"));
		index.addColumn(new Column("NAME").setOrder("DESC"));//倒序
		try {
			service.ddl().add(index);
		}catch (Exception e){
			e.printStackTrace();
		}

		LinkedHashMap<String, Index> indexs = service.metadata().indexs("crm_user");
		for(Index item:indexs.values()){
			System.out.println("所引:"+item.getName());
			System.out.println("是否主键:"+item.isPrimary());
			System.out.println("是否物理所引:"+item.isCluster());
			System.out.println("是否唯一:"+item.isUnique());
			LinkedHashMap<String, Column> columns = item.getColumns();
			for(Column column:columns.values()){
				System.out.println("包含列:"+column.getName());
			}
			//如果删除自增长主键 有可能会抛出异常： there can be only one auto column and it must be defined as a key

			System.out.println("删除索引:" + item.getName());
			try {
				service.ddl().drop(item);
			}catch (Exception e){
				e.printStackTrace();
			}


		}

		System.out.println("\n-------------------------------- end index  ----------------------------------------------\n");
	}

	public static void clear(){
		System.out.println("\n=============================== START clear =========================================\n");
		System.out.println("\n=============================== START clear =========================================\n");
	}
}

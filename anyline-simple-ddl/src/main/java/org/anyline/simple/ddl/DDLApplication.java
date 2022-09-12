package org.anyline.simple.ddl;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class DDLApplication {

	private static AnylineService service;
	public static void main(String[] args) throws Exception{

		SpringApplication application = new SpringApplication(DDLApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		service = context.getBean(AnylineService.class);
		table();
		//column();
		//执行异常监听
		//exception();
		System.out.println("\n============================= PG ==========================================\n");
		service.tables();
		DataSourceHolder.setDataSource("pg");
		table();
	}
	public static void table() throws Exception{
		Table table = new Table();
		table.setName("A_TEST_AAAA");
		table.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true).setComment("commnet");
		table.addColumn("NAME","varchar(50)");
		table.addColumn("NAMESS","varchar(50)");
		service.ddl().save(table);
	}
	public static void exception() throws Exception{
		//ConfigTable.AFTER_ALTER_COLUMN_EXCEPTION_ACTION
		// 0:中断执行
		// 1:直接修正
		// n:行数<n时执行修正  >n时触发另一个监听(默认返回false)


		//表中有数据的情况下
		DataRow row = new DataRow();
		row.put("A_CHAR","123A");
		service.insert("test", row);

		Column column = new Column();
		column.setTable("test");
		column.setName("A_CHAR");
		//这一列原来是String类型 现在改成int类型
		//如果值不能成功实现殷式转换(如123A转换成int)会触发一次默认的DDListener.afterAlterException
		//如果afterAlterException返回true，会再执行一次alter column如果还失败就会抛出异常
		//如果不用默认listener可以column.setListener
		column.setTypeName("int");
		service.ddl().save(column);
	}
	public static void column() throws Exception{

		Column column = new Column();
		column.setTable("test");
		column.setName("A_TEST");
		column.setTypeName("int");
		column.setDefaultValue("1");
		//添加 新列
		service.ddl().save(column);

		//修改列
		//没有值的属性 默认同步原有的数据库结构
		//如果不修改列名，直接修改column属性
		column.setTypeName("varchar(10)");
		column.setComment("测试备注1"+ DateUtil.format());
		column.setDefaultValue("2");
		service.ddl().save(column);

		//修改列名2种方式
		//注意:修改列名时，不要直接设置name属性,修改数据类型时，不要直接设置typeName属性,因为需要原属性
		// 1.可以设置newName属性
		column.setNewName("B_TEST");
		service.ddl().save(column);
		// 2.可以在update基础上修改
		//如果设置了update, 后续所有更新应该在update上执行
		column.update().setName("C_TEST").setPosition(0);
		service.ddl().save(column);

		service.ddl().drop(column);


	}
}

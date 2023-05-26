package org.anyline.simple.validate;

import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.data.entity.Column;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.PrimaryKey;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.Map;

@SpringBootTest
public class ValidateTest {

    private static Logger log = LoggerFactory.getLogger(ValidateTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void check() throws Exception {
/*
        check(null, "MySQL");
        check("cms", "MySQL");
        check("pg", "PostgreSQL");
        check("ms", "SQL Server");
        check("ms2000", "SQL Server 2000");
        check("oracle", "Oracle 11G");
        check("dm8", "达梦8");
        check("db2", "DB2");
        check("kingbase8", "人大金仓8(Oracle兼容)");
        check("gbase", "南大通用");
        check("opengauss", "高斯");
        check("oscar", "神州通用");
        check("informix", "Informix");
		*/
		/*for(String datasource:DataSourceHolder.list()){
			check(datasource,datasource);
		}*/
        //columnType();
       //
        //check("informix", "Informix");
        check(null, "MySQL");

    }
    public void check(String ds, String type) throws Exception {
        System.out.println("======================== start validate " + type + " ================================");
        if(null == ds){
            DataSourceHolder.setDefaultDataSource();
        }else {
            DataSourceHolder.setDataSource(ds);
        }
        //ddl();
        dml();
        meta();
        System.out.println("======================== finish validate " + type + " ================================");
    }
    public void meta(){
        Map<String,Table> tables = service.metadata().tables();
        for(String name:tables.keySet()){
            Table table = tables.get(name);
            System.out.println("table:"+table.getName());
        }
        Table table = service.metadata().table("HR_EMPLOYEE");
        Map<String, Column> columns = table.getColumns();
        for(String name:columns.keySet()){
            Column column = columns.get(name);
            System.out.println("column:"+column.getName() + "\ntype:" + column.getTypeName() + " length:" + column.getPrecision() + " scale:"+column.getScale());
        }
        PrimaryKey pk = table.getPrimaryKey();
        if(null != pk){
            System.out.println(table.getName()+"主键:"+pk.getName());
            Map<String,Column> cols = pk.getColumns();
            for(String name:cols.keySet()){
                Column column = cols.get(name);
                System.out.println("column:"+column.getName());
            }
        }
    }
    public void dml(){
        DataSet set = new DataSet();
        set = service.querys("HR_EMPLOYEE");
        for(int i=0; i<3; i++){
            DataRow row = new DataRow();
            row.put("CODE","C"+i);
            row.put("NAME", "N"+i);
            set.add(row);
          //  service.insert("HR_EMPLOYEE", row);
        }
        service.execute("DELETE FROM HR_EMPLOYEE");
        service.insert("HR_EMPLOYEE", set);
        set = service.querys("HR_EMPLOYEE");
        System.out.println(set);
    }

    public void ddl() throws Exception{
        //pg,ms,oracle,db2,ms2000,cms,dm8
        Table table = service.metadata().table("HR_EMPLOYEE");
        if(null != table){
            log.warn("删除表:"+table.getName());
            service.ddl().drop(table);
        }
        table = service.metadata().table("HR_EMPLOYEE");

        //删除表
        Assertions.assertNull(table);


        //创建表
        createTable();

        table = service.metadata().table("HR_EMPLOYEE");
        if(null == table){
            table = service.metadata().table("hr_employee");
        }
        Assertions.assertNotNull(table);

        //表备注
        Assertions.assertEquals(table.getComment(), "职员基础信息");

        //列备注
        Assertions.assertEquals(table.getColumn("NAME").getComment(), "姓名");

        //默认值 日期类型根据不同的数据库
        Object def = table.getColumn("DATA_STATUS").getDefaultValue();
        log.warn("默认值:{}", def);
        Assertions.assertNotNull(def);

        //主键
        PrimaryKey pk = table.getPrimaryKey();
        Assertions.assertNotNull(pk);
        Assertions.assertEquals(pk.getColumns().size(), 1);
        Assertions.assertEquals(pk.getColumn("ID").getComment(), "主键");

        //索引(包含主键)
        Map<String, Index> indexes = table.getIndexs();
        Assertions.assertEquals(indexes.size(), 2);

        //修改表结构
        table.setComment("新职员基础信息");
        table.addColumn("ALIAS", "varchar(10)").setComment("别名").setDefaultValue("def alias");
        table.addColumn("SLICE_COL", "varchar(10)").setComment("片段测试");
        table.getColumn("NAME").delete();
        table.getColumn("CODE").setType("varchar(300)").setComment("新备注");
        service.ddl().save(table);
        table = service.metadata().table("HR_EMPLOYEE");
        Assertions.assertEquals(table.getComment(), "新职员基础信息");
        Assertions.assertEquals(table.getColumn("ALIAS").getComment(), "别名");

        //修改列备注
        Column col = new Column("ALIAS");
        col.setTableName("HR_EMPLOYEE");
        col.setComment("新别名");
        service.ddl().save(col);

        table = service.metadata().table("HR_EMPLOYEE");
        Assertions.assertEquals(table.getColumn("ALIAS").getComment(), "新别名");

        //修改列数据类型
        col = new Column("ALIAS");
        col.setTableName("HR_EMPLOYEE");
        col.setType("varchar(20)");
        service.ddl().save(col);
        table = service.metadata().table("HR_EMPLOYEE");

        Assertions.assertEquals(table.getColumn("ALIAS").getPrecision(), 20);


    }
    private void createTable() throws Exception{
        Table table = new org.anyline.data.entity.Table("HR_EMPLOYEE").setComment("职员基础信息");
        //注意以下数据类型
        table.addColumn("ID"            , "BIGINT"       ).setComment("主键").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ).setComment("姓名")     ; // String          : nm
        table.addColumn("CODE"          , "varchar(50)"  ).setComment("工号")     ; // String          : workCode
        table.addColumn("BIRTHDAY"      , "DATE"         ).setComment("出生日期")  ; // Date            : birthday
        table.addColumn("JOIN_YMD"      , "varchar(10)"  ).setComment("入职日期")  ; // String          : joinYmd
        table.addColumn("SALARY"        , "float(10,2)"  ).setComment("工资")     ; // float           : salary
        table.addColumn("REMARK"        , "blob"         ).setComment("备注")     ; // byte[]          : remark
        table.addColumn("DATA_STATUS"   , "int"          ).setComment("状态").setDefaultValue(1);
        //注意SQL Server中一个表只能有一列TIMESTAMP， 不能设置默认值
        table.addColumn("CREATE_TIME"   , "DATETIME"    ).setComment("创建时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("UPDATE_TIME"   , "TIMESTAMP"   ).setComment("更新时间");
        service.ddl().create(table);
        Index index = new Index();
        index.addColumn("NAME");
        index.addColumn("CODE");
        index.setTable(table);
        service.ddl().add(index);
    }
}

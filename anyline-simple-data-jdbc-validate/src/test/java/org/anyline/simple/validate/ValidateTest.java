package org.anyline.simple.validate;

import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.data.entity.Column;
import org.anyline.data.entity.Index;
import org.anyline.data.entity.PrimaryKey;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class ValidateTest {

    private static Logger log = LoggerFactory.getLogger(ValidateTest.class);
    @Autowired
    @Qualifier("anyline.service")
    private AnylineService service;

    @Test
    public void ddl() throws Exception{
        DataSourceHolder.setDataSource("oracle");
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
        table.addColumn("ALIAS", "varchar(10)").setComment("别名");
        service.ddl().save(table);
        table = service.metadata().table("HR_EMPLOYEE");
        Assertions.assertEquals(table.getComment(), "新职员基础信息");
        Assertions.assertEquals(table.getColumn("ALIAS").getComment(), "别名");

        Column col = new Column("ALIAS");
        col.setTableName("HR_EMPLOYEE");
        col.setComment("新别名");
        service.ddl().save(col);
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
        //注意SQL Server中一个表只能有一列TIMESTAMP
        table.addColumn("CREATE_TIME"   , "TIMESTAMP"    ).setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        service.ddl().create(table);
        Index index = new Index();
        index.addColumn("NAME");
        index.addColumn("CODE");
        index.setTable(table);
        service.ddl().add(index);
    }
}
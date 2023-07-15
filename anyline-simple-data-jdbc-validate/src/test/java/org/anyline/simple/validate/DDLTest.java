package org.anyline.simple.validate;

import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DDLTest {

    @Autowired
    @Qualifier("anyline.service")
    private AnylineService service;

    private static  String tableName = "a_test";


    @Test
    public void test() throws Exception {
        addTableTest();
        alterTableTest();

    }


    public void addTableTest() throws Exception {
        Table table = service.metadata().table(tableName);
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table();
        table.setName(tableName);
        table.setComment("单元测试表");

        // 构建主键列
        Column idColumn = new Column();
        idColumn.setName("id");
        idColumn.setType("bigint(20)");
        idColumn.setNullable(false);
        idColumn.setPrimaryKey(true);
        idColumn.setComment("主键");
        idColumn.setDefaultValue(0);

        // 构建name列，用于测试修改
        Column nameColumn = new Column();
        nameColumn.setName("name");
        nameColumn.setType("varchar");
        nameColumn.setPrecision(20);
        nameColumn.setNullable(true);
        nameColumn.setPrimaryKey(false);
        nameColumn.setComment("姓名");
        nameColumn.setDefaultValue(null);

        // 构建age列，用于测试删除
        Column ageColumn = new Column();
        ageColumn.setName("age");
        ageColumn.setType("int(3)");
        ageColumn.setNullable(true);
        ageColumn.setPrimaryKey(false);
        ageColumn.setComment("年龄");
        ageColumn.setDefaultValue(null);

        // 构建run_env列
        Column runEnvColumn = new Column();
        runEnvColumn.setName("run_env");
        runEnvColumn.setType("varchar(10)");
        runEnvColumn.setNullable(false);
        runEnvColumn.setPrimaryKey(false);
        runEnvColumn.setComment("运行环境");
        runEnvColumn.setDefaultValue(null);

        table.addColumn(idColumn);
        table.addColumn(nameColumn);
        table.addColumn(ageColumn);
        table.addColumn(runEnvColumn);
        service.ddl().create(table);
    }

    public void alterTableTest() throws Exception {

        Table table = service.metadata().table(tableName);
        // 修改表备注
        table.setComment("单元测试表修改后");

        // 增加phone列
        Column phoneColumn = new Column();
        phoneColumn
                .setName("phone")
                .setType("varchar(10)")
                .setNullable(false)
                .setPrimaryKey(false)
                .setComment("电话")
                .setDefaultValue(null);

        // 修改name列的长度和备注
        Column nameColumn = new Column();
        nameColumn
                .setName("name")
                .setType("varchar")
                .setPrecision(30)
                .setComment("姓名备注修改后");

        // 删除age列
        Column ageColumn = new Column();
        ageColumn.setName("age");
        ageColumn.setDelete(true);

        table.addColumn(phoneColumn);
        table.addColumn(nameColumn);
        table.addColumn(ageColumn);

        service.ddl().alter(table);
    }

}

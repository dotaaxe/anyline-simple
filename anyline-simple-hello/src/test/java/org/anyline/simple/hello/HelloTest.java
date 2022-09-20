package org.anyline.simple.hello;

import org.anyline.entity.DataSet;
import org.anyline.jdbc.entity.Column;
import org.anyline.jdbc.entity.Table;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HelloTest {
    private Logger log = LoggerFactory.getLogger(HelloTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void init() throws Exception{
        //先创建个表
        Table table = service.metadata().table("crm_user");
        if(null != table){

            service.ddl().drop(table);
        }
        Column column = new Column("ID").setAutoIncrement(true).setType("int");
        table.addColumn(column);
        table.addColumn("NAME","varchar(10)");


        DataSet set = service.querys("al_hello");
        System.out.println(set);
    }
}

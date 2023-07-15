package org.anyline.simple.clear;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Column;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClearTest {
    private Logger log = LoggerFactory.getLogger(ClearTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void init() throws Exception{

        //先创建个表
        Table table = service.metadata().table("crm_user");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new Table("crm_user");
        Column column = new Column("ID").setAutoIncrement(true).setType("int").setPrimaryKey(true);
        table.addColumn(column);
        table.addColumn("CODE","varchar(10)");
        table.addColumn("NAME","varchar(10)");
        service.ddl().create(table);

        //插入数据
        DataRow row = new DataRow();
        row.put("CODE","101");
        row.put("NAME","ZH");
        service.insert("crm_user", row);

        //查询数据
        DataSet set = service.querys("crm_user");
        System.out.println(set);

        row = service.query("CRM_USER", "ID:1");
        row.put("CODE","102");
        //更新数据
        service.update(row);
    }
}

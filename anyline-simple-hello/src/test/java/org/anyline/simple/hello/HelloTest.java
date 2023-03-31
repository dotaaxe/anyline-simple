package org.anyline.simple.hello;

import org.anyline.data.entity.Column;
import org.anyline.data.entity.Table;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
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
        table = new Table("crm_user");
        Column column = new Column("ID").setAutoIncrement(true).setType("int").setPrimaryKey(true);
        table.addColumn(column);
        table.addColumn("CODE","varchar(10)");
        table.addColumn("NAME","varchar(10)");
        service.ddl().create(table);

        DataRow test = new DataRow();
        test.put("NAME",  "1,2,3".split(","));
        service.save("crm_user", test);

        //插入数据
        DataRow row = new DataRow();
        row.put("CODE","101");
        row.put("NAME","ZH");
        service.insert("crm_user", row);

        //查询数据
        DataSet set = service.querys("crm_user", "ID>1");
        System.out.println(set);
        set = service.querys("crm_user", service.condition().and("ID", "1,2,3".split(",")));
        System.out.println(set);
        //合计行数 与查询的参数一样只是返回值不一样
        int qty = service.count("crm_user", "id>1");
        System.out.println(qty);
        qty = service.count("crm_user(distinct id)", "id>1");
        System.out.println(qty);

        row = service.query("CRM_USER", "ID:1");
        row.put("CODE","102");
        //更新数据
        service.update(row);
    }

    /**
     * XML中自定义SQL
     */
    @Test
    public void xml(){
        DataSet users = service.querys("crm.user:USER_LIST");
        System.out.println(users);
    }
}

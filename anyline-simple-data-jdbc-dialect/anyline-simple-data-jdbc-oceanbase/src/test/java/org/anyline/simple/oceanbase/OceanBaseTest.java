package org.anyline.simple.oceanbase;

import org.anyline.entity.data.Table;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OceanBaseTest {
    private Logger log = LoggerFactory.getLogger(OceanBaseTest.class);
    @Autowired
    private AnylineService service          ;
    @Test
    public void init(){
        Table table = service.metadata().table("crm_user");
        if(null == table){
            table = new Table();
            table.setName("crm_user");
            table.setComment("表备注");
            table.addColumn("ID", "int").setPrimaryKey(true).setAutoIncrement(true).setComment("主键说明");

            table.addColumn("NAME","varchar(50)").setComment("名称");
            table.addColumn("CODE","varchar(50)");
            try {
                service.ddl().save(table);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(int i=0; i<10; i++){
            DataRow row = new DataRow();
            row.put("CODE", "code"+i);
            row.put("NAME", BasicUtil.getRandomString(10));
            service.insert("crm_user", row);
        }
        DataSet set = service.querys("crm_user");
        log.warn(set.toJSON());
    }
}

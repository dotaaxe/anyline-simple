package org.anyline.simple.interceptor;

import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InterceptorTest {

    private static Logger log = LoggerFactory.getLogger(InterceptorTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void query(){
        init();
        service.querys("CRM_USER");
    }
    private void init(){
        try {
            Table tab = service.metadata().table("CRM_USER");
            if (null != tab) {
                service.ddl().drop(tab);
            }
            tab = new Table("CRM_USER");
            tab.addColumn("ID", "INT").setAutoIncrement(true).setPrimaryKey(true);
            tab.addColumn("CODE", "INT");
            tab.addColumn("NAME", "VARCHAR(10)");
            tab.addColumn("REMARK", "TEXT");
            tab.addColumn("CREATE_TIME", "datetime");
            service.ddl().save(tab);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

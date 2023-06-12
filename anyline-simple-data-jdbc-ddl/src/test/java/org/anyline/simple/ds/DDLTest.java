package org.anyline.simple.ds;

import com.zaxxer.hikari.HikariDataSource;
import org.anyline.entity.data.Table;
import org.anyline.data.jdbc.ds.DataSourceHolder;
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


    @Test
    public void test() throws Exception{
        try {
            String url = "jdbc:mysql://localhost:13306/simple_crm?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
            DataSourceHolder.reg("sso", "com.zaxxer.hikari.HikariDataSource", "com.mysql.cj.jdbc.Driver", url, "root", "root");
        }catch (Exception e){
            e.printStackTrace();
        }
        DataSourceHolder.setDataSource("sso");
        HikariDataSource ds = (HikariDataSource) DataSourceHolder.getDataSource("sso");
        for(int i=0;i <100; i++){
            Table table = service.metadata().table(false, "soa_indicators_db");
            System.out.println("active:"+ds.getHikariPoolMXBean().getActiveConnections());
        }
    }

}

package org.anyline.simple.all;

import com.alibaba.druid.pool.DruidDataSource;
import org.anyline.metadata.Column;
import org.anyline.metadata.PrimaryKey;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@SpringBootTest(classes = BootApplication.class)
public class AllTest {
    @Autowired
    private AnylineService service          ;

    /**
     * 创建修改表
     * @throws Exception
     */
    @Test
    public void createTable() throws Exception{
        Table table = new Table() ;
        table.setComment("test");
        table.setName("qc_ti_test1");
        Column column = new Column();
        column.setType("varchar(128)");
        column.setName("CODE") ;
        column.setComment("TEST");
        table.setComment("CMT");;
        Column pcol = table.addColumn(  "PKID",  "int");
        PrimaryKey pk = new PrimaryKey() ;
        pk.addColumn(pcol);
        table .setPrimaryKey(pk) ;
        service.ddl().save(table);
    }

    /**
     * 查询所有表
     * @throws Exception
     */
    @Test
    public void tables() throws Exception{
        for(int i=0;i <10; i++) {
            LinkedHashMap<String, Table> tables = service.metadata().tables();
            for (String table : tables.keySet()) {
                System.out.println(service.metadata().table(table, true));
            }
        }
    }
    /**
     * 临时数据源
     * @throws Exception
     */
    @Test
    public void tmp() throws Exception{
        for(int i=0; i<10; i++) {
            String url = "jdbc:mysql://localhost:13306/simple?useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
            DruidDataSource ds = new DruidDataSource();
            ds.setUrl(url);
            ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
            ds.setUsername("root");
            ds.setPassword("root");
            ds.setConnectionErrorRetryAttempts(3);
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectTimeout(3000);
            ds.setMaxWait(30000);
            service = ServiceProxy.temporary(ds);

            LinkedHashMap<String, Table> tables = service.metadata().tables();
            for (String table : tables.keySet()) {
                System.out.println(service.metadata().table(table, true));
            }
        }
    }
}

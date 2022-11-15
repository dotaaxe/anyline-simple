package org.anyline.simple.sql;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SQLTest {

    @Autowired
    private AnylineService service;

    @Test
    public void init() throws Exception{
        //详细的查询条件构造方式 参考 anyline-simple-data-condition
        String sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        DataSet set = service.querys(sql, "PARAM_CODE:1");
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = 1

        //特别注意这以下情况 CODE:1与SQL中的变量匹配不成功时，SQL会追加一个条件 WHERE CODE = 1
        sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        set = service.querys(sql, "PARAM_CODE:1", "CODE:1");
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = 1 AND CODE = 1
    }
}

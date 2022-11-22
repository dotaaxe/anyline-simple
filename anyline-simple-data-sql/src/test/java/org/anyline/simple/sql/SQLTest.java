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


        //:PARAM_CODE 与 {PARAM_CODE} 效果一致但不能混用
        //会生成占位符
        String sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        DataSet set = service.querys(sql, "PARAM_CODE:100");
        sql = "SELECT * FROM CRM_USER WHERE CODE = {PARAM_CODE}";
        set = service.querys(sql, "PARAM_CODE:100");
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = ?



        //::PARAM_CODE 与 ${PARAM_CODE} 效果一致但不能混用
        //不生成占位符,而是在原sql上replace
        //在一些比较复杂的情况,简单占位符胜任不了时 会用到
        sql = "SELECT * FROM CRM_USER WHERE CODE = ::PARAM_CODE";
        set = service.querys(sql, "PARAM_CODE:100");
        sql = "SELECT * FROM CRM_USER WHERE CODE = ${PARAM_CODE}";
        set = service.querys(sql, "PARAM_CODE:100");
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = 1




        //特别注意这以下情况 ID:1与SQL中的变量匹配不成功时，SQL会追加一个条件  ID = 1
        sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        set = service.querys(sql, "PARAM_CODE:1", "ID:1");
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = ? AND ID = ?


        sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        set = service.querys(sql);

        //但是以个SQL 最好应该这样写
        //service.query("CRM_USER", "CODE:100");

    }
}

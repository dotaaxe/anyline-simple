package org.anyline.simple.sql;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataRow;
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


        //如果没有提供参数值 会生成 = NULL, 这种情况明显不符合预期
        sql = "SELECT * FROM CRM_USER WHERE CODE = :PARAM_CODE";
        set = service.querys(sql);
        //生成SQL  SELECT * FROM CRM_USER WHERE CODE = NULL

        sql = "UPDATE CRM_USER SET CODE = :CODE WHERE ID = :ID";
        service.execute(sql,"CODE:C001", "ID:1");
        //生成SQL  UPDATE  CRM_USER WHERE CODE = ? WHERE ID = ?


        sql = "UPDATE CRM_USER SET CODE = :CODE WHERE ID = :ID";
        service.execute(sql,"CODE:C001", "++ID:");
        //如果ID没有提供参数值，则整个SQL不执行

        String value = "100";
        /********************************************************************
         *
         *                        以上SQL 最好应该这样写
         *
         **************************************************************/
        service.query("CRM_USER", "CODE:"+value);
        //生成SQL SELECT * FROM CRM_USER WHERE CODE = ?

        //默认情况下如果value没有值则不会拼接相应的查询条件
        value = null;
        service.query("CRM_USER", "CODE:"+value);
        value =  "";
        service.query("CRM_USER", "CODE:"+value);
        //生成SQL SELECT * FROM CRM_USER

        //如果希望没有提供参数值是不执行SQL
        value = null;
        service.query("CRM_USER", "++CODE:"+value);
        value =  "";
        service.query("CRM_USER", "++CODE:"+value);
        //不会生成SQL 返回new DataSet();

        //如果希望没有提供参数值是依然拼接这个查询条件
        value = null;
        service.query("CRM_USER", "+CODE:"+value);
        value =  "";
        service.query("CRM_USER", "+CODE:"+value);
        //生成SQL SELECT * FROM CRM_USER WHERE CODE IS NULL


        DataRow row = new DataRow();
        row.put("NAME","中文名");
        row.put("CODE", "C001");
        row.setPrimaryKey("CODE");  //临时设置主键，执行update时根据设置的主键执行,否则按默认主键

        service.save("CRM_USER", row);

    }
}

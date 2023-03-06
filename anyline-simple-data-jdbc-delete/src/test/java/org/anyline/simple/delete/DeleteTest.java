package org.anyline.simple.delete;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DeleteTest {
    private Logger log = LoggerFactory.getLogger(DeleteTest.class);
    @Autowired
    private AnylineService service          ;
    @Test
    public void init(){
        /*
        * 删除会有以下几类情况
        * 1.先查出数据，再执行删除
        * 2.直接执行SQL
        * 3.根据约定参数删除
        *
        * */
        for(int i=0; i<10; i++){
            DataRow user = new DataRow();
            user.put("CODE", "C"+i);
            user.put("NAME", "NAME"+i);
            service.insert("CRM_USER", user);
        }
        //1.先查出数据，再执行删除
        //默认情况下根据主键删除
        DataRow row = service.query("CRM_USER");
        //SQL:DELETE FROM CRM_USER WHERE ID = ?
        service.delete(row);

        //遍历set逐行删除
        DataSet set = service.querys("CRM_USER");
        service.delete(set);

        //1.1 DataRow不一定从数据库是查出也可以临时构造
        //如果不是从数据库中查出，在删除时需要指定表名
        row = new DataRow();
        row.put("ID", "100");
        //SQL:DELETE FROM CRM_SUER WHERE ID = ?
        service.delete("CRM_USER", row);

        //可以修改临时主键,根据code,name条件删除,这样有可能删除多行
        row.put("CODE", "A1");
        row.put("NAME", "N1");
        row.setPrimaryKey("CODE", "NAME");
        //SQL:DELETE FROM CRM_USER WHERE CODE = ?(A1) AND NAME = ?(N1)
        service.delete("CRM_USER", row);

        //2.执行SQL
        service.execute("DELETE FROM CRM_USER WHERE ID = 1");

        //SQL:DELETE FROM CRM_USER WHERE ID = 1  AND NAME = ?(N1)
        service.execute("DELETE FROM CRM_USER WHERE ID = 1", "NAME:N1");

        //SQL:DELETE FROM CRM_USER WHERE ID = 2
        service.execute("DELETE FROM CRM_USER WHERE ID = ${ID}", "ID:2");

        //SQL:DELETE FROM CRM_USER WHERE ID = ?(3)
        service.execute("DELETE FROM CRM_USER WHERE ID = {ID}", "ID:3");

        //3.根据约定参数删除
        //根据 ID 删除多行 deletes(String table, String key, String ... values)
        try {
            //注意:为了避免整表删除,values必须提供否则会抛出异常
            //整表删除请调用service.execute("DELETE FROM HR_EMPLOYEE");
            service.deletes("HR_EMPLOYEE", "ID"); //这里少了values
        }catch (Exception e){
            e.printStackTrace();
        }
        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID = ?(100)
        service.deletes("HR_EMPLOYEE", "ID", "100");
        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID IN(?,?)
        service.deletes("HR_EMPLOYEE", "ID", "100","200");

        List<String> ids = new ArrayList<>();
        //注意:为了避免整表删除,ids必须提供否则会抛出异常
        //service.deletes("HR_EMPLOYEE", "ID", ids);
        ids.add("100");
        ids.add("200");
        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID IN(?,?)
        service.deletes("HR_EMPLOYEE", "ID", ids);

        //根据多列条件删除
        row = new DataRow();
        row.put("ID","1");
        row.put("NM", "ZH");
        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID = ?(1) AND NM = ?(ZH)
        service.delete("HR_EMPLOYEE", row, "ID", "NM");

        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID = ?(1) AND CODE = ?(20)
        service.delete("HR_EMPLOYEE","ID","1", "CODE:20");

        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID = ?('') AND CODE = ?(20)
        service.delete("HR_EMPLOYEE","ID","", "CODE:20");

        //SQL:DELETE FROM HR_EMPLOYEE WHERE ID = ?(1) AND CODE = ?('')
        service.delete("HR_EMPLOYEE","ID","1", "CODE:");
    }
}

package org.anyline.simple.special;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class ServiceTest {
    private Logger log = LoggerFactory.getLogger(ServiceTest.class);
    @Autowired
    private AnylineService service;


    @Test
    public void override(){
        //DataSet DataRow规则一样
        DataRow row = service.query("CRM_USER");

        //默认情况下 有主键值则update 没主键则insert
        service.save(row);

        //如果数据已存存则覆盖
        row.setOverride(true);
        service.save(row);

        //如果数据已存存则跳过
        row.setOverride(false);
        service.save(row);

        //复合主键规则一样
        row.setPrimaryKey("CODE","NAME");

        //默认情况下 有主键值则update 没主键则insert
        row.setOverride(null);
        service.save(row);

        //如果数据已存存则覆盖
        row.setOverride(true);
        service.save(row);

        //如果数据已存存则跳过
        row.setOverride(false);
        service.save(row);
    }
    @Test
    public void json(){
        //JS列设置成json类型
        //ConfigTable.IS_AUTO_CHECK_METADATA = true;
        DataRow user = service.query("CRM_USER");
        user.put("JS","{\"id\":1}");
        user.clearUpdateColumns();
        service.update(user);
        user = service.query("CRM_USER");
        System.out.println(user.toJSON());
    }
    @Test
    public void blob(){
        //BT 设置成blob类型
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        DataRow user = service.query("CRM_USER");
        System.out.println(user.toJSON());
        System.out.println("blob:"+new String((byte[])user.get("BT")));
        user.put("BT","abc".getBytes());
        user.clearUpdateColumns();
        service.update(user);
    }

    @Test
    public void query(){
        //关于几个 空值 的查询条件
        ConfigStore store = new DefaultConfigStore();
        store.and("+ID", null);                // ID IS NULL
        store.and("+REMARK", "");              // REMARK = ''
        store.and("+IDX", "".split(","));      // IDX = ''
        store.and("+CODE", new ArrayList<>());       // CODE IS NULL
        store.and("+VAL", new String[]{});           // VAL IS NULL
        DataSet set = service.querys("bs_dict(ID,GROUP_CODE,CODE,NM,VAL)", store);

        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        //反例 这里不能这样直接拼上，因为list.toString返回的结果会有空格[1, 2, 3]
        service.query("bs_dict","ID:"+ids, "(id>0 and age>10)");
        service.query("bs_dict","ID:"+BeanUtil.list2string(ids));

    }
    @Test
    public void update(){
        DataRow row = new DataRow();
        row.put("-REMARK","不更新,不插入");	//添加到row中 但不参与插入(更新)
        row.put("+CODE", null);				//默认情况这值不参与插入(更新)， +表示强制参与插入(更新)
        service.update("bs_dict", row);
        //只更新CODE REMARK
        service.update("bs_dict",row, "CODE", "REMARK");
        //CODE强制更新 其他按默认情况(但不包括已忽略的列)
        service.update("bs_dict",row,"+CODE");
        //只更新值有变化的列(但不包括已忽略的列)
        service.update("bs_dict",row);
    }
    @Test
    public void delete(){
        //根据 ID 删除多行
        try {
            //注意:为了避免整表删除,values必须提供否则会抛出异常
            //整表删除请调用service.execute("DELETE FROM HR_EMPLOYEE");
           // service.deletes("HR_EMPLOYEE", "ID");
        }catch (Exception e){
            e.printStackTrace();
        }
        //DELETE FROM HR_EMPLOYEE WHERE ID = 100
        service.deletes("HR_EMPLOYEE", "ID", "100");
        //DELETE FROM HR_EMPLOYEE WHERE ID IN(100,200)
        service.deletes("HR_EMPLOYEE", "ID", "100","200");

        service.deletes("HR_EMPLOYEE", "ID", 1,2);

        List<String> ids = new ArrayList<>();
        //注意:为了避免整表删除,ids必须提供否则会抛出异常
        //service.deletes("HR_EMPLOYEE", "ID", ids);
        ids.add("100");
        ids.add("200");
        service.deletes("HR_EMPLOYEE", "ID", ids);

        //根据多列条件删除
        //DELETE FROM HR_EMPLOYEE WHERE ID = 1 AND NM = 'ZH'
        DataRow row = new DataRow();
        row.put("ID","1");
        row.put("NM", "ZH");
        service.delete("HR_EMPLOYEE", row, "ID","NAME");

        //DELETE FROM HR_EMPLOYEE WHERE ID = 1 AND CODE = 20
        service.delete("HR_EMPLOYEE","ID","1", "CODE:20");

        //DELETE FROM HR_EMPLOYEE WHERE ID = '' AND CODE = 20
        service.delete("HR_EMPLOYEE","ID","", "CODE:20");
        //DELETE FROM HR_EMPLOYEE WHERE ID = 1 AND CODE = ''
        service.delete("HR_EMPLOYEE","ID","1", "CODE:");


    }
    @Test
    public void set(){
        DataSet set = new DataSet();
        DataRow row = new DataRow();
        row.put("ID",1);
        row.put("NAME", "张三");
        set.add(row);

        //value可以是正则表达式,也可以是SQL通配符
        DataSet result = set.select.like("NAME","张%");
        System.out.println(result);
        result = set.getRows("NAME:张%");
        System.out.println(result);
        result = set.getRows("NAME","张%");
        System.out.println(result);
    }

    @Test
    public void map(){
        Map<String,String> map = new HashMap<>();
        ConfigTable.IS_UPPER_KEY = false;
        map.put("ID","1");
        map.put("up_Status", "2");
        service.update("HR_EMPLOYEE", map, "up_Status");
        service.insert("HR_EMPLOYEE", map);
    }
    @Test
    public void or(){
        ConfigStore configs = new DefaultConfigStore();
        configs.and("ID", 1).or("NM","ZH").ors("SEX","1");
        configs.and(Compare.LIKE, "nm","zh");
        service.querys("HR_EMPLOYEE", configs);
    }
}

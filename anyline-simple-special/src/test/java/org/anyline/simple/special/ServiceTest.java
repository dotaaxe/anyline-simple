package org.anyline.simple.special;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class ServiceTest {
    private Logger log = LoggerFactory.getLogger(ServiceTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void query(){
        //关于几个 空值 的查询条件
        ConfigStore store = new DefaultConfigStore();
        store.addCondition("+ID", null);                // ID IS NULL
        store.addCondition("+REMARK", "");              // REMARK = ''
        store.addCondition("+IDX", "".split(","));      // IDX = ''
        store.addCondition("+CODE", new ArrayList<>());       // CODE IS NULL
        store.addCondition("+VAL", new String[]{});            // VAL IS NULL
        DataSet set = service.querys("bs_dict(ID,GROUP_CODE,CODE,NM,VAL)", store);
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
        //DELETE FROM HR_EMPLOYEE WHERE ID IN(100,200)
        service.deletes("HR_EMPLOYEE", "ID", "100");
        service.deletes("HR_EMPLOYEE", "ID", "100","200");

        //根据多列条件删除
        //DELETE FROM HR_EMPLOYEE WHERE ID = 1 AND NM = 'ZH'
        DataRow row = new DataRow();
        row.put("ID","1");
        row.put("NM", "ZH");
        service.delete("HR_EMPLOYEE", row, "ID","NM");
    }

}

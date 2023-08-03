package org.anyline.simple.trans.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.entity.DataRow;
import org.anyline.simple.trans.service.EmployeeService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/emp")
public class EmployeeController extends AnylineController {

    @Autowired
    private EmployeeService eservice;

    @RequestMapping("/a")
    public String insert(){
        long qty = service.count("HR_EMPLOYEE");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            eservice.insert(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        long cnt = eservice.count("HR_EMPLOYEE");
        return success("insert前后行数:"+qty+">"+cnt);
    }


    //不要在controller中回滚 除非能把异常抛出到上一层,也就是抛给spring容器

    //以下这样不行
    @RequestMapping("/a1")
    public String insert1(){
        long qty = service.count("HR_EMPLOYEE");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            insert1(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        long cnt = eservice.count("HR_EMPLOYEE");
        return success("insert前后行数:"+qty+">"+cnt);
    }

    @Transactional
    public void insert1(DataRow row){
        service.insert("HR_EMPLOYEE", row);
        throw  new RuntimeException("test RuntimeException");
    }
}

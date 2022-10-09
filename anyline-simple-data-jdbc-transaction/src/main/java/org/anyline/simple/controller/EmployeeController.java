package org.anyline.simple.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.entity.DataRow;
import org.anyline.simple.service.EmployeeService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("emp")
public class EmployeeController extends AnylineController {

    @Autowired
    private EmployeeService service;
    @RequestMapping("a")
    public String add(){
        int qty = service.count("HR_EMPLOYEE");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            service.insert(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        int cnt = service.count("HR_EMPLOYEE");
        return success("insert前后行数:"+qty+">"+cnt);
    }
}

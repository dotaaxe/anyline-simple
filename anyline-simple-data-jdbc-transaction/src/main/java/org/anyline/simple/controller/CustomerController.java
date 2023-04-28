package org.anyline.simple.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.simple.service.CustomerService;
import org.anyline.simple.service.EmployeeService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
@RequestMapping("cust")
public class CustomerController extends AnylineController {

    @Autowired
    private CustomerService service;
    @RequestMapping("a")
    public String add(){
        int qty = service.count("<crm>crm_customer");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            service.insert(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        int cnt = service.count("<crm>crm_customer");
        return success("insert前后行数:"+qty+">"+cnt);
    }
}

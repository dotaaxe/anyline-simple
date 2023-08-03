package org.anyline.simple.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.entity.DataRow;
import org.anyline.simple.service.CRMService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("crm")
public class CRMController extends AnylineController {

    @Autowired
    private CRMService service;
    @RequestMapping("a")
    public String add(){
        long qty = service.count("<crm>crm_customer");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            service.insert(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        long cnt = service.count("<crm>crm_customer");
        return success("insert前后行数:"+qty+">"+cnt);
    }
}

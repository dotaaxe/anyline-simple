package org.anyline.simple.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.entity.DataRow;
import org.anyline.simple.service.DefaultService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("def")
public class DefaultController extends AnylineController {

    @Autowired
    private DefaultService service;
    @RequestMapping("a")
    public String add(){
        long qty = service.count("HR_EMPLOYEE");
        try {
            DataRow row = new DataRow();
            row.put("NAME", BasicUtil.getRandomString(10));
            service.insert(row);
        }catch (Exception e){
            e.printStackTrace();
        }
        long cnt = service.count("HR_EMPLOYEE");
        return success("insert前后行数:"+qty+">"+cnt);
    }
}

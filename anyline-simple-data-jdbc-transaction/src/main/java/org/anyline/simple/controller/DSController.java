package org.anyline.simple.controller;

import org.anyline.controller.impl.AnylineController;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.simple.service.DSService;
import org.anyline.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ds")
public class DSController extends AnylineController {

    @Autowired
    private DSService service;

    @RequestMapping("a")
    public String add(){
        String ds = "sso";
        DataSourceHolder.setDataSource(ds);
        long qty = service.count("SSO_USER");
        try {
            DataRow row = new DataRow();
            row.put("NM", BasicUtil.getRandomString(10));
            service.insert(ds, row);
        }catch (Exception e){
            e.printStackTrace();
        }
        long cnt = service.count("SSO_USER");
        return success("insert前后行数:"+qty+">"+cnt);
    }
}

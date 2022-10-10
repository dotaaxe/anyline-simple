package org.anyline.simple.clear.controller;

import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("web.home.DefaultController")
@RequestMapping("/")
public class DefaultController extends BasicController {
    //如果没有从父类中继承到AnylineService
    //就直接在需要的位置注入一个
    @Qualifier("anyline.service")
    private AnylineService tmpService;

    @RequestMapping("list")
    @ResponseBody
    public String list() {
        DataSet set = tmpService.querys("crm_user");
        return success(set);
    }

}

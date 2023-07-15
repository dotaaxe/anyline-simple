package org.anyline.simple.clear.controller;

import org.anyline.entity.DataSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("web.home.DefaultController")
@RequestMapping("/")
public class DefaultController extends BasicController {
    //访问url
    // http://127.0.0.1:8080/list?id=1&id=2
    // 生成SQL SELECT * FROM crm_user WHERE ( ID IN (?,?) AND  CODE != ?) LIMIT 0,10

    // http://127.0.0.1:8080/list?n=A
    //生成SQL SELECT * FROM crm_user WHERE  NAME LIKE concat('%',?,'%') AND AND  CODE != ?  LIMIT 0,10
    @RequestMapping("list")
    @ResponseBody
    public String list() {
        //这里的true表示 需要分页
        DataSet set = service.querys("crm_user", condition(true,"NAME:%n%","ID:[id]","CODE:!=cd:${9}"));
        return success(set);
    }

}

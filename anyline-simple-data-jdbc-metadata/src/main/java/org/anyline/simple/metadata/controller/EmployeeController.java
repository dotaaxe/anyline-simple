package org.anyline.simple.metadata.controller;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("js.EmployeeController")
@RequestMapping("/js/emp")
public class EmployeeController extends BasicController {


    @RequestMapping("l")
    @ResponseBody
    public String list() {
        //这里需要配置 ConfigTable.HTTP_PARAM_KEY_CASE = "camel";
        //camel:小驼峰 Camel:大驼峰 lower:小写 upper:大写
        //更复杂的情况需要实现 EntityAdapter
        List<String> params = service.column2param("HR_EMPLOYEE");
        DataSet set = service.querys("HR_EMPLOYEE(ID,CODE,NM)", condition(true, params,"CODE:code"),"ID>0" );
        return success(set);
    }

    @RequestMapping("s")
    @ResponseBody
    public String save() {
        //只接收HR_EMPLOYEE表中有的属性调用AdapterProxy.metadata2param识别参数格式
        DataRow employee = entity("${HR_EMPLOYEE}");
        service.save("HR_EMPLOYEE", employee);

        DataSet employees = entitys("${HR_EMPLOYEE}");
        service.save("HR_EMPLOYEE", employees);

        //接收全部属性
        DataRow row = entity();
        List<String> cols = service.columns("HR_EMPLOYEE");
        //只更新或插入一部分属性
        service.save("HR_EMPLOYEE", row, cols);
        return success();
    }
}

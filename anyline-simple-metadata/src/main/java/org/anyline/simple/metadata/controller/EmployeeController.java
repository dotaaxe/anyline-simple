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
        List<String> params = service.metadata2param("hr_employee");
        DataSet set = service.querys("hr_employee(ID,CODE,NM)", condition(true, params,"CODE:code"),"ID>0" );
        return success(set);
    }

    @RequestMapping("s")
    @ResponseBody
    public String save() {
        //只接收hr_employee表中有的属性调用AdapterProxy.metadata2param识别参数格式
        DataRow employee = entity("${hr_employee}");
        service.save("hr_employee", employee);

        DataSet employees = entitys("${hr_employee}");
        service.save("hr_employee", employees);

        //接收全部属性
        DataRow row = entity();
        List<String> cols = service.metadata("hr_employee");
        //只更新或插入一部分属性
        service.save("hr_employee", row, cols);
        return success();
    }
}

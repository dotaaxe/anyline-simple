package org.anyline.simple.json.home.controller.hr;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller("js.home.hr.EmployeeController")
@RequestMapping("/js/home/hr/emp")
public class EmployeeController extends BasicController {

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

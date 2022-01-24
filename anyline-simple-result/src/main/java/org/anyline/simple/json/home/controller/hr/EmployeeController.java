package org.anyline.simple.json.home.controller.hr;

import org.anyline.entity.DataRow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("js.home.hr.EmployeeController")
@RequestMapping("/js/home/hr/emp")
public class EmployeeController extends BasicController {

    @RequestMapping("s")
    @ResponseBody
    public String save() {
        DataRow row = entity("{hr_employee}");
        service.save("hr_employee", row);
        return success();
    }
}

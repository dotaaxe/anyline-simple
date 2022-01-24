package org.anyline.simple.json.home.controller.hr;

import org.anyline.entity.DataRow;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("js.home.hr.DepartmentController")
@RequestMapping("/js/home/hr/dept")
public class DepartmentController extends BasicController {

    @RequestMapping("s")
    @ResponseBody
    public String save() {
        DataRow row = entity("ID:id+","NM:nm");
        if(row.isNew()){
            if(service.exists("HR_DEPARTMENT","NM:"+row.getNm())){
                return fail("部门重名");
            }
        }
        service.save("HR_DEPARTMENT", row);
        return success();
    }
    @RequestMapping("d")
    @ResponseBody
    public String delete() {
        DataRow row = service.query("HR_DEPARTMENT", condition("++ID:id+"));
        if(null == row){
            return fail("部门不存在");
        }
        if(service.exists("HR_EMPLOYEE","DEPARTMENT_ID:"+row.getId())){
            return fail("删除失败:当前部门中有关联人员");
        }
        service.delete(row);
        return success();
    }
}

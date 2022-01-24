package org.anyline.simple.web.home.controller.hr;

import org.anyline.entity.DataSet;
import org.anyline.entity.html.TableBuilder;
import org.anyline.web.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller("web.home.hr.EmployeeController")
@RequestMapping("/web/home/hr/emp")
public class EmployeeController extends BasicController{
    public String dir = "employee";
    @RequestMapping("l")
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView mv = template("list.jsp");
        DataSet departments = service.querys("HR_DEPARTMENT");
        mv.addObject("depts", WebUtil.encrypt(departments,"ID"));//加密ID
        return mv;
    }

    @RequestMapping("navi")
    @ResponseBody
    public String navi(HttpServletRequest request, HttpServletResponse response) {
        DataSet set = service.querys("V_HR_EMPLOYEE"
                    , condition(true            //需要分页
                        ,"NM:%nm%"          //按姓名like查询
                        ,"DEPARTMENT_ID:[dept+]"    //按部门ID IN查询[]表示数组 +表示值经过加密需要解密
                        ,"EMPLOYEE_NM:%epln%"       //部门名称like查询
                )
        );
        request.setAttribute("set", WebUtil.encrypt(set, "ID"));
        return navi(true,request, response, set, "/WEB-INF/web/home/template/data/hr/employee/navi.jsp");
    }
    //按部门分组
    @RequestMapping("group")
    public ModelAndView group() {
        ModelAndView mv = template("group.jsp");

        DataSet set = service.querys("V_HR_EMPLOYEE");              //查询人员列表
        WebUtil.encrypt(set, "ID","DEPARTMENT_ID");               //加密
        DataSet groups = set.group("DEPARTMENT_ID", "DEPARTMENT_NM");   //根据部门ID,部门名称分组

        mv.addObject("groups", groups);
        return mv;
    }

    //生成table
    @RequestMapping("table")
    public ModelAndView table() {
        ModelAndView mv = template("table.jsp");

        DataSet set = service.querys("V_HR_EMPLOYEE","ORDER BY DEPARTMENT_NM");              //查询人员列表


        List<String> headers = new ArrayList<>();
        headers.add("序号");
        headers.add("部门");
        headers.add("工号");
        headers.add("姓名");
        headers.add("入职日期");
        headers.add("复合属性");


        String table = TableBuilder.init()
                //设置需要显示的列
                .setFields(
                        "{num}(DEPARTMENT_NM)"      //根据部门设置序号
                        ,"DEPARTMENT_NM"
                        ,"CODE"
                        ,"NM"
                        ,"JOIN_YMD"
                        ,"入职日期:{JOIN_YMD},工号:{CODE}"
                )
                //设置需要合并的列
                .addUnion(
                        "DEPARTMENT_NM"
                        ,"JOIN_YMD(DEPARTMENT_NM)"      //只有部门合并的情况下，入职日期相同的才合并
                )
                .setDatas(set)
                .setClazz("list")
                .setHeaders(headers)
                .build().build();

        //或
        /*table = TableBuilder.init()
                .setUnions(unions).setDatas(set).setClazz("list")
                .addConfig("序号","{num}","100px", "text-align:center;")
                .addConfig("部门","DEPARTMENT_NM","100px", "text-align:center;")
                .addConfig("入职日期","JOIN_YMD","100px", "text-align:center;")
                .addConfig("工号","CODE","100px", "text-align:center;")
                .addConfig("姓名","NM","100px", "text-align:right;color:red;")
                .build().build();*/
        mv.addObject("html", table);
        return mv;
    }
}

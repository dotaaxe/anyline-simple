package org.anyline.simple.web.home.controller.hr;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.net.HttpUtil;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.DateUtil;
import org.anyline.util.MapUtil;
import org.anyline.web.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("web.home.hr.SalaryController")
@RequestMapping("/web/home/hr/slr")
public class SalaryController extends BasicController{
    public String dir = "salary";
    @RequestMapping("l")
    public ModelAndView list() {
        ModelAndView mv = template("list.jsp");
        DataSet departments = service.querys("HR_DEPARTMENT");
        mv.addObject("depts", WebUtil.encrypt(departments,"ID"));

        return mv;
    }

    @RequestMapping("navi")
    @ResponseBody
    public String navi(HttpServletRequest request, HttpServletResponse response) {
        DataSet set = service.querys("V_HR_SALARY", condition(true,"DEPARTMENT_ID:dept+","TOTAL_PRICE:>=fr","TOTAL_PRICE:<=to"));
        set.divide("TOTAL_PRICE_K","TOTAL_PRICE",1000.0).format.number("0","TOTAL_PRICE_K");
        request.setAttribute("set", WebUtil.encrypt(set, "ID","EMPLOYEE_ID","DEPARTMENT_ID"));
        return navi(true,request, response, set, "/WEB-INF/web/home/template/data/hr/salary/navi.jsp");
    }
    @RequestMapping("pivot")
    public ModelAndView pivot() {
        ModelAndView mv = template("pivot.jsp");
        //查询工资列表
        int yyyy = BasicUtil.parseInt(DateUtil.format("yyyy"),0)-1;
        long fr = System.currentTimeMillis();
        DataSet set = service.querys("V_HR_SALARY","YYYY:"+ yyyy, "ORDER BY YM");
        mv.addObject("query_time", System.currentTimeMillis()-fr);
        //行转列
        fr = System.currentTimeMillis();
        DataSet groups = set.pivot("EMPLOYEE_CODE,EMPLOYEE_NM","YM","TOTAL_PRICE");
        mv.addObject("pivot_time", System.currentTimeMillis()-fr);
        List<String> yms = set.getDistinctStrings("YM");
        //月最高
        DataRow max = set.max("TOTAL_PRICE");
        //月平均
        DataRow avg = set.avgs("TOTAL_PRICE","BASE_PRICE","REWARD_PRICE");

        mv.addObject("set", set);
        mv.addObject("groups", groups);
        mv.addObject("yms", yms);
        mv.addObject("set", set);
        mv.addObject("max", max);
        mv.addObject("avg", avg);
        return mv;
    }
    @RequestMapping("pivots")
    public ModelAndView pivotSet() {
        ModelAndView mv = template("pivots.jsp");
        //查询工资列表
        DataSet types = service.querys("bs_dict","GROUP_CODE:SALARY_PRICE_TYPE");
        int yyyy = BasicUtil.parseInt(DateUtil.format("yyyy"),0)-1;
        long fr = System.currentTimeMillis();
        DataSet set = service.querys("V_HR_SALARY_TYPE(EMPLOYEE_CODE,EMPLOYEE_NM,YM,TYPE_CODE,TYPE_NM,PRICE)","YYYY:"+ yyyy);
        mv.addObject("query_time", System.currentTimeMillis()-fr);
        //行转列
        fr = System.currentTimeMillis();
        DataSet groups = set.pivot("EMPLOYEE_CODE,EMPLOYEE_NM","YM,TYPE_CODE","PRICE");
        mv.addObject("pivot_time", System.currentTimeMillis()-fr);


        List<String> yms = set.getDistinctStrings("YM");
        mv.addObject("groups", groups);
        mv.addObject("yms", yms);
        mv.addObject("set", set);
        mv.addObject("types", types);
        return mv;
    }
    @RequestMapping("pivotm")
    public ModelAndView pivotMap() {
        ModelAndView mv = template("pivotm.jsp");
        //查询工资列表
        DataSet types = service.querys("bs_dict","GROUP_CODE:SALARY_PRICE_TYPE");
        int yyyy = BasicUtil.parseInt(DateUtil.format("yyyy"),0)-1;
        long fr = System.currentTimeMillis();
        List<Map<String,Object>> maps = service.maps("V_HR_SALARY_TYPE(EMPLOYEE_CODE,EMPLOYEE_NM,YM,TYPE_CODE,TYPE_NM,PRICE)","YYYY:"+ yyyy);
        mv.addObject("query_time", System.currentTimeMillis()-fr);
        mv.addObject("maps_size", maps.size());
        //行转列
        fr = System.currentTimeMillis();
        Collection groups = MapUtil.pivot(maps,"EMPLOYEE_CODE,EMPLOYEE_NM","YM,TYPE_CODE","PRICE");
        mv.addObject("pivot_time", System.currentTimeMillis()-fr);
        mv.addObject("groups_size", groups.size());


        Collection<Object> yms = MapUtil.distinctValue(maps,"YM");
        mv.addObject("groups", groups);
        mv.addObject("yms", yms);
        mv.addObject("types", types);
        mv.addObject("maps", maps);
        return mv;
    }

}

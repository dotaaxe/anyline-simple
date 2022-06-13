package org.anyline.simple.web.home.controller.hr;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.web.util.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;

@Controller("web.home.hr.DepartmentController")
@RequestMapping("/web/home/hr/dept")
public class DepartmentController extends BasicController{
    public String dir = "department";
    @RequestMapping("l")
    public ModelAndView list() {
        ModelAndView mv = template("list.jsp");
        return mv;
    }

    @RequestMapping("navi")
    @ResponseBody
    public String navi(HttpServletRequest request, HttpServletResponse response) {
        DataSet set = service.querys("HR_DEPARTMENT", condition(true,"NM:%nm%"));
        request.setAttribute("set", WebUtil.encrypt(set, "ID"));
        return navi(true,request, response, set, "/WEB-INF/web/home/template/data/hr/department/navi.jsp");
    }
    @RequestMapping("u")
    public ModelAndView update() {
        ModelAndView mv = template("info.jsp");
        DataRow row = service.query("HR_DEPARTMENT", condition("++ID:id+"));
        if(null == row){
            return error("部门不存在");
        }
        mv.addObject("row", WebUtil.encrypt(row,"ID"));
        return mv;
    }
    @RequestMapping("a")
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = template("info.jsp");
        return mv;
    }

    /**
     * 微信中打开自动调起浏览器下载文件
     * @param request
     * @param response
     */
    @RequestMapping("d")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        WebUtil.download(request, response, new File("D:\\a.txt"));
    }

}

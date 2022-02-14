package org.anyline.simple.web.home.controller;

import org.anyline.entity.DataSet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("web.home.DefaultController")
@RequestMapping("/")
public class DefaultController extends BasicController {
    @RequestMapping({"","index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = template("index.jsp");

        return mv;
    }

    /**
     * PARAM:like?nm=燕
     * SQL:NM LIKE '%燕%'
     * @return json
     */
    @RequestMapping("like")
    @ResponseBody
    public String like() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("NM:%nm%"));
        return success(set);
    }
    /**
     * PARAM:like1?code=1&cd=2
     * SQL:CODE LIKE '%1%'
     *
     * PARAM:like1?cd=2
     * SQL:CODE LIKE '%2%'
     *
     * PARAM:like1
     * SQL:CODE LIKE '%9%'
     *
     * @return json
     */
    @RequestMapping("like1")
    @ResponseBody
    public String like1() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("CODE:%code:cd:{9}%"));
        return success(set);
    }
    /**
     * PARAM:in?dept=1&dept=2
     * SQL:DEPARTMENT_ID IN(1,2)
     * @return json
     */
    @RequestMapping("in")
    @ResponseBody
    public String in() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:[dept]"));
        return success(set);
    }
    /**
     * PARAM:in1?id=1&id=2
     * SQL:ID IN(1,2)
     *
     * PARAM:in1?cd=11&cd=12
     * SQL:ID IN(11,12)
     *
     * PARAM:in1
     * SQL:ID IN(6,7,8)
     * @return json
     */
    @RequestMapping("in1")
    @ResponseBody
    public String in1() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("ID:[id:cd:{6,7,8}]"));
        return success(set);
    }
    /**
     * PARAM:or?dept=1&sex=0
     * SQL:DEPARTMENT_ID = 1 OR SEX = 0
     * @return json
     */
    @RequestMapping("or")
    @ResponseBody
    public String or() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:dept|SEX:sex"));
        return success(set);
    }

    /**
     * PARAM:or1?d1=1&d2=2
     * SQL:DEPARTMENT_ID = 1 OR DEPARTMENT_ID = 2
     * @return json
     */
    @RequestMapping("or1")
    @ResponseBody
    public String or1() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:d1|d2"));
        return success(set);
    }
    /**
     * PARAM:or2?d1=1&d2=2
     * SQL:DEPARTMENT_ID = 1 OR DEPARTMENT_ID = 2
     * @return json
     */
    @RequestMapping("or2")
    @ResponseBody
    public String or2() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:d1|d2"));
        return success(set);
    }

}

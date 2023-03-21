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
     * URL:/like?nm=燕
     * WHERE:NM LIKE '%燕%'
     * @return json
     */
    @RequestMapping("like")
    @ResponseBody
    public String like() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("NM:%nm%"));
        return success(set);
    }
    /**
     * URL:/like1?code=1&cd=2
     * WHERE:CODE LIKE '%1%'
     *
     * URL:/like1?cd=2
     * WHERE:CODE LIKE '%2%'
     *
     * URL:/like1
     * WHERE:CODE LIKE '%9%'
     *
     * @return json
     */
    @RequestMapping("like1")
    @ResponseBody
    public String like1() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("CODE:%code:cd:${9}%"));
        return success(set);
    }
    /**
     * URL:/in?dept=1,2
     * WHERE:DEPARTMENT_ID IN(1,2)
     * @return json
     */
    @RequestMapping("in")
    @ResponseBody
    public String in() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:[split(dept)]"));
        return success(set);
    }
    /**
     * URL:/in1?id=1&id=2
     * WHERE:ID IN(1,2)
     *
     * URL:/in1?cd=11&cd=12
     * WHERE:ID IN(11,12)
     *
     * URL:/in1
     * WHERE:ID IN(6,7,8)
     * @return json
     */
    @RequestMapping("in1")
    @ResponseBody
    public String in1() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("ID:[id:cd:${6,7,8}]"));
        set = service.querys("HR_EMPLOYEE", condition("ID:[id:cd:${[6,7,8]}]"));
        return success(set);
    }
    /**
     * URL:/or?dept=1&sex=0
     * WHERE:DEPARTMENT_ID = 1 OR SEX = 0
     * @return json
     */
    @RequestMapping("or")
    @ResponseBody
    public String or() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:dept|SEX:sex:s:${1}"));
        return success(set);
    }

    /**
     * URL:/or1?d1=1&d2=2
     * WHERE:DEPARTMENT_ID = 1 OR DEPARTMENT_ID = 2
     *
     * URL:/or1?d2=2
     * WHERE:
     *
     * @return json
     */
    @RequestMapping("or1")
    @ResponseBody
    public String or1() {
        //只有d1取值成功 当前条件才生效
        //如果需要d1,d2不相干 condition("DEPARTMENT_ID:d1|DEPARTMENT_ID:d2")
        DataSet set = service.querys("HR_EMPLOYEE",
                condition("DEPARTMENT_ID:d1|d2"));
        return success(set);
    }
    @RequestMapping("or2")
    @ResponseBody
    public String or2() {
        //只有d1取值成功 当前条件才生效
        DataSet set = service.querys("HR_EMPLOYEE",
                condition("DEPARTMENT_ID:d1|${9}"));
        return success(set);
    }
    /**
     * URL:/or9?d1=1
     * WHERE:DEPARTMENT_ID = 1
     *
     * URL:/or9?d2=2
     * WHERE:DEPARTMENT_ID = 2
     *
     * URL:/or9
     * WHERE:DEPARTMENT_ID = 9
     * @return json
     */
    @RequestMapping("or9")
    @ResponseBody
    public String or9() {
        //依次取d1,d2的值，如果d1取值成功则忽略d2,如果都失败则取默认值9
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:d1:d2:${9}"));
        return success(set);
    }

    @RequestMapping("ors")
    @ResponseBody
    public String ors() {
        //(SORT=1 AND AGE = 18) OR (SORT = 0 AND SEX IN (0,1))
        DataSet set = service.querys("HR_EMPLOYEE", condition("DEPARTMENT_ID:dept:${1}","SEX:sex:${1}").ors("DEPARTMENT_ID","2").and("SEX","2"));
        set.toLowerKey().camel();
        return success(set);
    }

    //特别注意这以下情况 SEX与SQL中的变量匹配不成功时，SQL会追加一个条件  SEX = ?
    @RequestMapping("sql")
    @ResponseBody
    public String sql() {
        String sql = "SELECT * FROM HR_EMPLOYEE WHERE DEPARTMENT_ID = :DEPT";
        DataSet set = service.querys(sql
                , condition("DEPT:dept","SEX:sex:${1}")
        );
        set.toLowerKey().camel();
        return success(set);
    }
    @RequestMapping("find")
    @ResponseBody
    public String find() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("[CODE]:code"));
        return success(set);
    }
    @RequestMapping("finds")
    @ResponseBody
    public String finds() {
        DataSet set = service.querys("HR_EMPLOYEE", condition("[CODE]:split(code)"));
        return success(set);
    }
}

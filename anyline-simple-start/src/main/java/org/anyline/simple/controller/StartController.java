package org.anyline.simple.controller;

import org.anyboot.mvc.controller.impl.TemplateController;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("StartController")
@RequestMapping("start")
public class StartController extends TemplateController {
    @RequestMapping("l")
    public String list(){
        DataSet set = service.querys("HR_EMPLOYEE(ID,NM)"
                , condition(
                        true                    //分页查询
                        ,"CODE:code"         //按编号查询           CODE = 101
                        , "NM:%nm%"                  //模糊查询             NM LIKE '%zh%'
                        ,"DEPARTMENT_ID:[dept]"      //部门ID              DEPARTMENT_ID IN(1,2,3)
                        ,"JOIN_YMD:>=fr"             //入职日期大小fr的      JOIN_YMD >= 2020-01-01
                        ,"JOIN_YMD:<=to"             //入职日期小于to的      JOIN_YMD <= 2020-12-01
                )
                ,"DATA_STATUS:1"           //只果DATA_STATUS=1的
        );
        return success(set);
    }

    /**
     * insert 或 update
     * 如果输入了主键ID的值，则执行update
     * 如果没有输入主键ID的值，则执行insert
     * @return String
     */
    @RequestMapping("s")
    public String save(){
        //这里的ID表示列名,id表示url参数名
        //前端提交数据可以通过以下形式
        //1. url?k=v1&k=v2&v1=v1的形貌提交
        //2. 在form体中提交
        //2. 在body体中以json格式提交
        DataRow row = entity("ID:id","NM:nm");
        log.warn(row.toJSON());
        //如果列太多可以根据表结构接收
        //如果前端提交的KEY是小驼峰格式，数据库中列是以下划线分隔，可以通过anyline-config.xml中配置参数实现自动转换
        //<property key="HTTP_PARAM_KEYS_CASE">camel</property>
        row = entity("{HR_EMPLOYEE}");

        //save会根据主键是否存在来确认执行insert 或update
        service.save("HR_EMPLOYEE", row);
        //在执行了save（insert）之后 DataRow中的主键就有值了
        log.warn("id:{}",row.getId());


        return success(row.getId());
    }

    /**
     * 根据ID删除
     * 注意这里的ID前面的++前缀，表示如果没有输入ID参数值，则不执行查询，也就是query返回null
     * @return String
     */
    @RequestMapping("d")
    public String delete(){
        DataRow row = service.query("HR_EMPLOYEE", condition("++ID:id"));
        if(null == row){
            return fail("用户数据不存在");
        }
        service.delete(row);
        return success();
    }
}

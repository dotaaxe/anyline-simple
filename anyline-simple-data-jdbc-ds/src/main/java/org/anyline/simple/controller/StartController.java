package org.anyline.simple.controller;

import org.anyline.controller.impl.TemplateController;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("StartController")
@RequestMapping("start")
public class StartController extends TemplateController {
    @RequestMapping("l")
    public String list(){
        //设置固定数据源，在这之后执行的查询都是查询的crm数据源
        DataSourceHolder.setDataSource("crm");
        //这里查询的的crm数据源，执行完成后还保持crm数据源
        DataSet set = service.querys("crm_customer");


        //这里查询erp数据源中的表，执行完成后切换回执行之前的crm数据源，注意并不是默认数据源
        set = service.querys("<erp>mm_material");

        //这里查询的的crm数据源，而不需要指定<crm>,因为之前已通过DataSourceHolder固定设置了crm数据源
        set = service.querys("crm_customer");

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
        DataRow row = entity("ID:id","NM:nm");
        //这里会往crm数据源中插入数据
        service.save("<crm>crm_customer", row);
        return success(row.getId());
    }

}

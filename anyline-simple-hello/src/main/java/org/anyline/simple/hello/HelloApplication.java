package org.anyline.simple.hello;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.PageNaviImpl;
import org.anyline.jdbc.config.ConfigStore;
import org.anyline.jdbc.config.impl.ConfigStoreImpl;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.GISUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class HelloApplication {
    public static void main(String[] args) {
        ConfigTable.IS_SQL_DELIMITER_OPEN = true;
        SpringApplication application = new SpringApplication(HelloApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");



        DataSet set = service.querys("BS_VALUE");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", 0, 10);
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", "ID:1");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", "CODE:1%");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", "ID:1");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", "ID=1","NM IS NOT NULL");
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", 0, 9,"NM IS NOT NULL");
        PageNavi navi = new PageNaviImpl(); //这里的分页数据一般不直接new，而是通过http参数自动构造
        navi.setPageRows(10);
        navi.setCurPage(2);//默认第1页,下标从1开始
        service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", navi, "NM IS NOT NULL");
        //实际开发中经常这样写分页, true:表示需要分页,其他参数通过http参数自动抽取
        // service.querys("BS_VALUE", condition(true), "NM IS NOT NULL");
        service.querys("BS_VALUE", new ConfigStoreImpl().addConditions("ID",1,2,3));
        System.out.println(set);

        DataRow row = new DataRow();
        row.put("CODE","A001");
        row.put("NM","TEST");
        row.put("DATA_STATUS","1");
        service.save("BS_VALUE", row);

    }

}

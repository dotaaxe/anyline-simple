package org.anyline.simple.hello;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.config.ConfigStore;
import org.anyline.jdbc.config.impl.ConfigStoreImpl;
import org.anyline.service.AnylineService;
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
        //关于几个 空值 的查询条件
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        ConfigStore store = new ConfigStoreImpl();
        store.addCondition("+ID", null);                // ID IS NULL
        store.addCondition("+REMARK", "");              // REMARK = ''
        store.addCondition("+IDX", "".split(","));      // IDX = ''
        store.addCondition("+CODE", new ArrayList<>());       // CODE IS NULL
        store.addCondition("+VAL", new String[]{});            // VAL IS NULL
        DataSet set = service.querys("BS_VALUE(ID,GROUP_CODE,CODE,NM,VAL)", store);


    }

}

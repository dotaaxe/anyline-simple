package org.anyline.simple.condition;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ConditionApplication.class)
public class ConditionTest {
    @Autowired
    private AnylineService service          ;

    @Test
    public void or(){
        ConfigStore configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.or("ID","2").or("ID","3").or("ID","4");
        service.query("crm_user", configs);
        //WHERE(ID = 1 OR  ID = 2 OR  ID = 3 OR  ID = 4)

        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.or("ID","2").or("ID","3").or("ID","4", true, true);
        service.query("crm_user", configs);
        //WHERE(ID = 4)
        //因为最后一步要覆盖相同的条件,也就是相同的条件中只留下第一个，并且用4覆盖原有的值（这里留哪个没影响，因为他的值最后都会被4覆盖）

        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.or("ID","2").or("ID","3").or("ID","4", true, false);
        service.query("crm_user", configs);
        //WHERE(ID = 1)
        //因为最后一步要覆盖相同的条件，也就是相同的条件中只留下第一个，但并不覆盖值，所以并没有用4覆盖1

        /**
         * or 表示当前条件 与前一个条件  形成 或关系
         */
        configs = new DefaultConfigStore();
        configs.and("ID", "1").and("ID", "2").or("ID", "3").and("ID", "4");
        service.query("crm_user", configs);
        //WHERE ID = 1 AND (ID = 2 OR  ID = 3) AND  ID = 4

        /**
         * ors 表示当前条件 与之前的全部条件  形成 或关系
         */
        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.and("ID","2").ors("ID","3");
        service.query("crm_user", configs);
        //WHERE((ID = 1 AND  ID = 2) OR  ID = 3)

        //(反例  反例 反例)
        // 多个ors尽量不要用 看最后面的方式
        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.ors("ID","2").ors("ID","3").ors("ID","4");
        service.query("crm_user", configs);
        //WHERE((((ID = 1) OR  ID = 2) OR  ID = 3) OR  ID = 4) //是不是很乱

        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.ors("ID","2").ors("ID","3").ors("ID","4", true, true);
        service.query("crm_user", configs);
        //WHERE(ID = 4)
        //因为最后一步要覆盖相同的条件,也就是相同的条件中只留下第一个，并且用4覆盖原有的值（这里留哪个没影响，因为他的值最后都会被4覆盖）

        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.ors("ID","2").ors("ID","3").ors("ID","4", true, false);
        service.query("crm_user", configs);
        //WHERE(ID = 1)
        //因为最后一步要覆盖相同的条件，也就是相同的条件中只留下第一个，但并不覆盖值，所以并没有用4覆盖1

        /***************************************************************************************************************
         *
         *                               如果有多个or(s)  会很混乱 一回头 自己就读不懂了  实际开发中不要用多个or(s)
         *                               如果确实需要应该这样 要用多个ConfigStore明确分好组 类似加上()
         *
         * *************************************************************************************************************/

        ConfigStore c = new DefaultConfigStore();
        ConfigStore c1 = new DefaultConfigStore();
        ConfigStore c2 = new DefaultConfigStore();
        c1.and("ID", "1").or("ID", "2");
        c2.and("ID", "11").or("ID", "22").or("ID", "33");
        c.and(c1).and(c2);
        service.query("crm_user", c);
        //WHERE(ID = 1 OR  ID = 2) AND (ID = 11 OR  ID = 22 OR ID = 33)



        c = new DefaultConfigStore();
        c1 = new DefaultConfigStore();
        c2 = new DefaultConfigStore();
        c1.and("ID", "1").and("ID", "2");
        c2.and("ID", "11").and("ID", "22").and("ID","33");
        c.and(c1).or(c2);
        service.query("crm_user", c);
        //WHERE(ID = 1 AND  ID = 2) OR (ID = 11 AND ID = 22 AND ID = 33)
    }
}

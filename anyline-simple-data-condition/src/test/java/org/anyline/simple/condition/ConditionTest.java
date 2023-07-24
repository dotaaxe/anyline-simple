package org.anyline.simple.condition;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.Compare;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ConditionApplication.class)
public class ConditionTest {
    @Autowired
    private AnylineService service          ;

    @Test
    public void over(){
        ConfigStore configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.and("ID","2");
        configs.and("ID","3");
        service.query("crm_user", configs);
        //WHERE(ID = 1 AND  ID = 2 AND ID = 3)

        // overCondition 覆盖key相同并且运行符相同的条件,true在现有条件基础上修改(多个相同key的条件只留下第一个),false:添加新条件
        // overValue	 覆盖key相同并且运行符相同的条件时，是否覆盖条件值,如果不覆盖则与原来的值合成新的集合
        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.and("ID", "2");
        configs.and("ID","3", true, true);
        service.query("crm_user", configs);
        //WHERE(ID = 3)
        //因为最后一步要覆盖相同的条件,也就是相同的条件中只留下第一个，并且用3覆盖原有的值（这里留哪个没影响，因为他的值最后都会被3覆盖）

        configs = new DefaultConfigStore();
        configs.and("ID", "1");
        configs.and("ID", "2");
        configs.and("ID","3", true, false);
        service.query("crm_user", configs);
        //WHERE(ID = 1)
        //因为最后一步要覆盖相同的条件，也就是相同的条件中只留下第一个，但并不覆盖值，所以并没有用3覆盖1


        configs = new DefaultConfigStore();
        configs.and("ID", "1,2".split(","));
        service.query("crm_user", configs);
        //WHERE ID IN(1,2)

        configs = new DefaultConfigStore();
        configs.and("ID", "1,2".split(",")); //这里会生成like
        configs.and("ID","3", true, true); //因为运算符不一致所以不会覆盖
        service.query("crm_user", configs);
        //WHERE(ID IN (1,2) AND  ID = 3)

        configs = new DefaultConfigStore();
        configs.and("ID", "1,2".split(",")); //这里会生成like
        configs.and(Compare.IN, "ID","3", true, true); //因为KEY和运算符都一致所以会覆盖
        service.query("crm_user", configs);
        //WHERE(ID IN (3))


        /***************************************************************************************************************
         *
         *                                       注意这里的 overValue = false
         *                                       虽然不会覆盖值，
         *                                       因为运算符IN可以接收多个值
         *                                       所有会在原来的基础上追加形成[1,2,3]
         *
         * ************************************************************************************************************/
        configs = new DefaultConfigStore();
        configs.and("ID", "1,2".split(",")); //这里会生成like
        configs.and(Compare.IN, "ID","3", true, false);
        service.query("crm_user", configs);
        //WHERE(ID IN (1,2,3))
    }
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

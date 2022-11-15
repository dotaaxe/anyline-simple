package org.anyline.simple.entity;


import com.sun.xml.internal.bind.v2.TODO;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataSet;
import org.anyline.entity.EntitySet;
import org.anyline.entity.PageNavi;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@SpringBootApplication
public class EntityApplication {
    private static AnylineService service;

    /* *********************************************************************
     *
     * condition 为方便示例的临时方法，在实际项目中 AnylineController会提供实现
     *
     ******************************************************************** */
    public static ConfigStore condition(boolean navi, String ... conditions){
        return new DefaultConfigStore();
    }
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(EntityApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService)context.getBean("anyline.service");
        run();
        xml();
        sql();
        System.exit(0);
    }
    //更多XML定义SQL参考  anyline-simple-data-xml
    public static void xml(){
        String sql = "crm.user:USER_LIST";
        Employee e = ServiceProxy.select(sql, Employee.class );
        System.out.println(BeanUtil.object2json(e));
    }
    public static void sql(){
        String sql = "SELECT * FROM HR_EMPLOYEE WHERE CODE = ':CODE' AND ID IN (:ID) AND CODE > ':CODE'";
        //url   http://127.0.0.1?name=张
        Employee e = ServiceProxy.select(sql, Employee.class , condition(true,"NAME:%name%")
                .addCondition("CODE","123")
                .addConditions("ID","1","2")
                .addCondition("DATA_STATUS", 1)
        );
        /*合成SQL

        SELECT * FROM HR_EMPLOYEE WHERE CODE = '123'
        AND ID IN (?,?)
        AND CODE > '123'
        AND DATA_STATUS = ?
        LIMIT 0,1
        */
        System.out.println(BeanUtil.object2json(e));
    }
    public static void run(){
        DataSet set = service.querys("HR_EMPLOYEE",0,9);
        System.out.println(set.toJSON());

        PageNavi navi = new DefaultPageNavi();
        navi.setPageRows(10);
        EntitySet<Employee> list = service.selects(Employee.class, navi,"ORDER BY ID DESC");
        System.out.println(BeanUtil.object2json(list));
        System.out.println(BeanUtil.object2json(list.getNavi()));

/*
        AnylineService<Employee> s = null;
        Employee e = s.get("");
        e = s.query(Employee.class);*/

        //这里需要转换类型
        Employee e = (Employee) service.select(Employee.class);

        //也可以通过静态代理类,AnylineProxy可以代理AnylineService的一切操作并且是静态方法
        //ServiceProxy不需要注入直接调用静态方法,方法签名参考AnylineService
        e = ServiceProxy.select(Employee.class);
        e = new Employee();
        e.setCreateTime(DateUtil.format());
        service.insert(e);
        e.setCreateTime(null);
        e.setId(1L);
        service.update(e);

        Employee employee = list.get(0);
        employee.setAge(100);
        service.save(employee);
        employee = new Employee();
        employee.setName("test");
        employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
        service.save(employee);
        System.out.println(BeanUtil.object2json(employee));

        list = new EntitySet<>();
        for(int i=0; i<3;i++) {
            employee = new Employee();
            employee.setName("test-"+i);
            employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
            list.add(employee);
        }
        service.save(list);
        System.out.println(BeanUtil.object2json(list));

        service.save(list);
        System.out.println(BeanUtil.object2json(list));

        employee.setId(123L);
        service.delete(employee);
        service.delete(list);

        list = new EntitySet<>();
        for(int i=0; i<3;i++) {
            employee = new Employee();
            employee.setName("test-"+i);
            employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
            list.add(employee);
        }
        service.insert(list);
        System.out.println(BeanUtil.object2json(list));
    }

}

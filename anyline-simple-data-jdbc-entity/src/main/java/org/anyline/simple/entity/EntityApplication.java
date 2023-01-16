package org.anyline.simple.entity;


import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.*;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.Base64Util;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"org.anyline"})
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


    public static void main(String[] args) throws Exception{
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        SpringApplication application = new SpringApplication(EntityApplication.class);
       ConfigurableApplicationContext context = application.run(args);
       service = (AnylineService)context.getBean("anyline.service");

        //blob();
        //json();
        run();
        //xml();
        //sql();
        System.exit(0);
    }
    public static void json(){
        Employee employee = ServiceProxy.select(Employee.class);
        //employee的department属性是Department类型
        //这里会输出{"id":1,"name":"张三","department":{"code":"A1","name":"财务部"}, "departments":[{"code":"A1","name":"财务部"}]}
        System.out.println(BeanUtil.object2json(employee));
        employee.setDjson("{\"code\":\"A1\",\"name\":\"财务部\"}");
        ServiceProxy.save(employee);


        Department dept = new Department();
        dept.setCode("A1");
        dept.setName("财务部");
        employee.setName("张三");
        //属性是entity类型的 数据是json类型
        employee.setEjson(dept);
        //这里会执行SQL UPDATE hr_employee SET department ={"code":"A1","name":"财务部"}(java.lang.String)
        ServiceProxy.save(employee);

        //DataRow 与Entity效果类似
        DataRow emp = ServiceProxy.query("hr_employee");
        System.out.println(emp.toJSON());

        DataSet emps = ServiceProxy.querys("hr_employee");
        System.out.println(emps.toJSON());

        emp.put("department", dept);
        //也可以通过String类型的json赋值
        emp.put("department", BeanUtil.object2json(dept));
        System.out.println(emp.toJSON());
        service.save(emp);
    }
    public static void blob()throws Exception{
        Employee employee = ServiceProxy.select(Employee.class);

        //数据库>blob entity>byte[]
        employee.setRemark("abcd".getBytes());

        //数据库>blob ,  entity>String
        //如果要把String保存到blob列,需要把String执行base64编码
        //因为从数据库中读取出来的blob需要经过base64(str)后赋值给了属性
        //再保存回去时如果不经过base64会造成 sql执行时不确定当前值是否是base64格式,(如果是base64需要执行decode,如果是String原文需要执行str.getBytes())
        //如 "abcd" 符合base64格式 这时无法判断如何处理
        //如果确定值是不符合base64格式的可以直接用原文,sql执行前会先执行base64

        //这样会造成歧义
        employee.setDblob("abcd");

        //应该这样
        employee.setDblob(Base64Util.encode("abcd"));


        //如果确定不规格base64格式的可以直接用原文,sql执行前会先执行base64
        employee.setDblob(Base64Util.encode("abcd中文123"));

        service.save(employee);
        employee = ServiceProxy.select(Employee.class);
        //这时查出来的des是经过base64后的内容如果要显示原文需要经过base解码
        System.out.println(BeanUtil.object2json(employee));
        System.out.println(new String(Base64Util.decode(employee.getDblob())));
        DataRow e = service.query("HR_EMPLOYEE");
        System.out.println(e.toJSON());

        //因为DataRow中没有类型限制所以 按原样(byte[])取出
        System.out.println(new String(e.getBytes("des")));


        //为什么不直接把str.getBytes()保存到blob
        //因为这样的话 在显示时 需要new String(byte[])来还原 原文
        //但有些情况下blob中可能不是String而是image,这时一般需要显示base64(image)
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
                .and("CODE","123")
                .ands("ID","1","2")
                .and("DATA_STATUS", 1)
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

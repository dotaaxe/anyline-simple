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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


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
        //ConfigTable.IS_AUTO_CHECK_METADATA = true;
        SpringApplication application = new SpringApplication(EntityApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService)context.getBean("anyline.service");
        init();
        json();
        test();
        point();
        blob();
        xml();
        sql();
        empty();
        camel();

        System.exit(0);
    }

    public static void init() throws Exception{
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        org.anyline.data.entity.Table table = service.metadata().table("HR_EMPLOYEE");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new org.anyline.data.entity.Table("HR_EMPLOYEE");
        //注意以下数据类型
        table.addColumn("ID"            , "BIGINT").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ); // String          :nm            姓名
        table.addColumn("CODE"          , "varchar(50)"  ); // String          :workCode      工号
        table.addColumn("BIRTHDAY"      , "DATE"         ); // Date            :birthday      出生日期
        table.addColumn("JOIN_YMD"      , "varchar(10)"  ); // String          :joinYmd       入职日期
        table.addColumn("REMARK"        , "blob"         ); // byte[]          :remark        备注
        table.addColumn("DESCRIPTION"   , "blob"         ); // String          :description   详细信息
        table.addColumn("DEPARTMENT"    , "json"         ); // Department      :department    部门
        table.addColumn("EXPERIENCES"   , "json"         ); // List<Experience>:experiences   工作经历
        table.addColumn("OTHER"         , "json"         ); // Object          :other         其他信息
        table.addColumn("WORK_LOCATION" , "point"        ); // Double[]        :workLocation  工作定位
        table.addColumn("HOME_LOCATION" , "point"        ); // Point           :homeLocation  家定位

        service.ddl().create(table);

        Employee em = new Employee();
        em.setNm("张三");
        em.setAge(30);
        em.setWorkCode("A10");
        em.setBirthday(LocalDate.of(2001,12,10));
        em.setJoinYmd("2020-01-01");
        em.setRemark("张三备注".getBytes());
        em.setDescription("张三详细信息");
        em.setDepartment(new Department("DF01", "财务一部"));
        List<Experience> experiences = new ArrayList<>();
        experiences.add(new Experience( 1, DateUtil.parse("2020-01-01"),  DateUtil.parse("2022-02-02"), "前台", "中国银行"));
        experiences.add(new Experience( 1, DateUtil.parse("2022-02-03"),  DateUtil.parse("2023-02-05"), "财务", "交通银行"));
        em.setExperiences(experiences);
        em.setOther("{\"爱好\":\"跑步\",\"籍贯\":\"山东\"}");
        em.setWorkLocation(new Double[]{121.0,35.0});
        em.setHomeLocation(new Point(120.10, 36.12));

        service.save("HR_EMPLOYEE", em);

        Employee employee = ServiceProxy.select(Employee.class);

        //当前类上没有表注解但父类上有 保存时取父类注解值
        ChildEntity child = new ChildEntity();
        child.setNm("张三");
        service.insert(child);

        DataSet set = service.querys("HR_EMPLOYEE",0,9);
        System.out.println(set.toJSON());

        PageNavi navi = new DefaultPageNavi();
        navi.setPageRows(10);
        EntitySet<Employee> list = service.selects(Employee.class, navi,"ORDER BY ID DESC");
        System.out.println(BeanUtil.object2json(list));
        System.out.println(BeanUtil.object2json(list.getNavi()));

        String sql = "SELECT * FROM hr_employee";
        list = service.selects(sql, Employee.class);
/*
        AnylineService<Employee> s = null;
        Employee e = s.get("");
        e = s.query(Employee.class);
        */

        //这里需要转换类型
        Employee e = (Employee) service.select(Employee.class);
        //更新全部列
        service.save(e);

        //修改部分列
        e.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
        service.update(e, "join_ymd");

        //也可以通过静态代理类,AnylineProxy可以代理AnylineService的一切操作并且是静态方法
        //ServiceProxy不需要注入直接调用静态方法,方法签名参考AnylineService
        e = ServiceProxy.select(Employee.class);
        e = new Employee();
        e.setCreateTime(DateUtil.format());
        service.insert(e);
        e.setCreateTime(null);
        e.setId(1L);
        // e.setName("test");
        service.update(e);

        employee = list.get(0);
        employee.setAge(100);
        service.save(employee);
        employee = new Employee();
        //employee.setName("test");
        employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
        service.save(employee);
        System.out.println(BeanUtil.object2json(employee));

        list = new EntitySet<>();
        for(int i=0; i<3;i++) {
            employee = new Employee();
            //employee.setName("test-"+i);
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
            // employee.setName("test-"+i);
            employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
            list.add(employee);
        }
        service.insert(list);
        System.out.println(BeanUtil.object2json(list));
    }
    public static void test(){
        DataRow t = service.query("sync_task(max(id))");
        SyncTask task = ServiceProxy.select(SyncTask.class);
        task = (SyncTask)service.select("sync_task", SyncTask.class, "id:1");
        task = new SyncTask();
        task.setId(1L);
        task.setLastExeQty(123L);
        DataRow row = DataRow.parse(task).camel_();
        System.out.println(row);
        task.setCols("NULL");
        service.update(task);
        service.update(task, "LAST_EXE_QTY");
        service.update("sync_task", task, "LAST_EXE_QTY");
    }
    public static void point(){
        //ConfigTable.IS_AUTO_SPLIT_ARRAY = false;
        Employee e =  ServiceProxy.select(Employee.class, "LOC IS NOT NULL");
        BeanUtil.setFieldValue(e, "ymd", DateUtil.parse("2020-01-01"));
        System.out.println(BeanUtil.object2json(e));
        //System.out.println(BeanUtil.concat(e.getLoc()));
        service.save(e);

        DataRow row = service.query("HR_EMPLOYEE", "LOC IS NOT NULL");
        System.out.println(row);
        service.save(row);

    }
    public static  void camel(){

        Employee e = new Employee();
       // e.setName("A1");
       // e.setUserName("zh");
        e.setJoinYmd("2020-01-01");
        service.save(e);
        List<Employee> list = new ArrayList<>();
        for(int i=0; i<3; i++){
            Employee item = new Employee();
           // item.setName("A"+i);
            //item.setUserName("zh"+i);
            item.setJoinYmd("2020-01-01");
            list.add(item);
        }
        //批量insert时不检测空值
        service.insert(list);
    }
    public static void empty(){
        ConfigTable.IS_UPDATE_NULL_FIELD = false;
        ConfigTable.IS_INSERT_NULL_FIELD = false;
        Employee e = new Employee();
        //e.setName("A1");
        service.save(e);
       // e.setName("A2");
        service.save(e);

        //会插入和更新null与""的值
        //如果属性上没有注解 会根据 ConfigTable.ENTITY_FIELD_COLUMN_MAP进程转换;
        //默认"camel_"属性小驼峰转下划线 userName > USER_NAME
        ConfigTable.IS_UPDATE_NULL_FIELD = true;
        ConfigTable.IS_INSERT_NULL_FIELD = true;
        ConfigTable.IS_UPPER_KEY = false;
        ConfigTable.ENTITY_FIELD_COLUMN_MAP = "camel_";
        e = new Employee();
       // e.setUserName("root");
       // e.setName("A1");
        service.save(e);
        //e.setName("A2");
        service.save(e);


    }
    public static void json(){
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        Employee employee = new Employee();
       // employee.setName("A");
        employee.setTmpCol("111"); //数据库中没有这一列,开启了IS_AUTO_CHECK_METADATA后 可以把不存在的列过滤掉

        ServiceProxy.save(employee);
        employee = ServiceProxy.select(Employee.class);
        //employee的department属性是Department类型
        //这里会输出{"id":1,"name":"张三","department":{"code":"A1","name":"财务部"}, "departments":[{"code":"A1","name":"财务部"}]}
        System.out.println(BeanUtil.object2json(employee));
        //employee.setDjson("{\"code\":\"A1\",\"name\":\"财务部\"}");
        employee.setTmpCol("123");
        ServiceProxy.save(employee);
        employee = ServiceProxy.select(Employee.class);

        Department dept = new Department();
        dept.setCode("A1");
        dept.setName("财务部");
       // employee.setName("张三");
        //属性是entity类型的 数据是json类型
        //employee.setEjson(dept);
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

        //新版本不要base64，不需要兼容 entity.String : db.img   这样的应该用 byte[]接收
        //数据库>blob ,  entity>String
        //如果要把String保存到blob列,需要把String执行base64编码
        //因为从数据库中读取出来的blob需要经过base64(str)后赋值给了属性
        //再保存回去时如果不经过base64会造成 sql执行时不确定当前值是否是base64格式,(如果是base64需要执行decode,如果是String原文需要执行str.getBytes())
        //如 "abcd" 符合base64格式 这时无法判断如何处理
        //如果确定值是不符合base64格式的可以直接用原文,sql执行前会先执行base64

        //这样会造成歧义
        //employee.setDblob("abcd");

        //应该这样
        //employee.setDblob(Base64Util.encode("abcd"));


        //如果确定不规格base64格式的可以直接用原文,sql执行前会先执行base64
       // employee.setDblob(Base64Util.encode("abcd中文123"));

        service.save(employee);
        employee = ServiceProxy.select(Employee.class);
        //这时查出来的des是经过base64后的内容如果要显示原文需要经过base解码
        System.out.println(BeanUtil.object2json(employee));
       // System.out.println(new String(Base64Util.decode(employee.getDblob())));
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


}

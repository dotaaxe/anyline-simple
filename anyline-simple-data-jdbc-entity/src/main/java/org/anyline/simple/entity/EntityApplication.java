package org.anyline.simple.entity;


import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.*;
import org.anyline.entity.geometry.Point;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


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
        ConfigTable.IS_PRINT_EXCEPTION_STACK_TRACE = true;
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        SpringApplication application = new SpringApplication(EntityApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        service = (AnylineService)context.getBean("anyline.service");
        test1();
        init();
        test();
        dependency();
        sql();
        json();
        test();
        point();
        blob();
        xml();
        empty();
        camel();
        metadata();
        System.exit(0);
    }
    public static void test1(){
        Employee em = new Employee();
        //头衔
        List<String> titles = new ArrayList<>();
        titles.add("先进工作者");
        titles.add("劳动模范");
        em.setTitles(titles);
        em.setCtitles(titles);
        //标签
        String[] labels = new String[]{"好人","工作积极"};
        em.setLabels(labels);
        em.setClabels(labels);
        service.insert(em);
    }
    public static void metadata(){
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        String sql = "SELECT * FROM HR_EMPLOYEE";
        //对于直接提供SQL的情况,不根据系统表检测metadata
        Employee e = ServiceProxy.select(sql, Employee.class);
        System.out.println(BeanUtil.object2json(e));
    }

    public static void init() throws Exception{
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        //职员表
        org.anyline.metadata.Table table = service.metadata().table("HR_EMPLOYEE");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new org.anyline.metadata.Table("HR_EMPLOYEE");
        //注意以下数据类型
        table.addColumn("ID"            , "BIGINT"       ).setComment("主键").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ).setComment("姓名")     ; // String          : nm
        table.addColumn("CODE"          , "varchar(50)"  ).setComment("工号")     ; // String          : workCode
        table.addColumn("BIRTHDAY"      , "DATE"         ).setComment("出生日期")  ; // Date            : birthday
        table.addColumn("JOIN_YMD"      , "varchar(10)"  ).setComment("入职日期")  ; // String          : joinYmd
        table.addColumn("SALARY"        , "float(10,2)"  ).setComment("工资")     ; // float           : salary
        table.addColumn("REMARK"        , "blob"         ).setComment("备注")     ; // byte[]          : remark
        table.addColumn("DESCRIPTION"   , "blob"         ).setComment("详细信息")  ; // String          : description
        table.addColumn("DEPARTMENT"    , "json"         ).setComment("部门")     ; // Department      : department
        table.addColumn("POSTS"         , "json"         ).setComment("职务s")    ; // Map<String,Post>: posts
        table.addColumn("EXPERIENCES"   , "json"         ).setComment("工作经历")  ; // List<Experience>: experiences
        table.addColumn("TITLES"        , "json"         ).setComment("头衔s")    ; // List<String>    : titles
        table.addColumn("LABELS"        , "json"         ).setComment("标签s")    ; // String[]        : labels
        table.addColumn("SCORES"        , "json"         ).setComment("成绩s")    ; // int[]           : scores
        table.addColumn("CTITLES"       , "varchar(255)" ).setComment("头衔s")    ; // List<String>    : ctitles
        table.addColumn("CLABELS"       , "varchar(255)" ).setComment("标签s")    ; // String[]        : clabels
        table.addColumn("CSCORES"       , "varchar(255)" ).setComment("成绩s")    ; // int[]           : cscores
        table.addColumn("OTHER"         , "json"         ).setComment("其他信息")  ; // Object          : other
        table.addColumn("MAP"           , "json"         ).setComment("其他信息")  ; // Map             : map
        table.addColumn("WORK_LOCATION" , "point"        ).setComment("工作定位")  ; // Double[]        : workLocation
        table.addColumn("HOME_LOCATION" , "point"        ).setComment("住址定准")  ; // Point           : homeLocation*/
        table.addColumn("LOCAL_TIME", "time");
        table.addColumn("DATA_STATUS"   , "int"          ).setComment("状态").setDefaultValue(1);
        //注意SQL Server中一个表只能有一列TIMESTAMP
        table.addColumn("CREATE_TIME"   , "TIMESTAMP"    ).setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("REG_TIME"      , "TIMESTAMP"    ).setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        service.ddl().create(table);

        //部门表
        table = service.metadata().table("HR_DEPARTMENT");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new org.anyline.metadata.Table("HR_DEPARTMENT");
        table.addColumn("ID"            , "BIGINT").setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("NAME"          , "varchar(50)"  ); // String          : name      名称
        table.addColumn("CODE"          , "varchar(50)"  ); // String          : code      编号
        table.addColumn("local_time", "time");
        service.ddl().create(table);

        //职员部门关系表
        table = service.metadata().table("HR_EMPLOYEE_DEPARTMENT");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new org.anyline.metadata.Table("HR_EMPLOYEE_DEPARTMENT");
        table.addColumn("ID"            , "BIGINT"  ).setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("EMPLOYEE_ID"   , "BIGINT"  ); // LONG          : employeeId        职员ID
        table.addColumn("DEPARTMENT_ID" , "BIGINT"  ); // LONG          : departmentId      部门ID
        service.ddl().create(table);

        //考勤记录
        table = service.metadata().table("HR_ATTENDANCE_RECORD");
        if(null != table){
            service.ddl().drop(table);
        }
        table = new org.anyline.metadata.Table("HR_ATTENDANCE_RECORD");
        table.addColumn("ID"            , "BIGINT"  ).setAutoIncrement(true).setPrimaryKey(true);
        table.addColumn("EMPLOYEE_ID"   , "BIGINT"  ); // LONG          : employeeId        职员ID
        table.addColumn("RECORD_TIME" , "DATETIME"  ); // LONG          : date      考勤时间
        service.ddl().create(table);
        //部门
        service.insert(new Department("FI01","财务部").setId(112L).setLocalTime(LocalTime.of(10,12)));
        service.insert(new Department("HR01","人力资源部").setLocalTime(LocalTime.of(10,12)));
        service.insert(new Department("PP01","生产一部").setLocalTime(LocalTime.of(10,12)));
        service.insert(new Department("PP02","生产二部").setLocalTime(LocalTime.of(10,12)));
        service.insert(new Department("QC01","质量组").setLocalTime(LocalTime.of(10,12)));

        Employee em = new Employee("张三");
        em.setAge(30);
        em.setWorkCode("A10");
        em.setBirthday(LocalDate.of(2001,12,10));
        em.setJoinYmd("2020-01-01");
        em.setRemark("张三备注".getBytes());
        em.setDescription("张三详细信息");
        em.setDepartment(new Department("DF01", "财务一部"));
        em.setSalary(10000.12f);

        //工作经历
        List<Experience> experiences = new ArrayList<>();
        experiences.add(new Experience( 1, DateUtil.parse("2020-01-01"),  DateUtil.parse("2022-02-02"), "前台", "中国银行"));
        experiences.add(new Experience( 1, DateUtil.parse("2022-02-03"),  DateUtil.parse("2023-02-05"), "财务", "交通银行"));
        em.setExperiences(experiences);

        //职务
        Map<String,Post> posts = new HashMap<>();
        posts.put("财务经理", new Post("财务经理", 100.11, DateUtil.parse("2010-01-01")));
        posts.put("市场部主任", new Post("市场部主任", 200.11, DateUtil.parse("2020-02-02")));
        em.setPosts(posts);
        //头衔
        List<String> titles = new ArrayList<>();
        titles.add("先进工作者");
        titles.add("劳动模范");
        em.setTitles(titles);
        em.setCtitles(titles);
        //标签
        String[] labels = new String[]{"好人","工作积极"};
        em.setLabels(labels);
        em.setClabels(labels);
        //成绩
        int[] scores = new int[]{95,98};
        em.setScores(scores);
        em.setCscores(scores);

        //其他信息
        em.setOther("{\"爱好\":\"跑步\",\"籍贯\":\"山东\"}");
        Map<String,String> m = new HashMap<>();
        m.put("name","zhang");
        JSONObject json = new JSONObject(m);
        em.setOther(json);
        //工作地
        em.setWorkLocation(new Double[]{121.0,35.0});
        //家庭住址
        em.setHomeLocation(new Point(120.10, 36.12));
        Map map = new HashMap();
        map.put("专业","计算机");
        map.put("学历","本科");
        em.setMap(map);

        //所在部门IDS
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(1L);
        deptIds.add(2L);
        em.setDepartmentIds(deptIds);

        //所在部门S
        List<Department> depts = new ArrayList<>();
        depts.add(new Department(1L, "FI01", "财务部"));
        depts.add(new Department(2L));
        depts.add(new Department(3L));
        em.setDepartments(depts);

        //考勤记录
        List<AttendanceRecord> records = new ArrayList<>();
        records.add(new AttendanceRecord(new Date()));
        records.add(new AttendanceRecord(DateUtil.addDay(-1)));
        records.add(new AttendanceRecord(DateUtil.addDay(-2)));
        em.setRecords(records);

        ConfigTable.ENTITY_FIELD_INSERT_DEPENDENCY = 1;
        service.save("HR_EMPLOYEE", em); //insert
        service.save("HR_EMPLOYEE", em); //update
        em = ServiceProxy.select(Employee.class);
        System.out.println(BeanUtil.object2json(em));
        em = new Employee();
        em.setNm("李四");
        service.save("HR_EMPLOYEE", em);

        //职员 部门关系(与上面的ManyToMany作用一样)
        service.insert("HR_EMPLOYEE_DEPARTMENT", employeeDepartment(2,2));
        service.insert("HR_EMPLOYEE_DEPARTMENT", employeeDepartment(2,3));
    }
    public static DataRow employeeDepartment(int employee, int department){
        DataRow row = new DataRow();
        row.put("EMPLOYEE_ID", employee);
        row.put("DEPARTMENT_ID", department);
        return row;
    }
    public static void dependency(){
        /***************** 依赖查询 **********************/
        ConfigTable.ENTITY_FIELD_SELECT_DEPENDENCY = 3;
        ConfigTable.ENTITY_FIELD_SELECT_DEPENDENCY_COMPARE = Compare.IN;

        Employee employee = ServiceProxy.select(Employee.class);
        System.out.println(BeanUtil.object2json(employee));
        EntitySet<Employee> es = service.selects(Employee.class);
        System.out.println(BeanUtil.object2json(es));


        /***************** 删除 **********************/
        ConfigTable.ENTITY_FIELD_DELETE_DEPENDENCY = 1;
        service.delete(employee);

    }
    public static void test(){

        //当前类上没有表注解但父类上有 保存时取父类注解值
        ChildEntity child = new ChildEntity();
        child.setName("张三");
        service.insert(child);

        Department dept = new Department();
        dept.setName("财务部");
        service.insert(dept);

        DataSet set = service.querys("HR_EMPLOYEE",0,9);
        System.out.println(set.toJSON());

        PageNavi navi = new DefaultPageNavi();
        navi.setPageRows(10);
        EntitySet<Employee> list = service.selects(Employee.class, navi,"ORDER BY ID DESC");
        System.out.println(BeanUtil.object2json(list));
        System.out.println(BeanUtil.object2json(list.getNavi()));

        String sql = "SELECT * FROM HR_EMPLOYEE";
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
        e.setNm("test");
        service.update(e);

        Employee employee = list.get(0);
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
        System.out.println(BeanUtil.object2json(list));
        service.delete(list);

        list = new EntitySet<>();
        for(int i=0; i<1;i++) {
            employee = new Employee();
            // employee.setName("test-"+i);
            employee.setJoinYmd(DateUtil.format("yyyy-MM-dd"));
            list.add(employee);
        }
        service.insert(list);
        System.out.println(BeanUtil.object2json(list));
    }
    public static void point(){
        //是否把数组集合参数拆开，默认需要拆开
        // 不需要拆开的情况DataRow 中保存了double[] 对应数据库中的Point 这种情况尽量不要用double而是用org.anyline.entity.Point,这样就可以默认IS_AUTO_SPLIT_ARRAY=true
        //ConfigTable.IS_AUTO_SPLIT_ARRAY = false;
        Employee e =  ServiceProxy.select(Employee.class, "WORK_LOCATION IS NOT NULL");
        BeanUtil.setFieldValue(e, "join_ymd", DateUtil.parse("2020-01-01"));
        System.out.println(BeanUtil.object2json(e));
        //System.out.println(BeanUtil.concat(e.getLoc()));
        service.save(e);

        DataRow row = service.query("HR_EMPLOYEE", "WORK_LOCATION IS NOT NULL");
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
        employee.setNm("A");
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
        //这里会执行SQL UPDATE HR_EMPLOYEE SET department ={"code":"A1","name":"财务部"}(java.lang.String)
        ServiceProxy.save(employee);

        //DataRow 与Entity效果类似
        DataRow emp = ServiceProxy.query("HR_EMPLOYEE");
        System.out.println(emp.toJSON());

        DataSet emps = ServiceProxy.querys("HR_EMPLOYEE");
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
        System.out.println(new String(e.getBytes("REMARK")));
        System.out.println(e.getString("REMARK"));


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
        //DataRow row = service.query("SELECT * FROM HR_EMPLOYEE");
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

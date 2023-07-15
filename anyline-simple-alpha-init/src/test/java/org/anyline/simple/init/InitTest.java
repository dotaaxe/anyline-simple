package org.anyline.simple.init;

import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InitTest {
    private Logger log = LoggerFactory.getLogger(InitTest.class);
    @Autowired
    private AnylineService service;

    @Test
    public void create() throws Exception{

        service.execute("DROP DATABASE IF EXISTS SIMPLE");
        service.execute("CREATE DATABASE IF NOT EXISTS SIMPLE");
        Table dict = service.metadata().table("SIMPLE", null,"BS_DICT");
        if(null != dict){
            service.ddl().drop(dict);
        }

        //也可以不用判断直接删除(SQL中会判断)
        service.ddl().drop(new Table("SIMPLE", null,"BS_DICT"));

        dict = new Table("SIMPLE", null,"BS_DICT");
        dict.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        dict.addColumn("CODE","varchar(50)");
        dict.addColumn("GROUP_CODE","varchar(50)");
        dict.addColumn("GROUP_NM","varchar(50)");
        dict.addColumn("GROUP_TITLE","varchar(50)");
        dict.addColumn("BASE_CODE","varchar(50)");
        dict.addColumn("NM","varchar(50)");
        dict.addColumn("VAL","varchar(255)");
        dict.addColumn("PARAM1","varchar(50)");
        dict.addColumn("PARAM2","varchar(50)");
        dict.addColumn("PARAM3","varchar(50)");
        dict.addColumn("PARAM4","varchar(50)");
        dict.addColumn("PARAM5","varchar(50)");
        common(dict);
        service.ddl().create(dict);
        dict = service.metadata().table("SIMPLE", null,"BS_DICT");
        Assertions.assertNotNull(dict);


        //部门
        Table dept = service.metadata().table("SIMPLE", null,"HR_DEPARTMENT");
        if(null != dept){
            service.ddl().drop(dept);
        }
        dept = new Table("SIMPLE", null,"hr_department");
        dept.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        dept.addColumn("CODE","varchar(50)");
        dept.addColumn("NM","varchar(50)");
        common(dept);
        service.ddl().create(dept);
        dept = service.metadata().table("SIMPLE", null,"HR_DEPARTMENT");
        Assertions.assertNotNull(dept);

        //人员
        Table employee = service.metadata().table("SIMPLE", null,"HR_EMPLOYEE");
        if(null != employee){
            service.ddl().drop(employee);
        }
        employee = new Table("SIMPLE", null,"HR_EMPLOYEE");
        employee.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        employee.addColumn("CODE","varchar(50)");
        employee.addColumn("NM","varchar(50)");
        employee.addColumn("SEX","bigint");
        employee.addColumn("BIRTHDAY","varchar(10)");
        employee.addColumn("JOIN_YMD","varchar(10)");
        employee.addColumn("DEPARTMENT_ID","bigint");
        common(employee);
        service.ddl().create(employee);
        employee = service.metadata().table("SIMPLE", null,"HR_EMPLOYEE");
        Assertions.assertNotNull(employee);

        //薪资
        Table salary = service.metadata().table("SIMPLE", null,"HR_SALARY");
        if(null != salary){
            service.ddl().drop(salary);
        }
        salary = new Table("SIMPLE", null,"HR_SALARY");
        salary.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        salary.addColumn("CODE","varchar(50)");
        salary.addColumn("EMPLOYEE_ID","bigint").setComment("职员ID");
        salary.addColumn("YYYY","int").setComment("年份");
        salary.addColumn("YM","varchar(10)").setComment("月份");
        salary.addColumn("BASE_PRICE","decimal(10,2)").setComment("基本薪资");
        salary.addColumn("REWARD_PRICE","decimal(10,2)").setComment("奖金");
        salary.addColumn("DEDUCT_PRICE","decimal(10,2)").setComment("扣除");
        common(salary);
        service.ddl().create(salary);
        salary = service.metadata().table("SIMPLE", null,"HR_SALARY");
        Assertions.assertNotNull(salary);

        //薪资(按分类/每个类别的薪资单独一行)
        salary = service.metadata().table("SIMPLE", null,"HR_SALARY_TYPE");
        if(null != salary){
            service.ddl().drop(salary);
        }
        salary = new Table("SIMPLE", null,"HR_SALARY_TYPE");
        salary.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        salary.addColumn("CODE","varchar(50)");
        salary.addColumn("EMPLOYEE_ID","bigint").setComment("职员ID");
        salary.addColumn("YYYY","int").setComment("年份");
        salary.addColumn("YM","varchar(10)").setComment("月份");
        salary.addColumn("TYPE_CODE", "varchar(10)").setComment("薪资类别");
        salary.addColumn("PRICE","decimal(10,2)").setComment("金额");
        common(salary);
        service.ddl().create(salary);
        salary = service.metadata().table("SIMPLE", null,"HR_SALARY_TYPE");
        Assertions.assertNotNull(salary);

        //数据源
        Table ds = service.metadata().table("SIMPLE", null,"BS_DATASOURCE");
        if(null != ds){
            service.ddl().drop(ds);
        }
        ds = new Table("SIMPLE", null,"BS_DATASOURCE");
        ds.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        ds.addColumn("CODE","varchar(50)");

        ds.addColumn("TITLE","varchar(50)").setComment("标题");
        ds.addColumn("DRIVER","varchar(200)").setComment("驱动");
        ds.addColumn("URL"," varchar(200)").setComment("连接");
        ds.addColumn("ACCOUNT","varchar(20)").setComment("帐号");
        ds.addColumn("PASSWORD","varchar(32)").setComment("密码");
        service.ddl().create(ds);
        ds = service.metadata().table("SIMPLE", null,"BS_DATASOURCE");
        Assertions.assertNotNull(ds);

        //视图
        service.execute("DROP VIEW IF EXISTS SIMPLE.V_HR_EMPLOYEE;");
        service.execute("CREATE VIEW SIMPLE.V_HR_EMPLOYEE AS\n" +
                "SELECT\n" +
                "    M.ID            AS ID               ,\n" +
                "    M.CODE          AS CODE             ,\n" +
                "    M.NM            AS NM               ,\n" +
                "    M.SEX           AS SEX              ,\n" +
                "    M.BIRTHDAY      AS BIRTHDAY         ,\n" +
                "    M.JOIN_YMD      AS JOIN_YMD         ,\n" +
                "    M.DEPARTMENT_ID AS DEPARTMENT_ID    ,\n" +
                "    FD.NM           AS DEPARTMENT_NM    ,\n" +
                "    M.DATA_STATUS   AS DATA_STATUS       \n" +
                "FROM SIMPLE.HR_EMPLOYEE AS M                    \n" +
                "LEFT JOIN SIMPLE.HR_DEPARTMENT AS FD            \n" +
                "   ON M.DEPARTMENT_ID = FD.ID;");

        service.execute("DROP VIEW IF EXISTS SIMPLE.V_HR_SALARY;");
        service.execute("CREATE VIEW SIMPLE.V_HR_SALARY AS\n" +
                "SELECT\n" +
                "    M.ID                       AS ID               ,\n" +
                "    M.CODE                     AS CODE             ,\n" +
                "    M.EMPLOYEE_ID              AS EMPLOYEE_ID      ,\n" +
                "    FE.CODE                    AS EMPLOYEE_CODE    ,\n" +
                "    FE.NM                      AS EMPLOYEE_NM      ,\n" +
                "    FE.DEPARTMENT_ID           AS DEPARTMENT_ID    ,\n" +
                "    FE.DEPARTMENT_NM           AS DEPARTMENT_NM    ,\n" +
                "    M.YYYY                     AS YYYY             ,\n" +
                "    M.YM                       AS YM               ,\n" +
                "    M.BASE_PRICE               AS BASE_PRICE       ,\n" +
                "    M.REWARD_PRICE             AS REWARD_PRICE     ,\n" +
                "    M.DEDUCT_PRICE             AS DEDUCT_PRICE     ,\n" +
                "    IFNULL(M.BASE_PRICE,0)                          \n" +
                "        +IFNULL(M.REWARD_PRICE,0)                   \n" +
                "        -IFNULL(M.DEDUCT_PRICE,0)AS TOTAL_PRICE    ,\n" +
                "    M.DATA_STATUS          AS DATA_STATUS           \n" +
                "FROM SIMPLE.HR_SALARY AS M\n" +
                "LEFT JOIN SIMPLE.V_HR_EMPLOYEE AS FE\n" +
                "    ON M.EMPLOYEE_ID = FE.ID");
        service.execute("DROP VIEW IF EXISTS SIMPLE.V_HR_SALARY_TYPE;");
        service.execute("CREATE VIEW SIMPLE.V_HR_SALARY_TYPE AS\n" +
                "SELECT\n" +
                "    M.ID                       AS ID               ,\n" +
                "    M.CODE                     AS CODE             ,\n" +
                "    M.EMPLOYEE_ID              AS EMPLOYEE_ID      ,\n" +
                "    FE.CODE                    AS EMPLOYEE_CODE    ,\n" +
                "    FE.NM                      AS EMPLOYEE_NM      ,\n" +
                "    FE.DEPARTMENT_ID           AS DEPARTMENT_ID    ,\n" +
                "    FE.DEPARTMENT_NM           AS DEPARTMENT_NM    ,\n" +
                "    M.YYYY                     AS YYYY             ,\n" +
                "    M.YM                       AS YM               ,\n" +
                "    M.TYPE_CODE                AS TYPE_CODE        ,\n" +
                "    FT.NM                      AS TYPE_NM          ,\n" +
                "    M.PRICE                    AS PRICE            ,\n" +
                "    M.DATA_STATUS              AS DATA_STATUS       \n" +
                "FROM SIMPLE.HR_SALARY_TYPE AS M           \n" +
                "LEFT JOIN SIMPLE.V_HR_EMPLOYEE AS FE      \n" +
                "       ON M.EMPLOYEE_ID = FE.ID    \n" +
                "LEFT JOIN SIMPLE.bs_dict  AS FT           \n" +
                "       ON M.TYPE_CODE = FT.CODE AND FT.GROUP_CODE = 'SALARY_PRICE_TYPE'");

        //CRM库
        service.execute("DROP DATABASE IF EXISTS SIMPLE_CRM");
        service.execute("CREATE DATABASE IF NOT EXISTS SIMPLE_CRM");
        Table crm = service.metadata().table("SIMPLE_CRM", null, "CRM_CUSTOMER");
        if(null != crm){
            service.ddl().drop(crm);
        }
        crm = new Table("simple_crm", null, "CRM_CUSTOMER");
        crm.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        crm.addColumn("CODE","varchar(50)");
        crm.addColumn("NM", "varchar(50)");
        common(crm);
        service.ddl().create(crm);
        crm = service.metadata().table("simple_crm", null, "CRM_CUSTOMER");
        Assertions.assertNotNull(crm);

        //ERP库
        service.execute("DROP DATABASE IF EXISTS SIMPLE_ERP");
        service.execute("CREATE DATABASE IF NOT EXISTS SIMPLE_ERP");
        Table erp = service.metadata().table("SIMPLE_ERP", null, "MM_MATERIAL");
        if(null != erp){
            service.ddl().drop(erp);
        }
        erp = new Table("simple_erp", null, "MM_MATERIAL");
        erp.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        erp.addColumn("CODE","varchar(50)");
        erp.addColumn("NM", "varchar(50)");
        common(erp);
        service.ddl().create(erp);
        erp = service.metadata().table("simple_erp", null, "MM_MATERIAL");
        Assertions.assertNotNull(erp);

        //SSO库
        service.execute("DROP DATABASE IF EXISTS SIMPLE_SSO");
        service.execute("CREATE DATABASE IF NOT EXISTS SIMPLE_SSO");
        Table sso = service.metadata().table("SIMPLE_SSO", null, "SSO_USER");
        if(null != sso){
            service.ddl().drop(sso);
        }
        sso = new Table("simple_sso", null, "SSO_USER");
        sso.addColumn( "ID","bigint").setAutoIncrement(true).setPrimaryKey(true);
        sso.addColumn("CODE","varchar(50)");
        sso.addColumn("NM", "varchar(50)");
        common(sso);
        service.ddl().create(sso);
        sso = service.metadata().table("simple_sso", null, "SSO_USER");
        Assertions.assertNotNull(sso);
    }
    @Test
    public void data() throws Exception{
        DataSet set = new DataSet();
        DataRow row = new DataRow()
                .set("ID", 1)
                .set("CODE", "BASE")
                .set("GROUP_CODE","SALARY_PRICE_TYPE")
                .set("GROUP_NM","薪资金额类别")
                .set("NM", "底薪");
        set.add(row);

        row = new DataRow()
                .set("ID", 2)
                .set("CODE", "BASE")
                .set("GROUP_CODE","SALARY_PRICE_TYPE")
                .set("GROUP_NM","薪资金额类别")
                .set("NM", "奖金");
        set.add(row);

        row = new DataRow()
                .set("ID", 3)
                .set("CODE", "BASE")
                .set("GROUP_CODE","SALARY_PRICE_TYPE")
                .set("GROUP_NM","薪资金额类别")
                .set("NM", "扣除");
        set.add(row);

        service.insert("SIMPLE.BS_DICT", set);
        int size = service.count("SIMPLE.BS_DICT");
        Assertions.assertEquals(set.size(), size);

        //部门
        service.execute("INSERT INTO SIMPLE.HR_DEPARTMENT(ID,CODE,NM)VALUES(1,'D01','人事部'),(2,'D02','市场部'),(3,'D03','研发部')");
        size = service.count("SIMPLE.HR_DEPARTMENT");
        Assertions.assertEquals(size, 3);

        //人员
        service.execute("INSERT INTO SIMPLE.HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES" +
                "('1001','风清扬','1900-01-01',1,'2000-01-01',1),\n" +
                "('1002','步惊云','1910-01-01',1,'2000-02-01',1),\n" +
                "('1003','李寻欢','1920-01-01',1,'2000-02-01',1),\n" +
                "('1004','西门吹雪','1930-03-01',1,'2000-03-01',2),\n" +
                "('1005','练霓裳','1940-09-01',0,'2000-03-01',2),\n" +
                "('1006','丁春秋','1950-01-01',1,'2000-03-01',2),\n" +
                "('1007','邀月','1960-08-01',0,'2000-03-01',2),\n" +
                "('1008','王语嫣','1970-07-01',0,'2000-04-01',2),\n" +
                "('1009','上官无极','1980-06-01',1,'2000-04-01',2),\n" +
                "('1010','欧阳锋','1990-05-01',1,'2000-04-01',2),\n" +
                "('1011','黄药师','1930-05-01',1,'2000-05-01',2),\n" +
                "('1012','令狐冲','1930-04-01',1,'2000-05-01',2),\n" +
                "('1013','任我行','1930-04-01',1,'2000-05-01',2),\n" +
                "('1014','李莫愁','1940-03-01',0,'2000-06-01',2),\n" +
                "('1015','燕十三','1940-03-01',1,'2000-06-01',3),\n" +
                "('1016','燕南天','1950-02-01',1,'2000-06-01',3),\n" +
                "('1017','周芷若','1950-02-01',0,'2000-07-01',3),\n" +
                "('1018','慕容复','1903-02-01',1,'2000-07-01',3),\n" +
                "('1019','张无忌','1902-01-01',1,'2000-07-01',3),\n" +
                "('1020','小龙女','1901-01-01',0,'2000-08-01',3)");
        size = service.count("SIMPLE.HR_EMPLOYEE");
        Assertions.assertEquals(size, 20);


        service.execute("INSERT INTO simple_crm.CRM_CUSTOMER(CODE,NM)VALUES('1001','中国电信');");
        service.execute("INSERT INTO simple_erp.MM_MATERIAL(CODE,NM)VALUES('1001','合成丝雪纺A2');");
        service.execute("INSERT INTO simple_sso.SSO_USER(CODE,NM)VALUES ('1002','张三');");

    }
    private static void common(Table table){
        table.addColumn("IDX","varchar(50)").setComment("排序");
        table.addColumn("REMARK","varchar(50)").setComment("备注");
        table.addColumn("REG_ID","varchar(50)").setComment("注册人ID");
        table.addColumn("REG_IP","varchar(50)").setComment("注册IP");
        table.addColumn("REG_TIME","datetime").setComment("注册时间").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
        table.addColumn("REG_CLIENT_ID","varchar(50)").setComment("注册客户端ID");
        table.addColumn("UPT_ID","varchar(50)").setComment("最后修改人ID");
        table.addColumn("UPT_IP","varchar(50)").setComment("最后修改IP");
        table.addColumn("UPT_TIME","datetime").setComment("最后修改时间").setOnUpdate(true);
        table.addColumn("UPT_CLIENT_ID","varchar(50)").setComment("最后修改客户端ID");
        table.addColumn("DATA_STATUS","int").setComment("数据状态").setDefaultValue(1);
        table.addColumn("TENANT_CODE","varchar(50)").setComment("'租户CODE'").setDefaultValue(0);
        table.addColumn("ORG_CODE","varchar(50)").setComment("'组织CODE'").setDefaultValue(0);
        table.addColumn("DATA_VERSION","varchar(50)").setComment("'数据版本'").setDefaultValue(0);
    }
}

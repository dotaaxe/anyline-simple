DROP TABLE IF EXISTS bs_value;
CREATE TABLE bs_value  (
   ID               bigint(0) NOT NULL AUTO_INCREMENT,
   CODE             varchar(50) ,
   GROUP_CODE       varchar(50) ,
   GROUP_NM         varchar(50) ,
   GROUP_TITLE      varchar(50) ,
   BASE_CODE        varchar(50) ,
   NM               varchar(50) ,
   VAL              varchar(255) ,
   PARAM1           varchar(50) ,
   PARAM2           varchar(50) ,
   PARAM3           varchar(50) ,
   PARAM4           varchar(50) ,
   PARAM5           varchar(50) ,
   IDX              varchar(10) ,
   REMARK           varchar(500) ,
   REG_ID           varchar(50) ,
   REG_IP           varchar(20) ,
   REG_TIME         datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
   REG_CLIENT_ID    varchar(50) ,
   UPT_ID           varchar(50) ,
   UPT_IP           varchar(20) ,
   UPT_TIME         datetime(0) default NULL ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   UPT_CLIENT_ID    varchar(50) ,
   DATA_STATUS      int(0) NULL DEFAULT 1,
                           PRIMARY KEY (ID) USING BTREE
);
INSERT INTO bs_value VALUES (1, 'BASE', 'SALARY_PRICE_TYPE', '薪资金额类别', NULL, NULL, '底薪', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-12-08 13:43:54', NULL, NULL, NULL, '2021-12-08 13:43:54', NULL, 1);
INSERT INTO bs_value VALUES (2, 'REWARD', 'SALARY_PRICE_TYPE', NULL, NULL, NULL, '奖金', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-12-08 13:44:25', NULL, NULL, NULL, '2021-12-08 13:44:25', NULL, 1);
INSERT INTO bs_value VALUES (3, 'DEDUCT', 'SALARY_PRICE_TYPE', NULL, NULL, NULL, '扣除', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2021-12-08 13:44:26', NULL, NULL, NULL, '2021-12-08 13:44:26', NULL, 1);



drop table if exists HR_DEPARTMENT;

/*==============================================================*/
/* Table: 部门                                         */
/*==============================================================*/
create table HR_DEPARTMENT
(
    ID                   bigint not null auto_increment,
    CODE                 varchar(50),
    NM                   varchar(50),
    IDX                  nvarchar(10),
    REMARK               nvarchar(500),
    REG_ID               varchar(50),
    REG_IP               varchar(20),
   REG_TIME				datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
    REG_CLIENT_ID        varchar(50),
    UPT_ID               varchar(50),
    UPT_IP			     varchar(20),
   UPT_TIME         datetime(0) default NULL ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    UPT_CLIENT_ID        varchar(50),
    DATA_STATUS          int default 1,
    primary key (ID)
);

drop table if exists HR_EMPLOYEE;

/*==============================================================*/
/* Table: 职员                                           */
/*==============================================================*/
create table HR_EMPLOYEE
(
    ID                   bigint not null auto_increment,
    CODE                 varchar(50),
    NM                   varchar(50),
    SEX                  int,
    BIRTHDAY             varchar(10),
    JOIN_YMD             varchar(10),
    DEPARTMENT_ID        bigint,
    IDX                  nvarchar(10),
    REMARK               nvarchar(500),
    REG_ID               varchar(50),
    REG_IP               varchar(20),
    REG_TIME             datetime default CURRENT_TIMESTAMP,
    REG_CLIENT_ID        varchar(50),
    UPT_ID               varchar(50),
    UPT_IP               varchar(20),
   UPT_TIME         datetime(0) default NULL ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    UPT_CLIENT_ID        varchar(50),
    DATA_STATUS          int default 1,
    primary key (ID)
);

drop table if exists HR_SALARY;

/*==============================================================*/
/* Table: 薪资                                             */
/*==============================================================*/
create table HR_SALARY
(
    ID                   bigint not null auto_increment,
    CODE                 varchar(50),
    EMPLOYEE_ID          bigint,
    YYYY                 int,
    YM                   varchar(10),
    BASE_PRICE           decimal(10,2),
    REWARD_PRICE         decimal(10,2),
    DEDUCT_PRICE         decimal(10,2),
    IDX                  nvarchar(10),
    REMARK               nvarchar(500),
    REG_ID               varchar(50),
    REG_IP               varchar(20),
    REG_TIME             datetime default CURRENT_TIMESTAMP,
    REG_CLIENT_ID        varchar(50),
    UPT_ID               varchar(50),
    UPT_IP               varchar(20),
   UPT_TIME         datetime(0) default NULL ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    UPT_CLIENT_ID        varchar(50),
    DATA_STATUS          int default 1,
    primary key (ID)
);

DROP VIEW IF EXISTS V_HR_EMPLOYEE;
CREATE VIEW V_HR_EMPLOYEE AS
SELECT
    M.ID 			AS ID			    ,
    M.CODE			AS CODE			    ,
    M.NM            AS NM               ,
    M.SEX           AS SEX              ,
    M.BIRTHDAY      AS BIRTHDAY         ,
    M.JOIN_YMD      AS JOIN_YMD         ,
    M.DEPARTMENT_ID AS DEPARTMENT_ID    ,
    FD.NM           AS DEPARTMENT_NM    ,
    M.DATA_STATUS   AS DATA_STATUS
FROM HR_EMPLOYEE AS M
         LEFT JOIN HR_DEPARTMENT AS FD
                   ON M.DEPARTMENT_ID = FD.ID;

DROP VIEW IF EXISTS V_HR_SALARY;
CREATE VIEW V_HR_SALARY AS
SELECT
    M.ID 			    		AS ID			    ,
    M.CODE			    		AS CODE			    ,
    M.EMPLOYEE_ID       		AS EMPLOYEE_ID      ,
    FE.CODE                     AS EMPLOYEE_CODE    ,
    FE.NM               		AS EMPLOYEE_NM      ,
    FE.DEPARTMENT_ID    		AS DEPARTMENT_ID    ,
    FE.DEPARTMENT_NM    		AS DEPARTMENT_NM    ,
    M.YYYY                      AS YYYY             ,
    M.YM                		AS YM               ,
    M.BASE_PRICE        		AS BASE_PRICE       ,
    M.REWARD_PRICE      		AS REWARD_PRICE     ,
    M.DEDUCT_PRICE      		AS DEDUCT_PRICE     ,
    IFNULL(M.BASE_PRICE,0)
        +IFNULL(M.REWARD_PRICE,0)
        -IFNULL(M.DEDUCT_PRICE,0)	AS TOTAL_PRICE   	,
    M.DATA_STATUS       		AS DATA_STATUS
FROM HR_SALARY AS M
         LEFT JOIN V_HR_EMPLOYEE AS FE
                   ON M.EMPLOYEE_ID = FE.ID

;
drop table if exists HR_SALARY_TYPE;

/*==============================================================*/
/* Table: HR_SALARY_TYPE                                        */
/*==============================================================*/
create table HR_SALARY_TYPE
(
    ID                   bigint not null auto_increment comment 'ID',
    CODE                 varchar(50) comment 'CODE',
    EMPLOYEE_ID          bigint comment '职员ID',
    YYYY                 int comment '年度',
    YM                   varchar(10) comment '年月',
    TYPE_CODE            varchar(10) comment '类别',
    PRICE                decimal(10,2) comment '金额',
    IDX                  nvarchar(10) comment '排序',
    REMARK               nvarchar(500) comment '备注',
    REG_ID               varchar(50) comment '注册人',
    REG_IP               varchar(20) comment '注册IP',
    REG_TIME             datetime default CURRENT_TIMESTAMP comment '注册时间',
    REG_CLIENT_ID        varchar(50) comment '注冊端ID',
    UPT_ID               varchar(50) comment '修改人',
    UPT_IP               varchar(20) comment '修改人IP',
   UPT_TIME         datetime(0) default NULL ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    UPT_CLIENT_ID        varchar(50) comment '修改端ID',
    DATA_STATUS          int default 1 comment '活动状态',
    primary key (ID)
);
DROP VIEW IF EXISTS V_HR_SALARY_TYPE;
CREATE VIEW V_HR_SALARY_TYPE AS
SELECT
    M.ID 			    		AS ID			    ,
    M.CODE			    		AS CODE			    ,
    M.EMPLOYEE_ID       		AS EMPLOYEE_ID      ,
    FE.CODE                     AS EMPLOYEE_CODE    ,
    FE.NM               		AS EMPLOYEE_NM      ,
    FE.DEPARTMENT_ID    		AS DEPARTMENT_ID    ,
    FE.DEPARTMENT_NM    		AS DEPARTMENT_NM    ,
    M.YYYY                      AS YYYY             ,
    M.YM                		AS YM               ,
    M.TYPE_CODE                 AS TYPE_CODE        ,
    FT.NM                       AS TYPE_NM          ,
    M.PRICE        		        AS PRICE            ,
    M.DATA_STATUS       		AS DATA_STATUS
FROM HR_SALARY_TYPE AS M
         LEFT JOIN V_HR_EMPLOYEE AS FE
                   ON M.EMPLOYEE_ID = FE.ID
         LEFT JOIN BS_VALUE  AS FT
                   ON M.TYPE_CODE = FT.CODE AND FT.GROUP_CODE = 'SALARY_PRICE_TYPE'
;



    INSERT INTO HR_DEPARTMENT(ID,CODE,NM)VALUES(1,'D01','人事部'),(2,'D02','市场部'),(3,'D03','研发部');
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1001','风清扬','1900-01-01',1,'2000-01-01',1);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1002','步惊云','1910-01-01',1,'2000-02-01',1);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1003','李寻欢','1920-01-01',1,'2000-02-01',1);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1004','西门吹雪','1930-03-01',1,'2000-03-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1005','练霓裳','1940-09-01',0,'2000-03-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1006','丁春秋','1950-01-01',1,'2000-03-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1007','邀月','1960-08-01',0,'2000-03-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1008','王语嫣','1970-07-01',0,'2000-04-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1009','上官无极','1980-06-01',1,'2000-04-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1010','欧阳锋','1990-05-01',1,'2000-04-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1011','黄药师','1930-05-01',1,'2000-05-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1012','令狐冲','1930-04-01',1,'2000-05-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1013','任我行','1930-04-01',1,'2000-05-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1014','李莫愁','1940-03-01',0,'2000-06-01',2);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1015','燕十三','1940-03-01',1,'2000-06-01',3);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1016','燕南天','1950-02-01',1,'2000-06-01',3);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1017','周芷若','1950-02-01',0,'2000-07-01',3);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1018','慕容复','1903-02-01',1,'2000-07-01',3);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1019','张无忌','1902-01-01',1,'2000-07-01',3);
INSERT INTO HR_EMPLOYEE(CODE,NM,BIRTHDAY,SEX,JOIN_YMD,DEPARTMENT_ID)VALUES('1020','小龙女','1901-01-01',0,'2000-08-01',3);

drop table if exists bs_datasource;

/*==============================================================*/
/* Table: bs_datasource                                         */
/*==============================================================*/
create table bs_datasource
(
    ID                   bigint not null auto_increment comment '主键',
    CODE                 varchar(50) comment 'CODE',
    TITLE                varchar(50) comment '标题',
    DRIVER               varchar(200) comment '驱动',
    URL                  varchar(200) comment 'URL',
    ACCOUNT              varchar(20) comment '帐号',
    PASSWORD             varchar(32) comment '密码',
    IDX                  varchar(10) comment '排序',
    REMARK               varchar(500) comment '备注',
    REG_ID               varchar(50) comment '注册人',
    REG_IP               varchar(20) comment '注册IP',
    REG_TIME             datetime default CURRENT_TIMESTAMP comment '注册时间',
    REG_CLIENT_ID        varchar(50) comment '注冊端ID',
    UPT_ID               varchar(50) comment '修改人',
    UPT_IP               varchar(20) comment '修改人IP',
    UPT_TIME             datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    UPT_CLIENT_ID        varchar(50) comment '修改端ID',
    DATA_STATUS          int default 1 comment '活动状态',
    TENANT_CODE          varchar(50) default '0' comment '租户CODE',
    ORG_CODE             varchar(50) comment '组织CODE',
    DATA_VERSION         varchar(50) default '0' comment '数据版本',
    primary key (ID)
);

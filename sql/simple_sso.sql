drop table if exists SSO_USER;

/*==============================================================*/
/* Table: SSO_USER                                         */
/*==============================================================*/
create table SSO_USER
(
    ID                   bigint not null auto_increment,
    CODE                 varchar(50),
    NM                   varchar(50),
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
INSERT  INTO SSO_USER(CODE,NM)VALUES ('1002','张三');



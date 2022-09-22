
## 环境配置
```
刚pull下来的项目
先install anyline-simple-dependency(用来设置基础依赖)
再install anyline-simple(编译到前项目下所有的module)


注意anyline-simple-dependency中添加了如下的repositorie:
<repositories>
    <repository>
        <id>aliyun</id>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </repository>
    <repository>
        <id>anyline</id>
        <url>http://maven.anyline.org/repository/maven-snapshots/</url>
    </repository>
</repositories>

这里的repository.id对应mavent setteing.xml的mirror.id
<repository><id>aliyun</id></repository>对应mavent setting.xml的<mirror><id>aliyun</id></mirror>
<repository><id>anyline</id></repository>对应mavent setting.xml的<mirror><id>anyline</id></mirror> 

所以需要在本地%mavent_home%/conf/settings.xml中配置,在<mirrors>下添加:
<mirror>
    <id>aliyun</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror> 
 <mirror>
  <id>anyline</id>   
    <name>anyline maven</name>
  <mirrorOf>*</mirrorOf>   
  <url>http://maven.anyline.org/repository/maven-snapshots/</url>   
</mirror>  

如果还是缺少依赖的话 请联系QQ群(86020680)的管理员
或者直接pull源码 https://gitee.com/anyline/anyline
也是先install anyline-dependency
再install anyline


先创建数据库
运行anyline-simple-alpha-init中的InitTest(创建以下几个数据库以及表以及insert测试数据)(先配置好数据库的帐号密码)
或者手工运行脚本(在/sql/中)
simple
simple_crm
simple_erp
simple_sso
注意安装数据库时,设置表名不区分大小写
可以参考  
http://qa.anyline.org/art/v?id=lv0182c99f6acebf1d2d680783bf1168e8f99eced8e0136a4ec1

```
## 示例代码


   ```
        入门请先看  anyline-simple-hello(没有web环境 只操作数据库)
        
        SpringApplication application = new SpringApplication(HelloApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("BS_VALUE");
        System.out.println(set.size());


        项目中操作数据库只需要依赖anyline-jdbc-*以及相应数据库的驱动
 
        <groupId>org.anyline</groupId>
        <artifactId>anyline-jdbc-mysql(mssql|oracle|clickhouse...)</artifactId>

        如果本地有源码 就用本地的版本号
        如果没有可以用中央库的版本(如果你配置了阿里云的私服不要用最近日期的，可能还没同步过去)
        版本号参考
        https://mvnrepository.com/artifact/org.anyline/anyline-core
        测试环境可以用私服上的快照版本
        snapshots <url>http://maven.anyline.org/repository/maven-snapshots/</url>

        为了操作数据库，需要一个AnylineService，大部分操作通过这个service来完成

        可以在Controller中注入service
        @Qualifier("anyline.service") 
        private AnylineService service;

        
        但通常是继承这个TemplateController,里面已经默认注入了service
        org.anyboot.mvc.controller.impl.TemplateController

        接下来大部分操作通过这个service来完成，如
        DataSet set = service.querys("HR_USER");
        返回的DataSet中自带了常用的数学计算函数 如排序，求和，截取，清除空值，按列去重，最大最小值，交集合集差集，分组，行列转换，类SQL筛选
   ```


## 目录说明
        <!--基础依赖-->
        <module>anyline-simple-dependency</module>
        <module>anyline-simple-dependency-web</module>
        <module>anyline-simple-dependency-mysql</module>

        <!--简单的数据库操作-->
        <module>anyline-simple-start</module>

        <!--多数据源库操作-->
        <module>anyline-simple-ds</module>

        <!--对结果集的操作-->
        <module>anyline-simple-result</module>

        <!--pdf操作-->
        <module>anyline-simple-pdf</module>

        <!--正则表达式 主要用来抽取标签 拆分字符串 比如从一段html中抽取所有超链接-->
        <module>anyline-simple-regular</module>

        <!--各种场景的查询-->
        <module>anyline-simple-query</module>

        <!--网络操作-->
        <module>anyline-simple-net</module>

        <!--word excel操作 重点实现word excel中的表格操作以及html/css转word标签-->
        <module>anyline-simple-office-excel</module>
        <module>anyline-simple-office-word</module>
        <module>anyline-simple-rabbitmq</module>
        <module>anyline-simple-minio</module>
        <!--没有web环境-->
        <module>anyline-simple-noweb</module>

        <!--各种数据库操作示例-->

        <module>anyline-simple-jdbc-dialect</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-dm</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-oracle</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-postgresql</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-clickhouse</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-kingbase</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-sqlite</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-derby</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-h2</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-hsqldb</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-tdengine</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-mssql</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-mysql</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-mariadb</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-db2</module>

        <!--短信-->
        <module>anyline-simple-sms</module>

        <!--针对Entity的操作-->
        <module>anyline-simple-entity</module>

        <!--ThingsBoard-->
        <module>anyline-simple-thingsboard</module>

        <!--百度地图-->
        <module>anyline-simple-baidu-map</module>

        <!--腾讯地图-->
        <module>anyline-simple-qq-map</module>

        <!--模拟浏览器执行js-->
        <module>anyline-simple-spider</module>

        <!--事务回滚-->
        <module>anyline-simple-transaction</module>

        <!--动态数据源事务回滚-->
        <module>anyline-simple-ds-transaction</module>

        <!--适配 高德、百度、腾讯地图接口 超限额后自动切换平台-->
        <module>anyline-simple-map</module>

        <!--多表操作-->
        <module>anyline-simple-tables</module>

        <!--界定符-->
        <module>anyline-simple-delimiter</module>

        <!--界定符 占位-->
        <module>anyline-simple-placeholder</module>

        <!--数据库结构 表、列明细-->
        <module>anyline-simple-metadata</module>

        <!--部分不常用的操作-->
        <module>anyline-simple-special</module>

        <!--DDL相关操作  创建修改表列数据类型 以及超表子表-->
        <module>anyline-simple-ddl</module>

        <module>anyline-simple-help</module>

每个目录下有read说明了实现的示例、测试方式和注意事项
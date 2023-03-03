注意有些代码在test中，不要只看main
## 环境配置
```


如果只是搭建环境的话，只需要看这个:anyline-simple-alpha-clear  
这是一个独立的项目,spring-boot环境,与其他模块没有任何关系,直接运行  

现有项目基础上要集成anyline可以参考这里  
项目中主要用到Anyline(可以继承AnylineController或者在需要的位置注入)  
主要配置了maven仓库以及mvc环境和jdbc环境  
其他的一些依赖一般用现有项目的就可以  


查询条件构造比较灵活，示例代码中只作简单演示，详细格式参考  
anyline-simple-data-condition  
    
    

注意因为示例中用了快照版本 所以anyline-simple-dependency中添加了如下的repositorie:
<repositories>
    <repository>
        <id>aliyun</id>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </repository>
    <repository>
        <id>anyline</id>
        <url>http://maven.anyline.org/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
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

刚pull下来的项目
先install anyline-simple-dependency(用来设置基础依赖)
再install anyline-simple(编译到前项目下所有的module)


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


        项目中操作数据库只需要依赖anyline-data-jdbc-*以及相应数据库的驱动
 
        <groupId>org.anyline</groupId>
        <artifactId>anyline-data-jdbc-mysql(mssql|oracle|clickhouse...)</artifactId>

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
        <module>anyline-simple-start</module>
        <module>anyline-simple-start-mysql</module>
        <module>anyline-simple-start-mvc-mysql</module>
        
        <!--一个完全独立的项目，与其他模块不相关，用为演示快速环境搭建-->
        <module>anyline-simple-alpha-clear</module>

        <!--先执行这个初始化数据库中的表及测试数据-->
        <module>anyline-simple-alpha-init</module>

        <!--一个简单的入门示例-->
        <module>anyline-simple-hello</module>

        <!--多数据源库操作-->
        <module>anyline-simple-data-jdbc-ds</module>

        <!--对结果集的操作-->
        <module>anyline-simple-data-jdbc-result</module>

        <!--pdf操作-->
        <module>anyline-simple-pdf</module>

        <!--正则表达式 主要用来抽取标签 拆分字符串 比如从一段html中抽取所有超链接-->
        <module>anyline-simple-regular</module>

        <!--各种场景的查询-->
        <module>anyline-simple-query</module>

        <!--各种查询条件构造-->
        <module>anyline-simple-data-condition</module>
        
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
        <module>anyline-simple-data-jdbc-dialect</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-dm</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-oracle</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-postgresql</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-clickhouse</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-kingbase</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-sqlite</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-derby</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-h2</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-hsqldb</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-tdengine</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-mssql</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-mysql</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-mariadb</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-db2</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-questdb</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-timescale</module>
        <module>anyline-simple-data-jdbc-dialect/anyline-simple-data-jdbc-neo4j</module>
        <module>anyline-simple-data-mongodb</module>

        <!--短信-->
        <module>anyline-simple-sms</module>

        <!--针对Entity的操作-->
        <module>anyline-simple-data-jdbc-entity</module>

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
        <module>anyline-simple-data-jdbc-transaction</module>

        <!--适配 高德、百度、腾讯地图接口 超限额后自动切换平台-->
        <module>anyline-simple-map</module>

        <!--多表操作-->
        <module>anyline-simple-data-jdbc-tables</module>

        <!--界定符-->
        <module>anyline-simple-data-jdbc-delimiter</module>

        <!--界定符 占位-->
        <module>anyline-simple-data-jdbc-placeholder</module>

        <!--数据库结构 表、列明细-->
        <module>anyline-simple-data-jdbc-metadata</module>

        <!--部分不常用的操作-->
        <module>anyline-simple-special</module>

        <!--DDL相关操作  创建修改表列数据类型 以及超表子表-->
        <module>anyline-simple-data-jdbc-ddl</module>

        <module>anyline-simple-help</module>
        <!--MDL相关-->
        <module>anyline-simple-data-jdbc-dml</module>
        <!--word操作-->
        <module>anyline-simple-doc</module>
        <!--加密解密rsa m2等-->
        <module>anyline-simple-encrypt</module>
        <!--xml中自定义复杂SQL-->
        <module>anyline-simple-data-xml</module>
        <!--java中自定义SQL-->
        <module>anyline-simple-data-sql</module>


每个目录下有read说明了实现的示例、测试方式和注意事项
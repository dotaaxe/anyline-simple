
```
刚pull下来的项目
先install anyline-simple-dependenc(用来设置基础依赖)
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



先创建数据库(脚本在/sql/中):
simple
simple_crm
simple_erp
simple_sso


```
在每个数据库中执行sql目录下对应的SQL脚本  
注意示例中的数据库端口用了36902  
注意安装数据库时,设置表名不区分大小写
可以参考  
http://qa.anyline.org/art/v?id=lv0182c99f6acebf1d2d680783bf1168e8f99eced8e0136a4ec1

    <!--
        入门请先看  anyline-simple-hello(没有web环境 只操作数据库)

        SpringApplication application = new SpringApplication(HelloApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("BS_VALUE");
        System.out.println(set.size());


        其他示例中添加web环境的依赖
        实际上操作数据库只需要依赖anyline-jdbc-*以及相应数据库的驱动
 
        <groupId>org.anyline</groupId>
        <artifactId>anyline-jdbc-mysql(mssql|oracle|clickhouse...)</artifactId>

        如果本地有源码 就用本地的版本号
        如果没有可以用中央库的版本(如果你配置了阿里云的私服不要用最近日期的，可能还没同步过去) https://mvnrepository.com/artifact/org.anyline/anyline-core
        最新的版本可以通过anyline私服
        releases <url>http://maven.anyline.org/repository/maven-releases/</url>
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
    -->


    <!--没有web环境 只操作数据库-->
    <module>anyline-simple-nowweb</module>

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

每个目录下有read说明了实现的示例、测试方式和注意事项
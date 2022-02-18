先创建数据库:
```
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
        为了操作数据库，需要一个AnylineService，大部分操作通过这个service来完成

        可以在Controller中注入service
        @Qualifier("anyline.service") 
        private AnylineService service;

        但通常是继承这个TemplateController
        org.anyboot.mvc.controller.impl.TemplateController

        接下来大部分操作通过这个service来完成，如
        DataSet set = service.querys("HR_USER");
        返回的DataSet中自带了常用的数学计算函数 如排序，求和，截取，清除空值，按列去重，最大最小值，交集合集差集，分组，行列转换，类SQL筛选
    -->
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
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

每个目录下有read说明了实现的示例、测试方式和注意事项
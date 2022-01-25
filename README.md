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
    anyline-simple-start
    
    <!--多数据源库操作-->
    anyline-simple-ds
    
    <!--对结果集(DataSet/DataRow)的操作-->
    anyline-simple-result
    
    <!--word excel操作-->
    anyline-simple-office
    
    <!--pdf操作-->
    anyline-simple-pdf

每个目录下有read.txt说明了实现的示例、测试方式和注意事项
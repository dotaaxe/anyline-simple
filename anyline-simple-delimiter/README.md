关于界定符  
一般情况下不需要  
默认没有开启  
```
可以通过配置文件 anyline-config.xml中的配置开启
<!--定界符 -->
<property key="IS_SQL_DELIMITER_OPEN">true</property>

或者通过配置类
ConfigTable.IS_SQL_DELIMITER_OPEN = true;	
```  
  
开启后生成SQL时，会在表名列名上添加界定符 如
 
```
service.querys("HR_USER(ID,NAME)");
生成最终SQL:
SELECT `ID`, `NAME` FROM `HR_USER`
```
系统已为不同数据库设置了默认界定符，但有些数据库支持多种界定符如SQL Server中支持[]和“”
如果需要修改可以在配置文件中设置，参考对应数据库的SQLCreater如
```java 

org.anyline.jdbc.config.db.impl.db2.SQLCreaterImpl  
@Value("${anyline.jdbc.delimiter.db2:}")  
private String delimiter;  
在spring配置文件中这样设置
anyline.jdbc.delimiter.db2=“
或
anyline.jdbc.delimiter.db2=“”
```



但不会对原生SQL作修改(因为原生SQL会有比较复杂的情况，不容易识别)  
```sql
#如:
service.querys("HR_USER", "LVL:1", "AGE>20");
#生成最终SQL:
SELECT * FROM `HR_USER` WHERE `LVL` = ? AND AGE>20
#这里的AGE并不会添加界定符

#再如:
service.querys("SELECT * FROM HR_USER WHERE LVL = 1");
#这里不会对SQL作任何处理,会直接执行
SELECT * FROM HR_USER WHERE LVL = 1
```



在多数据源的情况下有可能有些数据库设置等问题，需要在原生的SQL中添加界定符号  
因为原生SQL识别不出来表名、列名，
需要在写SQL上使用占位符，在SQL执行时系统会根据数据源的不同把占位符换成相应的界定符
具体参考示例:anyline-simple-placeholder




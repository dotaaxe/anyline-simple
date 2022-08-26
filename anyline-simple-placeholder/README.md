
系统不会对原生SQL添加界定符(因为原生SQL会有比较复杂的情况，不容易识别)  
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
如果需要在原生SQL上添加界定符，需要编码时就添加上    
但不同的数据库界定符不同，所以在遇到多数据源时，可以使用界定符的占位符  
在SQL执行之前，系统会根据数据源类型的不同把占位符换成对应的界定符

需要开启占位符配置
```
<!--
是否开启界定符占位
开启后,编码时无论什么数据库都可以用这里指定的界定符,在SQL执行之前会替换成相应数据库的界定符
如果最终执行的SQL中出现界定符(不包括?占位的参数值),需要把占位符写两次 类似与sql insert values中出'要写成''
-->
<property key="IS_SQL_DELIMITER_PLACEHOLDER_OPEN">false</property>
<!--
界定符占位符(IS_SQL_DELIMITER_PLACEHOLDER_OPEN开启才才生效) 如果不设置默认`(mysql界定符),

可以设置成一对[],尽量不要设置一对,因为会设置成了一对后,会自动开启正则匹配
只有类似SQL Server设置了只支持[],不支持""时才需要设置成一对[]
-->
<property key="SQL_DELIMITER_PLACEHOLDER">`</property>

也可以通过配置类

ConfigTable.IS_SQL_DELIMITER_PLACEHOLDER_OPEN = true;
ConfigTable.SQL_DELIMITER_PLACEHOLDER = "[]";
ConfigTable.SQL_DELIMITER_PLACEHOLDER = "\"";
ConfigTable.SQL_DELIMITER_PLACEHOLDER = "`";
```


开启后要根据SQL_DELIMITER_PLACEHOLDER来写SQL 如默认情况下的占位符用`(mysql的界定符)
```
SELECT * FROM `HR_USER` WHERE `ID` > 1 AND LVL = 1
在SQL执行前会根据数据源的不同把占位符替换成不同的界定符
MySQL中执行:
SELECT * FROM `HR_USER` WHERE `ID` > 1 AND LVL = 1

SQL Server中执行:
SELECT * FROM [HR_USER] WHERE [ID] > 1 AND LVL = 1
```

多数据源事务支持  
启动后分别调用  
http://127.0.0.1:8088/emp/a         //默认数据源  
http://127.0.0.1:8088/usr/a         //运行时注册数据源  
http://127.0.0.1:8088/cust/a        //配置文件数据源  

如果不回滚  
启动类上有没有开启  
org.springframework.transaction.annotation.EnableTransactionManagement;  
检查一下有没有依赖
``` 
 <groupId>org.springframework</groupId>  
 <artifactId>spring-tx</artifactId>  
```     
配置文件中没有不配置
spring.transaction.rollback-on-commit-failur=true

根据版本的默认配置不同，有些版本不需要配置，不需要注解，自己测试一下  
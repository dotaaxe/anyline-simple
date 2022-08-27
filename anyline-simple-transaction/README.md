事务支持  
启动后调用  
http://127.0.0.1:8080/emp/a   

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



不要在controller中回滚 除非能把异常抛出到上一层,也就是抛给spring容器
示例中的a1是一个失败的反例，并不能实现回滚

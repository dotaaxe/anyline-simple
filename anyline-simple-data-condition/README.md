查询条件的值可能来自  
1.java后台构造  
2.http参数,这个来源用的是最多的,但为测试和说明 更直观一些，当前示例中的参数值通过后台模拟一下 就不需要发起http请求了  

有个非常容易混淆的地方,一定要注意  
String flag = 1;  
service.querys("CRM_USER", condition("TYPE_CODE:type"), "FLAG:"+flag);  
service.querys("CRM_USER", condition("TYPE_CODE:type"), "FLAG:1");   
以上condition()是AnylineController提供的 是基于web环境从request中接收type参数值,这里的type是参数key而不是值  
而"FLAG:1"是在java后台直接构造的查询条件,与request没有关系,这里的1需要提供一个具体的值  
约定格式参考[http://doc.anyline.org/s?id=p298pn6e9o1r5gv78acvic1e624c62387f2c45dd13bb112b34176fad5a868fa6a4](http://doc.anyline.org/s?id=p298pn6e9o1r5gv78acvic1e624c62387f2c45dd13bb112b34176fad5a868fa6a4)
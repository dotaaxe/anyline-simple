
```
ʾ��������˽���ϵĿ��հ汾��Ҫ�ڱ���mavent_home/conf/settings.xml������
��<mirrors>�����
<mirror>
  <id>anyline</id>   
  <mirrorOf>*</mirrorOf>   
  <url>http://maven.anyline.org/repository/maven-snapshots/</url>   
</mirror>  

�ȴ������ݿ�(�ű���/sql/��):
simple
simple_crm
simple_erp
simple_sso


```
��ÿ�����ݿ���ִ��sqlĿ¼�¶�Ӧ��SQL�ű�  
ע��ʾ���е����ݿ�˿�����36902  
ע�ⰲװ���ݿ�ʱ,���ñ��������ִ�Сд
���Բο�  
http://qa.anyline.org/art/v?id=lv0182c99f6acebf1d2d680783bf1168e8f99eced8e0136a4ec1

    <!--
        �������ȿ�  anyline-simple-hello(û��web���� ֻ�������ݿ�)

        SpringApplication application = new SpringApplication(HelloApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        AnylineService service = (AnylineService) ctx.getBean("anyline.service");
        DataSet set = service.querys("BS_VALUE");
        System.out.println(set.size());


        ����ʾ�������web����������
        ʵ���ϲ������ݿ�ֻ��Ҫ����anyline-jdbc-*�Լ���Ӧ���ݿ������
 
        <groupId>org.anyline</groupId>
        <artifactId>anyline-jdbc-mysql(mssql|oracle|clickhouse...)</artifactId>

        ���������Դ�� ���ñ��صİ汾��
        ���û�п����������İ汾(����������˰����Ƶ�˽����Ҫ��������ڵģ����ܻ�ûͬ����ȥ) https://mvnrepository.com/artifact/org.anyline/anyline-core
        ���µİ汾����ͨ��anyline˽��
        releases <url>http://maven.anyline.org/repository/maven-releases/</url>
        snapshots <url>http://maven.anyline.org/repository/maven-snapshots/</url>

        Ϊ�˲������ݿ⣬��Ҫһ��AnylineService���󲿷ֲ���ͨ�����service�����

        ������Controller��ע��service
        @Qualifier("anyline.service") 
        private AnylineService service;

        



        ��ͨ���Ǽ̳����TemplateController,�����Ѿ�Ĭ��ע����service
        org.anyboot.mvc.controller.impl.TemplateController

        �������󲿷ֲ���ͨ�����service����ɣ���
        DataSet set = service.querys("HR_USER");
        ���ص�DataSet���Դ��˳��õ���ѧ���㺯�� ��������ͣ���ȡ�������ֵ������ȥ�أ������Сֵ�������ϼ�������飬����ת������SQLɸѡ
    -->


    <!--û��web���� ֻ�������ݿ�-->
    <module>anyline-simple-nowweb</module>

    <module>anyline-simple-start</module>
    
    <!--������Դ�����-->
    <module>anyline-simple-ds</module>
    
    <!--�Խ�����Ĳ���-->
    <module>anyline-simple-result</module>
    
    <!--pdf����-->
    <module>anyline-simple-pdf</module>
    
    <!--������ʽ ��Ҫ������ȡ��ǩ ����ַ��� �����һ��html�г�ȡ���г�����-->
    <module>anyline-simple-regular</module>
    
    <!--���ֳ����Ĳ�ѯ-->
    <module>anyline-simple-query</module>
    
    <!--�������-->
    <module>anyline-simple-net</module>
    
    <!--word excel���� �ص�ʵ��word excel�еı������Լ�html/cssתword��ǩ-->
    <module>anyline-simple-office-excel</module>
    <module>anyline-simple-office-word</module>

ÿ��Ŀ¼����read˵����ʵ�ֵ�ʾ�������Է�ʽ��ע������
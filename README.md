
```
��pull��������Ŀ
��install anyline-simple-dependenc(�������û�������)
��install anyline-simple(���뵽ǰ��Ŀ�����е�module)


ע��anyline-simple-dependency����������µ�repositorie:
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

�����repository.id��Ӧmavent setteing.xml��mirror.id
<repository><id>aliyun</id></repository>��Ӧmavent setting.xml��<mirror><id>aliyun</id></mirror>
<repository><id>anyline</id></repository>��Ӧmavent setting.xml��<mirror><id>anyline</id></mirror> 

������Ҫ�ڱ���%mavent_home%/conf/settings.xml������,��<mirrors>�����:
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

�������ȱ�������Ļ� ����ϵQQȺ(86020680)�Ĺ���Ա
����ֱ��pullԴ�� https://gitee.com/anyline/anyline
Ҳ����install anyline-dependency
��install anyline

�ȴ������ݿ�(�ű���/sql/��):
simple
simple_crm
simple_erp
simple_sso


```
��ÿ�����ݿ���ִ��sqlĿ¼�¶�Ӧ��SQL�ű�
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
        ���û�п����������İ汾(����������˰����Ƶ�˽����Ҫ��������ڵģ����ܻ�ûͬ����ȥ)
        �汾�Ųο�
        https://mvnrepository.com/artifact/org.anyline/anyline-core
        ���Ի���������˽���ϵĿ��հ汾
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


        <!--��������-->
        <module>anyline-simple-dependency</module>
        <module>anyline-simple-dependency-web</module>
        <module>anyline-simple-dependency-mysql</module>

        <!--�򵥵����ݿ����-->
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
        <module>anyline-simple-rabbitmq</module>
        <module>anyline-simple-minio</module>
        <!--û��web����-->
        <module>anyline-simple-noweb</module>

        <!--�������ݿ����ʾ��-->
        <module>anyline-simple-jdbc-dialect</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-dm</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-oracle</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-postgresql</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-clickhouse</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-kingbase</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-sqlite</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-derby</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-h2</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-hsqldb</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-tdengine</module>
        <module>anyline-simple-jdbc-dialect/anyline-simple-jdbc-mssql</module>

        <!--����-->
        <module>anyline-simple-sms</module>

        <!--���Entity�Ĳ���-->
        <module>anyline-simple-entity</module>

        <!--ThingsBoard-->
        <module>anyline-simple-thingsboard</module>

        <!--�ٶȵ�ͼ-->
        <module>anyline-simple-baidu-map</module>

        <!--��Ѷ��ͼ-->
        <module>anyline-simple-qq-map</module>

        <!--ģ�������ִ��js-->
        <module>anyline-simple-spider</module>

        <!--����ع�-->
        <module>anyline-simple-transaction</module>

        <!--��̬����Դ����ع�-->
        <module>anyline-simple-ds-transaction</module>

        <!--���� �ߵ¡��ٶȡ���Ѷ��ͼ�ӿ� ���޶���Զ��л�ƽ̨-->
        <module>anyline-simple-map</module>

        <!--������-->
        <module>anyline-simple-tables</module>

        <!--�綨��-->
        <module>anyline-simple-delimiter</module>

        <!--�綨�� ռλ-->
        <module>anyline-simple-placeholder</module>

        <!--���ݿ�ṹ ������ϸ-->
        <module>anyline-simple-metadata</module>

        <!--���ֲ����õĲ���-->
        <module>anyline-simple-special</module>

        <!--DDL��ز���  �����޸ı����������� �Լ������ӱ�-->
        <module>anyline-simple-ddl</module>

        <module>anyline-simple-help</module>

ÿ��Ŀ¼����read˵����ʵ�ֵ�ʾ�������Է�ʽ��ע������
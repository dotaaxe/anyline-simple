package org.anyline.data.mongodb;


import java.util.ArrayList;
import java.util.List;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB 操作类连接
 * @author hlinfo.net
 *
 */
public class MongodbConn {
    //MongoDB数据库地址
    private static String host="192.168.220.100";
    // Mongodb端口号， 默认是27017
    private static int port = 27017;
    //Mongodb 用户名,没有设置可以为空
    private static String userName="root";
    //mongoDB 密码,没有设置可以为空
    private static String password="root";
    //MongoDB 默认链接的数据库
    private static String databaseName="simple";
    /**
     * MongoDB连接对象
     */
    private MongoClient client;

    /**
     * 不需要认证获取连接对象
     */
    public void mongoClient(){
        try {
            //获取mongodb连接对象
            client = new MongoClient(host,port);
        } catch (Exception e) {
            System.out.println("不需要认证获取连接对象失败:"+e);
        }
    }

    /**
     * 需要认证获取连接对象
     */
    public void certifyMongoClient(){
        try {
            //连接到MongoDB
            //ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress(host,port);
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            //createScramSha1Credential()三个参数分别为 用户名,数据库名,密码
            MongoCredential credential = MongoCredential.createScramSha1Credential(userName, databaseName, password.toCharArray());
            //List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            //credentials.add(credential);
            MongoClientOptions options = MongoClientOptions.builder().build();
            //通过连接认证获取MongoDB连接
            client = new MongoClient(addrs,credential,options);
        } catch (Exception e) {
            System.out.println("需要认证的获取连接对象失败:"+e);
        }
    }

    /**
     * 获取数据库对象
     * @return  MongoDatabase
     */
    public MongoDatabase getDatabase(){
        if(client == null){
            //没有配置用户名和密码的情况
            if(userName==null || "".equals(userName) || password==null || "".equals(password)) {
                mongoClient();
            }else {
                //配置用户名和密码的情况
                certifyMongoClient();
            }
        }
        MongoDatabase database = client.getDatabase(databaseName);
        return database;
    }

    /**
     * 关闭连接对象
     */
    public void close(){
        if(client != null){
            client.close();
        }
        client = null;
    }

}


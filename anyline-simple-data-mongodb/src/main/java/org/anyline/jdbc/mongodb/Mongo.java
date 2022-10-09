package org.anyline.data.jdbc.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class Mongo extends MongoClient {
    public static void main(String[] args) {
        // 创建 MongoDB 连接
        MongoClient mongo = new MongoClient( "192.168.220.100" , 27017 );
        // 连接到 MongoDB
        MongoCredential credential = MongoCredential.createCredential("root", "admin", "root".toCharArray());
        System.out.println("Connected to the database successfully");
        // 访问数据库
        MongoDatabase database = mongo.getDatabase("admin");
        System.out.println("Credentials ::"+ credential);
        database.createCollection("memberss");

    }
}

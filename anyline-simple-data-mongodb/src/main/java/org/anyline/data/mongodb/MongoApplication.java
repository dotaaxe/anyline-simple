package org.anyline.data.mongodb;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;

@SpringBootApplication
public class MongoApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MongoApplication.class);
        ConfigurableApplicationContext context = application.run(args);

        AnylineService service = (AnylineService)context.getBean("anyline.service");
        //DatabaseHolder.setDataSource("mongo");
        DataSet set = new DataSet();
        for(int i=0; i<10;i ++){
            DataRow row = new DataRow();
            row.put("ID", i);
            row.put("name", "USER_"+i);
            set.add(row);
        }
        service.insert(set);
          MongoTemplate mongoTemplate;
    }

    public static void test() {
        String uri = "mongodb://localhost:27017";

        ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri)).serverApi(serverApi).build();
        MongoClient client = MongoClients.create(uri);

        //MongoClient client = MongoClients.create(settings);
        MongoDatabase database = client.getDatabase("admin");
        Bson command = new BsonDocument("ping", new BsonInt64(1));
        Document commandResult = database.runCommand(command);
        System.out.println("连接成功!");

        MongoCollection<Document> collection = database.getCollection("movies");

        InsertOneResult result = collection.insertOne(new Document()
                .append("_id", new ObjectId())
                .append("title", "Ski Bloopers")
                .append("genres", Arrays.asList("Documentary", "Comedy")));
        System.out.println("插入一行成功 id: " + result.getInsertedId());

        MongoCollection<DataRow> users = database.getCollection("user", DataRow.class);
         result = users.insertOne(new DataRow()
                .set("_id", new ObjectId())
                .set("title", "Ski Bloopers")
                .set("genres", Arrays.asList("Documentary", "Comedy")));
        System.out.println("Success! Inserted document id: " + result.getInsertedId());

        DataSet set = new DataSet();
        for(int i=0; i<10;i ++){
            DataRow row = new DataRow();
            row.put("ID", i);
            row.put("name", "USER_"+i);
            set.add(row);
        }
        users.insertMany(set.getRows());
        System.out.println("插入多行成功 id: " + result.getInsertedId());


        Bson filter = Filters.and(Filters.eq("NAME", "USER_1"));
        FindIterable<DataRow> docs = users.find(filter);
        for(DataRow row:docs){
            System.out.println(row);
        }
    }
}

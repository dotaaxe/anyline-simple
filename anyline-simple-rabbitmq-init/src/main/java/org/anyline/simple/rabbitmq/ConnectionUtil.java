package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
    private static Connection connection = null;
    public static Connection connection() throws Exception{
        //连接工厂
        if(null ==connection || !connection.isOpen()) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.220.100");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            connection = factory.newConnection();
            System.out.println("建立新连接");
        }
        return connection;
    }
}

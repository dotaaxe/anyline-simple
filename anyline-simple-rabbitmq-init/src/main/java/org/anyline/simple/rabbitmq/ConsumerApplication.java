package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.anyline.data.entity.Table;
import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.service.AnylineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class ConsumerApplication {
    //队列名称
    private static String queue_name = "q_location";
    //创建一个连接工厂
    public static void main(String[] args) throws Exception {

        SpringApplication application = new SpringApplication(ProducerApplication.class);

        ConfigurableApplicationContext context = application.run(args);
        AnylineService service = context.getBean(AnylineService.class);
        if(null ==service.metadata().table("mq_msg")){
            Table table = new Table("mq_msg");
            table.addColumn("ID", "bigint").setAutoIncrement(true).setPrimaryKey(true);
            table.addColumn("TIME", "datetime").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
            table.addColumn("FLAG","varchar(20)");
            table.addColumn("LOCATION_TIME", "datetime");
            service.ddl().create(table);
        }
        Consumer consumer = context.getBean(Consumer.class);

        Connection connection = ConnectionUtil.connection();
        //获取信道
        Channel channel = connection.createChannel();

        //声明接收消息
        DeliverCallback deliver = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                consumer.receive(new String(delivery.getBody()));
            }
        };

        //声明取消消息
        CancelCallback cancel= mess->{
            System.out.println("消息被中断");
        };

        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费者成功之后是否要应答 true代表自动应答，false代表树洞应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         *
         * */
        channel.basicConsume(queue_name,true,deliver ,cancel);
    }
}
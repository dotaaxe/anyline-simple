package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.anyline.data.adapter.JDBCAdapter;
import org.anyline.entity.DataRow;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.anyline.simple.rabbitmq.Constant.QUEUE_SIMPLE;

@Component
public class ConsumerSimple {
    @Autowired
    @Qualifier("anyline.service")
    private AnylineService service;

    private Channel channel;

    private DeliverCallback deliver;
    private CancelCallback cancel;
    public ConsumerSimple(){
        try {
            Connection connection = ConnectionUtil.connection();
            //获取信道
            channel = connection.createChannel();

            channel.queueDeclare(QUEUE_SIMPLE, true, false, false, null);
            //声明接收消息
            deliver = new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
                    //consumer中数据库相磁操作
                    receive(new String(delivery.getBody()));
                }
            };

            //声明取消消息
            cancel = mess -> {
                System.out.println("消息被中断");
            };

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void start() throws Exception{
        init();
        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费者成功之后是否要应答 true代表自动应答，false代表树洞应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         *
         * */
        channel.basicConsume(QUEUE_SIMPLE,true, deliver ,cancel);
    }

    public void receive(String msg){
        System.out.println("receive:"+msg);
        DataRow row = DataRow.parseJson(msg);
        service.insert("mq_msg", row);
    }

    public void init() throws Exception{
        if(null ==service.metadata().table("mq_msg")){
            Table table = new Table("mq_msg");
            table.addColumn("ID", "bigint").setAutoIncrement(true).setPrimaryKey(true);
            table.addColumn("TIME", "datetime").setDefaultValue(JDBCAdapter.SQL_BUILD_IN_VALUE.CURRENT_TIME);
            table.addColumn("FLAG","varchar(20)");
            table.addColumn("LOCATION_TIME", "datetime");
            service.ddl().create(table);
        }
    }
}
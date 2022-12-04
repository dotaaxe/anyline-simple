package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.anyline.simple.rabbitmq.Constant.QUEUE_TOPIC_MAIL;
import static org.anyline.simple.rabbitmq.Constant.QUEUE_TOPIC_SMS;

@Component
public class ConsumerTopicSMS {

    private Channel channel;

    private DeliverCallback deliver;
    private CancelCallback cancel;
    public ConsumerTopicSMS(){
        try {
            Connection connection = ConnectionUtil.connection();
            //获取信道
            channel = connection.createChannel();

            //声明队列
            channel.queueDeclare(QUEUE_TOPIC_SMS, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(Constant.EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
            //绑定交换机与队列
            channel.queueBind(QUEUE_TOPIC_SMS, Constant.EXCHANGE_ROUTING, "sms");

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
        channel.basicConsume(QUEUE_TOPIC_SMS,true, deliver ,cancel);
    }

    public void receive(String msg){
        System.out.println("receive(topic sms):"+msg);
    }

}
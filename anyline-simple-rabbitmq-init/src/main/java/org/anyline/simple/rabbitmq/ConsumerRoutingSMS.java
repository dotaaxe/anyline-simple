package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.anyline.simple.rabbitmq.Constant.*;

@Component
public class ConsumerRoutingSMS {

    private Channel channel;

    private DeliverCallback deliver;
    private CancelCallback cancel;
    public ConsumerRoutingSMS(){
        try {
            Connection connection = ConnectionUtil.connection();
            //获取信道
            channel = connection.createChannel();

            //声明队列
            channel.queueDeclare(Constant.QUEUE_ROUTING_SMS, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(Constant.EXCHANGE_ROUTING, BuiltinExchangeType.DIRECT);
            //绑定交换机与队列
            channel.queueBind(Constant.QUEUE_ROUTING_SMS, Constant.EXCHANGE_ROUTING, "sms");

            //声明接收消息
            deliver = new DeliverCallback() {
                @Override
                public void handle(String s, Delivery delivery) throws IOException {
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
        channel.basicConsume(QUEUE_ROUTING_SMS,true, deliver ,cancel);
    }

    public void receive(String msg){
        System.out.println("receive(routing sms):"+msg);
    }

}
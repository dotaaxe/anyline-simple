package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.anyline.simple.rabbitmq.Constant.*;

@Component
public class ConsumerFanoutSMS {

    private Channel channel;

    private DeliverCallback deliver;
    private CancelCallback cancel;
    public ConsumerFanoutSMS(){
        try {
            Connection connection = ConnectionUtil.connection();
            //获取信道
            channel = connection.createChannel();

            //声明队列
            channel.queueDeclare(Constant.QUEUE_FANOUT_SMS, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(Constant.EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);
            //绑定交换机与队列
            channel.queueBind(Constant.QUEUE_FANOUT_SMS, Constant.EXCHANGE_FANOUT, "");

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
        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费者成功之后是否要应答 true代表自动应答，false代表树洞应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         *
         * */
        channel.basicConsume(QUEUE_FANOUT_SMS,true, deliver ,cancel);
    }

    public void receive(String msg){
        System.out.println("receive(fanout sms):"+msg);
    }

}
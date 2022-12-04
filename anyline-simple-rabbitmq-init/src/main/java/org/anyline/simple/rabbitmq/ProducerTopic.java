package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.stereotype.Component;

import static org.anyline.simple.rabbitmq.Constant.EXCHANGE_ROUTING;
import static org.anyline.simple.rabbitmq.Constant.EXCHANGE_TOPIC;

@Component
public class ProducerTopic {
    private Channel channel;
    public ProducerTopic(){
        try {
            Connection connection = ConnectionUtil.connection();
            channel = connection.createChannel();
            //声明两个队列
            channel.queueDeclare(Constant.QUEUE_TOPIC_SMS, true, false, false, null);
            channel.queueDeclare(Constant.QUEUE_TOPIC_MAIL, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);

            //绑定交换机与队列
            channel.queueBind(Constant.QUEUE_TOPIC_SMS, EXCHANGE_TOPIC, "sms");
            channel.queueBind(Constant.QUEUE_TOPIC_MAIL, EXCHANGE_TOPIC, "mail.#");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //发布订阅
    public void send() throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        DataRow msg = new DataRow();
                        msg.put("FLAG", System.currentTimeMillis());
                        msg.put("MSG_TIME", DateUtil.format());
                        //这里发送到交换机,交换机上绑定了两个队列,消息将被同时投送到两个队列中
                        channel.basicPublish(EXCHANGE_TOPIC, "sms", null, msg.set("type","sms").getJson().getBytes());
                        channel.basicPublish(EXCHANGE_TOPIC, "mail.126", null, msg.set("type","mail 126").getJson().getBytes());
                        channel.basicPublish(EXCHANGE_TOPIC, "mail.163", null, msg.set("type","mail 163").getJson().getBytes());
                        System.out.println("send topic:" + msg.getJson());
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}

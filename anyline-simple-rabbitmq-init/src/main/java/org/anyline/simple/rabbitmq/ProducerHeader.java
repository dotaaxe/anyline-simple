package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.anyline.simple.rabbitmq.Constant.EXCHANGE_HEADER;
import static org.anyline.simple.rabbitmq.Constant.EXCHANGE_TOPIC;

@Component
public class ProducerHeader {
    private Channel channel;
    public ProducerHeader(){
        try {
            Connection connection = ConnectionUtil.connection();
            channel = connection.createChannel();
            //声明两个队列
            channel.queueDeclare(Constant.QUEUE_HEADER_MAIL, true, false, false, null);
            channel.queueDeclare(Constant.QUEUE_HEADER_SMS, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_HEADER, BuiltinExchangeType.HEADERS);

            //绑定交换机与队列
            Map<String,Object> headers = new HashMap<>();
            headers.put("msg_header","sms");
            channel.queueBind(Constant.QUEUE_HEADER_SMS, Constant.EXCHANGE_HEADER,"", headers);

            headers.put("msg_header","mail");
            channel.queueBind(Constant.QUEUE_HEADER_MAIL, Constant.EXCHANGE_HEADER,"", headers);
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
                        Map<String,Object> headers = new HashMap<>();
                        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();

                        headers.put("msg_header","sms");
                        builder.headers(headers);
                        channel.basicPublish(EXCHANGE_HEADER, "", builder.build(), msg.set("type","sms").getJson().getBytes());
                        System.out.println("send header:" + msg.getJson());

                        headers.put("msg_header","mail");
                        builder.headers(headers);
                        channel.basicPublish(EXCHANGE_HEADER, "", builder.build(), msg.set("type","mail").getJson().getBytes());
                        System.out.println("send header:" + msg.getJson());

                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}

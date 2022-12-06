package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.stereotype.Component;

import static org.anyline.simple.rabbitmq.Constant.EXCHANGE_FANOUT;
import static org.anyline.simple.rabbitmq.Constant.QUEUE_SIMPLE;

@Component
public class ProducerFanout {
    private Channel channel;
    public ProducerFanout(){
        try {
            Connection connection = ConnectionUtil.connection();
            channel = connection.createChannel();
            //声明两个队列
            channel.queueDeclare(Constant.QUEUE_FANOUT_SMS, true, false, false, null);
            channel.queueDeclare(Constant.QUEUE_FANOUT_MAIL, true, false, false, null);
            //声明交换机
            channel.exchangeDeclare(Constant.EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);

            //绑定交换机与队列
            channel.queueBind(Constant.QUEUE_FANOUT_SMS, Constant.EXCHANGE_FANOUT, "");
            channel.queueBind(Constant.QUEUE_FANOUT_MAIL, Constant.EXCHANGE_FANOUT, "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
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
                        channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getJson().getBytes());
                        System.out.println("send fanout:" + msg.getJson());
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}

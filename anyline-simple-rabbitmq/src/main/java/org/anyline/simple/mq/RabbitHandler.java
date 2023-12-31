package org.anyline.simple.mq;

import com.rabbitmq.client.Channel;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitHandler {

    @Autowired
    private AnylineService service;
    private static DataSet cars = new DataSet();
    int vol = 20;
    @org.springframework.amqp.rabbit.annotation.RabbitHandler
    @RabbitListener(queues = "anyline_rout"
            , containerFactory = "anylineRabbitListenerContainerFactory"
            , concurrency = "1")
    public void car(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            String json = new String(message.getBody());
            System.out.println("time_rout receive:"+json);
            DataRow row = DataRow.parseJson(json);
            row.removeEmpty();//删除空值
            synchronized (cars) {
                cars.add(row);
                if (cars.size() >= vol) {
                    try {
                        //service.insert("crm.CAR", cars);
                    }catch (Exception e){}
                    cars = new DataSet();
                }
            }
        } catch (IOException e) {
            try {
                channel.basicRecover();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}

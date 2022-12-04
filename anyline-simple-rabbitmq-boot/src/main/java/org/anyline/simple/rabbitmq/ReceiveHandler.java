package org.anyline.simple.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {
    @RabbitListener(queues = {RabbitConfig.QUEUE_SMS})
    public void listenerSMS(String msg){
        System.out.println("receive sms:"+msg);
    }
    @RabbitListener(queues = {RabbitConfig.QUEUE_MAIL})
    public void listenerMail(String msg){
        System.out.println("receive mail:"+msg);
    }
}

package org.anyline.simple.rabbitmq;

import org.anyline.service.AnylineService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {
    @Autowired
    @Qualifier("anyline.service")
    private AnylineService service;

    @RabbitListener(queues = {RabbitConfig.QUEUE_SMS})
    public void listenerSMS(String msg){
        System.out.println("receive sms:"+msg);
    }
    @RabbitListener(queues = {RabbitConfig.QUEUE_MAIL})
    public void listenerMail(String msg){
        System.out.println("receive mail:"+msg);
    }
}

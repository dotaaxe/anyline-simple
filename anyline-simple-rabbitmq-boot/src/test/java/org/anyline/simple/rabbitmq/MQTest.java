package org.anyline.simple.rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MQTest {
    @Autowired
    private RabbitTemplate template;

    @Test
    public void send(){
            while (true){
                template.convertAndSend(RabbitConfig.EXCHANGE_TOPIC, "mail", "mail msg");
                template.convertAndSend(RabbitConfig.EXCHANGE_TOPIC, "mail.126", "126.mail msg");
                template.convertAndSend(RabbitConfig.EXCHANGE_TOPIC, "mail.163", "163.mail msg");
                template.convertAndSend(RabbitConfig.EXCHANGE_TOPIC, "sms", "sms msg");
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
    }

}

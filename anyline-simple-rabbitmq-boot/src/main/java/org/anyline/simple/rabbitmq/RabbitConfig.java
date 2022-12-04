package org.anyline.simple.rabbitmq;


import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static  final  String EXCHANGE_TOPIC = "exchange_topic_boot";
    public static  final  String QUEUE_MAIL = "queue_mail_boot";
    public static  final  String QUEUE_SMS = "queue_sms_boot";
    @Bean(EXCHANGE_TOPIC)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC).durable(true).build();
    }

    @Bean(QUEUE_MAIL)
    public Queue queueMail(){
        return new Queue(QUEUE_MAIL);
    }
    @Bean(QUEUE_SMS)
    public Queue queueSMS(){
        return new Queue(QUEUE_SMS);
    }
    @Bean
    public Binding bindingMail(@Qualifier(QUEUE_MAIL) Queue queue, @Qualifier(EXCHANGE_TOPIC) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("mail.#").noargs();
    }
    @Bean
    public Binding bindingSMS(@Qualifier(QUEUE_SMS) Queue queue, @Qualifier(EXCHANGE_TOPIC) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("sms").noargs();
    }
}

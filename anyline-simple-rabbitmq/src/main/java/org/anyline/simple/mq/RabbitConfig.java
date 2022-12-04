package org.anyline.simple.mq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
    /**
     * 配置RabbitMq数据源
     */
    @Bean(name = "anylineConnectionFactory")
    public CachingConnectionFactory connectionFactory(@Value("${spring.rabbitmq.host}") String host,
                                                         @Value("${spring.rabbitmq.port}") int port,
                                                         @Value("${spring.rabbitmq.username}") String username,
                                                         @Value("${spring.rabbitmq.password}") String password,
                                                         @Value("${spring.rabbitmq.virtual-host}") String virtualHost) {
        // 通常我们使用RabbitTemplate来进行简单的收发消息，而RabbitTemplate使用CachingConnectionFactory作为连接工厂
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    /**
     * 配置RabbitMq连接
     */
    @Bean(name = "anylineRabbitTemplate")
    public RabbitTemplate rabbitTemplate(@Qualifier("anylineConnectionFactory") CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * 配置RabbitMq监听
     */
    @Bean(name = "anylineRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory factory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                           @Qualifier("anylineConnectionFactory") CachingConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(5); // 并发设置消费者监听数量
        factory.setMaxConcurrentConsumers(20); // 并发设置最大消费者监听数量
        factory.setPrefetchCount(500); // 队列最大未ack消息，达到上限后，队列将不在接收消息
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 消息确认ack   如果消息接收后，未ack确认，则管理页面显示unacked +1
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /**
     * 配置RabbitMq
     * RabbitAdmin：作为申明、删除交换机和队列，绑定和解绑队列和交换机的绑定关系使用。
     */
    @Bean(name = "RabbitAdmin")
    public RabbitAdmin rabbitAdmin(
            @Qualifier("anylineConnectionFactory") CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
}

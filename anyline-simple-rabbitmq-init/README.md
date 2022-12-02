
基于rabbitmq官方驱动的hello world  
springboot版本请看 anyline-simple-rabbitmq  
启动ProducerApplication
    每3秒通过Producer发送一条消息
启动ConsumerApplication
    先检测mq_location表是否存在，不存在则创建表
    接收消息后通过Consumer消费(保存到数据库)

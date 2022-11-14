package org.anyline.simple.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CallbackProducer {//带回调函数的生产者
    public static String topic = "callback_topic";//定义主题
    public static void main(String[] args) {
        //创建配置信息
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.220.100:9092");//kafka地址，多个地址用逗号分割
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);


        //创建生产者对象
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        //发送数据
        for (int i=0;i<10;i++) {
            //回调函数，该方法会在 Producer 收到 ack 时调用，为异步调用
            producer.send(new ProducerRecord<String, String>(topic ,Integer.toString(i), Integer.toString(i)), (metadata, exception) -> {
                //如果key为确定值，那么分区也就确定了
                if (exception == null) {
                    System.out.println("success->" +
                            "   partition = " +metadata.partition()+",offset = "+metadata.offset());
                } else {
                    exception.printStackTrace();
                } });  //在send方法后接着调用get方法 就是有序的同步发送消息
        }


        producer.close();
    }
}

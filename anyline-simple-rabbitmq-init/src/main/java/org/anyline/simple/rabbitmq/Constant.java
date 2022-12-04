package org.anyline.simple.rabbitmq;

public class Constant {
    public static String QUEUE_SIMPLE = "queue_simple";

    //广播
    public static String QUEUE_FANOUT_MAIL = "queue_fanout_mail";
    public static String QUEUE_FANOUT_SMS = "queue_fanout_sms";
    public static String EXCHANGE_FANOUT = "exchange_fanout";



    //路由
    public static String QUEUE_ROUTING_MAIL = "queue_routing_mail";
    public static String QUEUE_ROUTING_SMS = "queue_routing_sms";
    public static String EXCHANGE_ROUTING = "exchange_routing";


    //正则路由
    public static String QUEUE_TOPIC_MAIL = "queue_topic_mail";
    public static String QUEUE_TOPIC_SMS = "queue_topic_sms";
    public static String EXCHANGE_TOPIC = "exchange_topic";


    //header
    public static String QUEUE_HEADER_MAIL = "queue_header_mail";
    public static String QUEUE_HEADER_SMS = "queue_header_sms";
    public static String EXCHANGE_HEADER = "exchange_header";
}

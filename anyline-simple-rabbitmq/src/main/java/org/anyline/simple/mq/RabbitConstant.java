package org.anyline.simple.mq;

/**
 * <p>
 * RabbitMQ常量池
 * </p>
 */
public interface RabbitConstant {


    // 交换机名称(车辆GPS)
    String XIZHENG_DIRECT_EXCHANGE = "XIZHENG_DIRECT_EXCHANGE";

    // 转发支队达梦队列——出租车gps
    String RECIVE_TAXIGPS_TRANSMIT_ZDDM = "RECIVE_TAXIGPS_TRANSMIT_ZDDM";
    // 转发支队达梦队列——出租车gps绑定key
    String RECIVE_TAXIGPS_TRANSMIT_ZDDM_KEY = "RECIVE_TAXIGPS_TRANSMIT_ZDDM_KEY";
    // 转发支队达梦队列——网约车gps
    String RECIVE_NETCARGPS_TRANSMIT_ZDDM = "RECIVE_NETCARGPS_TRANSMIT_ZDDM";
    // 转发支队达梦队列——网约车gps绑定key
    String RECIVE_NETCARGPS_TRANSMIT_ZDDM_KEY = "RECIVE_NETCARGPS_TRANSMIT_ZDDM_KEY";
    // 转发支队达梦列——两客一危gps
    String RECIVE_LKYWGPS_TRANSMIT_ZDDM = "RECIVE_LKYWGPS_TRANSMIT_ZDDM";
    // 转发支队达梦列——两客一危gps绑定key
    String RECIVE_LKYWGPS_TRANSMIT_ZDDM_KEY = "RECIVE_LKYWGPS_TRANSMIT_ZDDM_KEY";

    // 转发支队达梦列——两客一危gps
    String RECIVE_AIS_TRANSMIT_ZDDM = "RECIVE_AIS_TRANSMIT_ZDDM";
    // 转发支队达梦列——两客一危gps绑定key
    String RECIVE_AIS_TRANSMIT_ZDDM_KEY = "RECIVE_AIS_TRANSMIT_ZDDM_KEY";


}

package org.anyline.simple.mq;

import com.rabbitmq.client.Channel;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitHandlerDM {

    @Autowired
    private AnylineService service;
    private static DataSet taxiList = new DataSet();
    private static DataSet netList = new DataSet();
    private static DataSet lkywList = new DataSet();

    int vol = 20;
    //出租车
    @RabbitHandler
    @RabbitListener(queues = RabbitConstant.RECIVE_TAXIGPS_TRANSMIT_ZDDM, containerFactory = "tpscFactory", concurrency = "1")
    public synchronized void taxi(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            String json = new String(message.getBody());
            DataRow row = DataRow.parseJson(json);
            row.replace("BCSJ","T"," ");
            row.replace("GPSSJ","T"," ");
            row.replace("SUBMITTIME","T"," ");
            row.put("RECID", System.currentTimeMillis()+"-"+ BasicUtil.getRandomNumberString(6));
            synchronized (taxiList) {
                taxiList.add(row);
                if (taxiList.size() >= vol) {
                    try {
                        service.insert("traffic.BD_RTRANS_TAXIGPSINFO", taxiList);
                    }catch (Exception e){}
                    taxiList = new DataSet();
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


    //风约车
    @RabbitHandler
    @RabbitListener(queues = RabbitConstant.RECIVE_NETCARGPS_TRANSMIT_ZDDM, containerFactory = "tpscFactory", concurrency = "1")
    public synchronized void net(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            String json = new String(message.getBody());
            DataRow row = DataRow.parseJson(json);
            row.replace("gpstime","T"," ");
            row.replace("submittime","T"," ");
            row.put("RECID", System.currentTimeMillis()+"-"+BasicUtil.getRandomNumberString(6));
            synchronized (netList) {
                netList.add(row);
                if (netList.size() >= vol) {
                    try {
                        service.insert("traffic.BD_RTRANS_NETCARGPSINFO", netList);
                    }catch (Exception e){}
                    netList = new DataSet();
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


    //两客一危
    @RabbitHandler
    @RabbitListener(queues = RabbitConstant.RECIVE_LKYWGPS_TRANSMIT_ZDDM, containerFactory = "tpscFactory", concurrency = "1")
    public  synchronized void lkyw(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            String json = new String(message.getBody());
            DataRow row = DataRow.parseJson(json);
            row.replace("gpstime","T"," ");
            row.replace("submittime","T"," ");
            row.put("RECID", System.currentTimeMillis()+"-"+BasicUtil.getRandomNumberString(6));
            synchronized (lkywList) {
                lkywList.add(row);
                if (lkywList.size() >= vol) {
                    try {
                        service.insert("traffic.BD_RTRANS_LKYWGPSINFO", lkywList);
                    }catch (Exception e){}
                    lkywList = new DataSet();
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

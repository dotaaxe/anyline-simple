package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.anyline.entity.DataRow;
import org.anyline.util.DateUtil;
import org.springframework.stereotype.Component;

import static org.anyline.simple.rabbitmq.Constant.QUEUE_SIMPLE;

@Component
public class ProducerSimple {

    private Channel channel;
    public ProducerSimple() {
        try {
            Connection connection = ConnectionUtil.connection();
            channel = connection.createChannel();
            //声明队列(如果队列不存在则创建)
            /*
             *1、queue 队列名称
             *2、durable 是合持久化，如果持久化，wn重肩片M列还在
             *3、exclusive 是否独占连接，从列只允许在当前连接中访问，如果connection连接关闭队列则自动删院,如果参数设置true可用于临时队列的创建
             *4、autoDelete白动删除，以列不再使用时是否白动删除除此队列，如果此参数和exclusive参数设置为true就可以实现临时队列(队列不用了就自动删除)
             *5、args 参数，可以设置一个队列的扩展参数，比如，可设置存活时问
             */
            channel.queueDeclare(QUEUE_SIMPLE, true, false, false, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void send() throws Exception{

        //不设置交换机(也就是使用默认交换机)
        /*
         *1、 exchange 交换机 不指定则使用默认交换机
         *2、 routingKey 路由routingKey 交换机根据key把消息发到指定队列,如果使用默认交换机 routingKey设置成队列名称
         *3、 props 消息属性
         *4、 body 消息体
         * */

        DataRow msg = new DataRow();
        for(int i=0; i<10; i++) {
            msg.put("FLAG", System.currentTimeMillis());
            msg.put("LOCATION_TIME", DateUtil.format());
            channel.basicPublish("", QUEUE_SIMPLE, null, msg.getJson().getBytes());
            System.out.println("send:" + msg.getJson());
        }

    }
}

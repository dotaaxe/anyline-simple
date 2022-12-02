package org.anyline.simple.rabbitmq;

import com.rabbitmq.client.*;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Consumer {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    public void receive(String msg){
        System.out.println("receive:"+msg);
        DataRow row = DataRow.parseJson(msg);
        service.insert("mq_msg", row);
    }
}
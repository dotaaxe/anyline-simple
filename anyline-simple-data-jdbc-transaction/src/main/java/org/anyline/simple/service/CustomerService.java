package org.anyline.simple.service;

import org.anyline.entity.DataRow;
import org.anyline.service.init.SimpleService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("cust.service")
public class CustomerService extends SimpleService {
    @Transactional
    public void insert(DataRow row){
        insert("<crm>crm_customer", row);
        throw new RuntimeException("test exception");
    }
}
package org.anyline.simple.service;

import org.anyline.entity.DataRow;
import org.anyline.service.init.DefaultService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("crm.service")
public class CRMService extends DefaultService {
    @Transactional(value = "anyline.transaction.crm")
    public void insert(DataRow row){
        insert("<crm>crm_customer", row);
        throw new RuntimeException("test exception");
    }
}

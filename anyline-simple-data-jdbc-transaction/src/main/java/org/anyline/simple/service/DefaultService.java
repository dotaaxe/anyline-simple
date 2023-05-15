package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("def.service")
public class DefaultService extends org.anyline.service.init.DefaultService {
    @Transactional
    public void insert(DataRow row){
        insert("HR_EMPLOYEE", row);
        throw new RuntimeException("test exception");
    }
}

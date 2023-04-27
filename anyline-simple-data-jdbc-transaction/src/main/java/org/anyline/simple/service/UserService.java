package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.anyline.service.init.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("user.service")
public class UserService {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    @Transactional
    public void insert(DataRow row){
        DataSourceHolder.setDataSource("sso");
        service.insert("SSO_USER", row);
        throw new RuntimeException("test exception");
    }
}

package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("sso.service")
public class SSOService {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    @Transactional(value = "anyline.transaction.sso")
    public void insert(DataRow row){
        try {
            DataSourceHolder.setDataSource("sso");
            service.insert("SSO_USER", row);
            DataSourceHolder.setDefaultDataSource();
        }catch (Exception e){

        }
        throw new RuntimeException("test exception");
    }
    public long count(String table){
        return service.count(table);
    }
}

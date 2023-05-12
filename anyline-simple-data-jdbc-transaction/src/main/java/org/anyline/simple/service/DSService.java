package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("ds.service")
public class DSService {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    public void insert(String ds, DataRow row){
        try {
            DataSourceHolder.setDataSource(ds);
            service.insert("SSO_USER", row);
        }catch (Exception e){

        }
        throw new RuntimeException("test exception");
    }
    public int count(String table){
        return service.count(table);
    }
}

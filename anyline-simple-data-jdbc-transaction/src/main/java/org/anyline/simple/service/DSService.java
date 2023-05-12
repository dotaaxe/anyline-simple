package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

@Component("ds.service")
public class DSService {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    public void insert(String ds, DataRow row, int flag){
        DataSourceHolder.setDataSource(ds);
        TransactionStatus status = DataSourceHolder.startTransaction();
        service.insert("SSO_USER", row);
        if(flag == 0) {
            DataSourceHolder.rollback(status);
        }else{
            DataSourceHolder.commit(status);
        }
    }

    public int count(String table){
        return service.count(table);
    }
}

package org.anyline.simple.service;

import org.anyline.data.jdbc.ds.DataSourceHolder;
import org.anyline.entity.DataRow;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

@Component("ds.service")
public class DSService {

    @Autowired(required = false)
    @Qualifier("anyline.service")
    protected AnylineService service;

    //通用数据源(就是可以执行多个数据源，可切换)
    public void insert(String ds, DataRow row){
        DataSourceHolder.setDataSource(ds);
        TransactionStatus status = DataSourceHolder.startTransaction();//这里没有指定数据源 会默认当前数据源
        try {
            service.insert("SSO_USER", row);
            DataSourceHolder.commit(status);//这里不需要指定数据源，因为status已经绑定了数据源
        }catch (Exception e){
            DataSourceHolder.rollback(status);
        }
    }

    //固定数据源(就是为每个数据源生成独立的jdbcTemplate,事务管理器,dao,service)
    //推荐这种方式，可以避免线程共享的引起的切换失败问题
    public void inserts(String ds, DataRow row){
        AnylineService service = ServiceProxy.service(ds); //返回操作固定数据源的service
        TransactionStatus status = DataSourceHolder.startTransaction(ds);
        try {
            service.insert("SSO_USER", row);
            DataSourceHolder.commit(status); //这里不需要指定数据源，因为status已经绑定了数据源
        }catch (Exception e){

            DataSourceHolder.rollback(status);
        }
    }
    public long count(String table){
        return service.count(table);
    }
}

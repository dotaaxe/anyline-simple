package org.anyline.simple.noweb;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.jdbc.ds.DataSourceHolder;
import org.anyline.service.AnylineService;
import org.anyline.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduleTask {
    private static Logger log = LoggerFactory.getLogger(ScheduleTask.class);
    @Autowired
    private AnylineService service;

    @Scheduled(cron="0 * * * * ?")
    private synchronized void scan(){


        //检测数据源
        DataSourceHolder.setDefaultDataSource();
        DataSet datasources = service.querys("BS_DATASOURCE");
        for(DataRow ds:datasources){
            String code = ds.getCode();
            String driver = ds.getString("DRIVER");
            String url = ds.getString("URL");
            String account = ds.getString("ACCOUNT");
            String password = ds.getString("PASSWORD");
            try {
                if(!DataSourceHolder.contains(code)) {
                    DataSourceHolder.reg(code, "com.zaxxer.hikari.HikariDataSource", driver, url, account, password);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

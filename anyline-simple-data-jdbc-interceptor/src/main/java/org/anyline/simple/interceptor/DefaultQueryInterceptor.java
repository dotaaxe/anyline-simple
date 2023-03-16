package org.anyline.simple.interceptor;

import org.anyline.data.interceptor.QueryInterceptor;
import org.anyline.data.jdbc.adapter.JDBCAdapter;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.Condition;
import org.anyline.data.prepare.Procedure;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.data.run.Run;
import org.anyline.entity.DataSet;
import org.anyline.entity.EntitySet;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class DefaultQueryInterceptor implements QueryInterceptor {


    @Override
    public int before(JDBCAdapter adapter, RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID>0");
        return 0;
    }

    @Override
    public int before(Procedure procedure) {
        return 0;
    }

    @Override
    public int after(Run run, List<?> maps, long millis) {
        return 0;
    }

    @Override
    public int after(Run run, EntitySet<?> maps, long millis) {
        return 0;
    }

    @Override
    public int after(Run run, DataSet set, long millis) {
        return 0;
    }

    @Override
    public int after(Procedure procedure, DataSet set, long millis) {
        return 0;
    }

    @Override
    public int after(Run run, int count, long millis) {
        return 0;
    }
}

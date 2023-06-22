package org.anyline.simple.interceptor;

import org.anyline.data.interceptor.QueryInterceptor;
import org.anyline.data.jdbc.ds.JDBCRuntime;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.data.run.Run;
import org.anyline.entity.PageNavi;
import org.anyline.entity.data.ACTION;
import org.anyline.entity.data.Parameter;
import org.anyline.entity.data.Procedure;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DMInterceptor implements QueryInterceptor {

    @Override
    public ACTION.SWITCH prepare(JDBCRuntime runtime, RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID", 1);
        return ACTION.SWITCH.CONTINUE;
    }

    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, Run run, PageNavi navi) {
        return QueryInterceptor.super.before(runtime, run, navi);
    }

    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, Procedure procedure, List<Parameter> inputs, List<Parameter> outputs, PageNavi navi) {
        return QueryInterceptor.super.before(runtime, procedure, inputs, outputs, navi);
    }

    @Override
    public ACTION.SWITCH after(JDBCRuntime runtime, Run run, boolean success, Object result, PageNavi navi, long millis) {
        return QueryInterceptor.super.after(runtime, run, success, result, navi, millis);
    }

    @Override
    public ACTION.SWITCH after(JDBCRuntime runtime, Procedure procedure, List<Parameter> inputs, List<Parameter> outputs, PageNavi navi, boolean success, Object result, long millis) {
        return QueryInterceptor.super.after(runtime, procedure, inputs, outputs, navi, success, result, millis);
    }
}

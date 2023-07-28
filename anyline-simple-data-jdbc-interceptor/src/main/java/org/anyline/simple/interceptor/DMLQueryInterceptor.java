package org.anyline.simple.interceptor;

import org.anyline.data.jdbc.ds.JDBCRuntime;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.data.run.Run;
import org.anyline.entity.PageNavi;
import org.anyline.metadata.ACTION;
import org.anyline.metadata.Parameter;
import org.anyline.metadata.Procedure;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DMLQueryInterceptor implements org.anyline.data.interceptor.QueryInterceptor {

    @Override
    public ACTION.SWITCH prepare(JDBCRuntime runtime, RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID", 1);
        return ACTION.SWITCH.CONTINUE;
    }

    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, Run run, PageNavi navi) {
        return org.anyline.data.interceptor.QueryInterceptor.super.before(runtime, run, navi);
    }

    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, Procedure procedure, List<Parameter> inputs, List<Parameter> outputs, PageNavi navi) {
        return org.anyline.data.interceptor.QueryInterceptor.super.before(runtime, procedure, inputs, outputs, navi);
    }

    @Override
    public ACTION.SWITCH after(JDBCRuntime runtime, Run run, boolean success, Object result, PageNavi navi, long millis) {
        return org.anyline.data.interceptor.QueryInterceptor.super.after(runtime, run, success, result, navi, millis);
    }

    @Override
    public ACTION.SWITCH after(JDBCRuntime runtime, Procedure procedure, List<Parameter> inputs, List<Parameter> outputs, PageNavi navi, boolean success, Object result, long millis) {
        return org.anyline.data.interceptor.QueryInterceptor.super.after(runtime, procedure, inputs, outputs, navi, success, result, millis);
    }
}
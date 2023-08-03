package org.anyline.simple.interceptor;

import org.anyline.data.interceptor.QueryInterceptor;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.data.runtime.DataRuntime;
import org.anyline.metadata.ACTION;
import org.springframework.stereotype.Component;


@Component
public class DMLQueryInterceptor implements QueryInterceptor {

    @Override
    public ACTION.SWITCH prepare(DataRuntime runtime, RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID", 1);
        return ACTION.SWITCH.CONTINUE;
    }


}

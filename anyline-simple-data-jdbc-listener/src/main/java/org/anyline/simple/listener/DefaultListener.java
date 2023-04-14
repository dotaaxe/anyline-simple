package org.anyline.simple.listener;

import org.anyline.data.listener.init.BasicDMListener;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.springframework.stereotype.Component;

@Component()
public class DefaultListener extends BasicDMListener {
    @Override
    public boolean beforeBuildQuery(RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID > 1");
        return true;
    }
}

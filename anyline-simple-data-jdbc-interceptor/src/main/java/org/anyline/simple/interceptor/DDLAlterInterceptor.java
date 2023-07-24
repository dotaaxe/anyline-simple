package org.anyline.simple.interceptor;

import org.anyline.data.interceptor.DDInterceptor;
import org.anyline.data.jdbc.ds.JDBCRuntime;
import org.anyline.data.run.Run;
import org.anyline.metadata.ACTION;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DDLAlterInterceptor implements DDInterceptor {
    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, String random, ACTION.DDL action, Object metadata, List<Run> runs) {
        for (Run run:runs){
            String sql = run.getFinalUpdate();
            System.out.println("--------------DDLAlterInterceptor("+action+")-------------------\n"+sql);
        }
        return ACTION.SWITCH.CONTINUE;
    }

    @Override
    public List<ACTION.DDL> actions() {
        List<ACTION.DDL> list = new ArrayList<>();
        list.add(ACTION.DDL.COLUMN_ALTER);
        list.add(ACTION.DDL.COLUMN_RENAME);
        return list;
    }
}

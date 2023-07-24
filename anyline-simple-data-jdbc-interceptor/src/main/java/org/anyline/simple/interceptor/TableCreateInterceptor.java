package org.anyline.simple.interceptor;

import org.anyline.data.interceptor.DDInterceptor;
import org.anyline.data.jdbc.ds.JDBCRuntime;
import org.anyline.data.run.Run;
import org.anyline.metadata.ACTION;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TableCreateInterceptor implements DDInterceptor {

    /**
     * 拦截哪一个DDL事件，如果需要拦截多个可以写多个拦截器也可以实现 List<ACTION.DDL> actions()
     * @return
     */
    /*@Override
    public ACTION.DDL action() {
        return ACTION.DDL.TABLE_CREATE;
    }*/
    @Override
    public List<ACTION.DDL> actions() {
        List<ACTION.DDL> list = new ArrayList<>();
        list.add(ACTION.DDL.TABLE_CREATE);
        return list;
    }

    /**
     * 输出SQL到这一步 到这一步SQL已经造成完成不可以修改了
     * @param runtime  包含数据源(key)、适配器、JDBCTemplate、dao
     * @param random 用来标记同一组SQL、执行结构、参数等
     * @param action 执行命令
     * @param metadata table/column等
     * @param runs 需要执行的SQL 有些命令需要多条SQL完成
     * @return ACTION.SWITCH
     */
    @Override
    public ACTION.SWITCH before(JDBCRuntime runtime, String random, ACTION.DDL action, Object metadata, List<Run> runs) {
        for (Run run:runs){
            String sql = run.getFinalUpdate();
            System.out.println("--------------TableCreateInterceptor("+action+")-------------------\n"+sql);
        }
        return ACTION.SWITCH.CONTINUE;
    }

    @Override
    public ACTION.SWITCH after(JDBCRuntime runtime, String random, ACTION.DDL action, Object metadata, List<Run> runs, boolean result, long millis) {
        return DDInterceptor.super.after(runtime, random, action, metadata, runs, result, millis);
    }

}

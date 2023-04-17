package org.anyline.simple.listener;

import org.anyline.data.listener.init.BasicDMListener;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.prepare.RunPrepare;
import org.anyline.data.run.Run;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component()
public class DefaultListener extends BasicDMListener {


    /**
     * 创建插入相关的SQL之前调用<br/>
     * 要修改插入内容可以在这一步实现,注意不是在beforeInsert
     * @param dest 表
     * @param obj 实体
     * @param checkPrimary 是否需要检查重复主键,默认不检查
     * @param columns 需要抛入的列 如果不指定  则根据实体属性解析
     * @return 如果返回false 则中断执行
     */
    public boolean beforeBuildInsert(String dest, Object obj, boolean checkPrimary, List<String> columns){
        if(obj instanceof DataRow){
            DataRow row = (DataRow)obj;
            row.put("REG_TIME", DateUtil.format());
        }
        return true;
    }
    /**
     * 创建查相关的SQL之前调用,包括slect exists count等<br/>
     * 要修改查询条件可以在这一步实现,注意不是在beforeQuery
     * @param prepare  prepare
     * @param configs 查询条件配置
     * @param conditions 查询条件
     * @return 如果返回false 则中断执行
     */
    @Override
    public boolean beforeBuildQuery(RunPrepare prepare, ConfigStore configs, String... conditions) {
        configs.and("ID > 1");
        return true;
    }
    /**
     * 创建更新相关的SQL之前调用<br/>
     * 要修改更新内容或条件可以在这一步实现,注意不是在beforeUpdate
     * @param dest 表
     * @param obj Entity或DtaRow
     * @param checkPrimary 是否需要检查重复主键,默认不检查
     * @param columns 需要更新的列
     * @param configs 更新条件
     * @return 如果返回false 则中断执行
     */
    @Override
    public boolean beforeBuildUpdate(String dest, Object obj, ConfigStore configs, boolean checkPrimary, List<String> columns) {
        if(obj instanceof DataRow){
            DataRow row = (DataRow)obj;
            row.put("UPT_TIME", DateUtil.format());
        }
        return true;
    }
    /**
     * 创建删除SQL前调用(根据Entity/DataRow),修改删除条件可以在这一步实现<br/>
     * 注意不是beforeDelete<br/>
     * 注意beforeBuildDelete有两个函数需要实现<br/>
     * service.delete(DataRow);
     * @param dest 表
     * @param obj entity或DataRow
     * @param columns 删除条件的我
     * @return 如果返回false 则中断执行
     */
    public boolean beforeBuildDelete(String dest, Object obj, String ... columns){
        if(obj instanceof DataRow){
            DataRow row = (DataRow)obj;
            row.put("UPT_TIME", DateUtil.format());
            if(row.getInt("ROLE_ID", 0) == 99){
                return false;
            }
        }
        return true;
    }
    /**
     * 创建删除SQL前调用(根据条件),修改删除条件可以在这一步实现<br/>
     * 注意不是beforeDelete<br/>
     * 注意beforeBuildDelete有两个函数需要实现<br/>
     * service.delete("CRM_USER", "ID", "1", "2", "3");
     * @param table 表
     * @param key key
     * @param values values
     * @return 如果返回false 则中断执行
     */
    public boolean beforeBuildDelete(String table, String key, Object values){
        return false;
    }

    @Override
    public void afterQuery(Run run, DataSet set, long millis) {
        System.out.println(run.getFinalQuery());
        System.out.println(run.getValues());
    }
}

package org.anyline.simple.entity;

import org.anyline.adapter.EntityAdapter;
import org.anyline.metadata.Column;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

@Component
public class EmployeeAdapter implements EntityAdapter {
    public Class type(){
        return Employee.class;
    }
    @Override
    public Column column(Class clazz, Field field, String... annotations) {
        System.out.println("[class:"+clazz+"][field:"+field+"][在这里实现file>column]");
        return EntityAdapter.super.column(clazz, field, annotations);
    }

    @Override
    public LinkedHashMap<String, Column> columns(Class clazz, MODE mode) {
        LinkedHashMap<String, Column> columns = EntityAdapter.super.columns(clazz, mode);
        if(mode == MODE.UPDATE){ //UPDATE 时不更新REG_ID
            columns.remove("REG_ID");
        }
        return columns;
    }
}

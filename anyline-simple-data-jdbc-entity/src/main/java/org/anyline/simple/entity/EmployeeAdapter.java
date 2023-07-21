package org.anyline.simple.entity;

import org.anyline.adapter.EntityAdapter;
import org.anyline.metadata.Column;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class EmployeeAdapter implements EntityAdapter {
    public Class type(){
        return Employee.class;
    }
    @Override
    public Column column(Class clazz, Field field, String... annotations) {
        System.out.print("[class:"+clazz+"][field:"+field+"][在这里转换成列名]");
        return EntityAdapter.super.column(clazz, field, annotations);
    }
}

package org.anyline.simple.trans.service;

import org.anyline.entity.DataRow;
import org.anyline.service.impl.AnylineServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("emp.service")
public class EmployeeService extends AnylineServiceImpl {

    @Transactional
    public void insert(DataRow row){
        insert("HR_EMPLOYEE", row);
        throw  new RuntimeException("test RuntimeException");
    }
}

package org.anyline.simple.service;

import org.anyline.entity.DataRow;
import org.anyline.service.init.SimpleService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("emp.service")
public class EmployeeService extends SimpleService {
    @Transactional
    public void insert(DataRow row){
        insert("HR_EMPLOYEE", row);
        throw new RuntimeException("test exception");
    }
}
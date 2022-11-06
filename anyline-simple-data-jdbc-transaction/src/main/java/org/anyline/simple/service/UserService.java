package org.anyline.simple.service;

import org.anyline.entity.DataRow;
import org.anyline.service.init.DefaultService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("user.service")
public class UserService extends DefaultService {
    @Transactional
    public void insert(DataRow row){
        insert("<sso>SSO_USER", row);
        throw new RuntimeException("test exception");
    }
}

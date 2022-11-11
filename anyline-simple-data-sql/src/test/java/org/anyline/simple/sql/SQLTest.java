package org.anyline.simple.sql;

import org.anyline.service.AnylineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SQLTest {

    @Autowired
    private AnylineService service;
    @Test
    public void init() throws Exception{
        String sql = "SELECT ";
    }
}

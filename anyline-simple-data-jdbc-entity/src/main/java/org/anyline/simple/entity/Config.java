package org.anyline.simple.entity;

import org.anyline.entity.metadata.Convert;
import org.anyline.util.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean("sqlDate2LocalDate11")
    public Convert sqlDate2LocalDate(){
        Convert convert = new Convert() {
            @Override
            public Class getOrigin() {
                return java.sql.Date.class;
            }

            @Override
            public Class getTarget() {
                return java.time.LocalDate.class;
            }

            @Override
            public Object exe(Object value, Object def) {
                java.util.Date date = DateUtil.parse(value);
                return DateUtil.localDate(date);
            }
        };
        return convert;
    }

}

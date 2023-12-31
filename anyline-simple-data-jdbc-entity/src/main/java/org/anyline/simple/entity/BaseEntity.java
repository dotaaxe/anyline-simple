package org.anyline.simple.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="HR_EMPLOYEE")
public abstract class BaseEntity implements Serializable {
    public static String CREATE_TIME = "create_time";
    private Long id;
    @Column(name = "REG_TIME", updatable = false)
    public String create_time;

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String createTime) {
        this.create_time = createTime;
    }
}

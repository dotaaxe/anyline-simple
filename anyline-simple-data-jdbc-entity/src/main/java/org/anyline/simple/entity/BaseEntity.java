package org.anyline.simple.entity;

import javax.persistence.Column;
import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

    @Column(name = "REG_TIME", updatable = false)
    public String create_time;

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String createTime) {
        this.create_time = createTime;
    }
}

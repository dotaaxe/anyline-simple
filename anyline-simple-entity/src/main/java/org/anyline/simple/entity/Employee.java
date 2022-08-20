package org.anyline.simple.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
@Table(name="hr_employee")
public class Employee {
    private Long id;
    @Column(name = "NM")
    private String name;
    @Transient
    private int age;

    //@Column(name = "JOIN_YMD")
    private String joinYmd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJoinYmd() {
        return joinYmd;
    }

    public void setJoinYmd(String joinYmd) {
        this.joinYmd = joinYmd;
    }
}

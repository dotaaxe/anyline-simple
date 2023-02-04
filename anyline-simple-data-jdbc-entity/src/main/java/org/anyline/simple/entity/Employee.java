package org.anyline.simple.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Table(name="hr_employee")
public class Employee extends BaseEntity{
    private Long id;
    @Column(name = "NM")
    private String name;
    @Transient
    private int age;
    //对应数据库blob类型
    private byte[] remark;
    //对应数据库blob类型
    private String dblob;
    //对应数据库中的json类型
    private String djson;

    private Department ejson;

    //如果属性上没有注解会 会根据 ConfigTable.ENTITY_FIELD_COLUMN_MAP 进程转换;
    //默认"camel_"属性小驼峰转下划线 joinYmd > join_ymd

    //@Column(name = "JOIN_YMD")
    private String joinYmd;
    private List<Department> ejsons;

    public List<Department> getEjsons() {
        return ejsons;
    }

    public void setEjsons(List<Department> ejsons) {
        this.ejsons = ejsons;
    }

    public String getDblob() {
        return dblob;
    }

    public void setDblob(String dblob) {
        this.dblob = dblob;
    }

    public byte[] getRemark() {
        return remark;
    }

    public void setRemark(byte[] remark) {
        this.remark = remark;
    }

    public String getDjson() {
        return djson;
    }

    public void setDjson(String djson) {
        this.djson = djson;
    }

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

    public Department getEjson() {
        return ejson;
    }

    public void setEjson(Department ejson) {
        this.ejson = ejson;
    }
}

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
    private String des;
    //对应数据库中的json类型
    private String sdepartment;

    private Department department;

    //@Column(name = "JOIN_YMD")
    private String joinYmd;
    private List<Department> departments;

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public byte[] getRemark() {
        return remark;
    }

    public void setRemark(byte[] remark) {
        this.remark = remark;
    }

    public String getSdepartment() {
        return sdepartment;
    }

    public void setSdepartment(String sdepartment) {
        this.sdepartment = sdepartment;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

}

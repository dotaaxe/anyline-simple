package org.anyline.simple.entity;

import org.anyline.entity.Point;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.List;

@Table(name="hr_employee")
public class Employee extends BaseEntity{

    private Long id;
    @Column(name = "NM")
    private String name;
    private String userName;
    //对应date类型
    private LocalDate ymd;
    @Transient
    private int age;
    //对应数据库blob类型
    private byte[] remark;
    //对应数据库blob类型
    private String dblob;
    private Department ejson;
    //这个属性在数据库中不存在
    private String tmpCol;

    // 对应数据库中的json类型 注意这里不要用String接收 否则在返回给前端调用toJson时会把引号 转义
    // 应该根据json格式定义一个类，如果不想定义可以用Object类型(会实例化一个LinkedHashMap赋值给Object)
    private Object djson;


    //如果属性上没有注解会 会根据 ConfigTable.ENTITY_FIELD_COLUMN_MAP 进程转换;
    //默认"camel_"属性小驼峰转下划线 joinYmd > join_ymd

    private String joinYmd;
    private List<Department> ejsons;
    //对应数据类型point
    private Double[] loc;
    //对应数据类型point
    private Point pt;




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

    public String getTmpCol() {
        return tmpCol;
    }

    public void setTmpCol(String tmpCol) {
        this.tmpCol = tmpCol;
    }

    public Object getDjson() {
        return djson;
    }

    public void setDjson(Object djson) {
        this.djson = djson;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getYmd() {
        return ymd;
    }

    public void setYmd(LocalDate ymd) {
        this.ymd = ymd;
    }

    public Point getPt() {
        return pt;
    }

    public void setPt(Point pt) {
        this.pt = pt;
    }

    public Double[] getLoc() {
        return loc;
    }

    public void setLoc(Double[] loc) {
        this.loc = loc;
    }
}

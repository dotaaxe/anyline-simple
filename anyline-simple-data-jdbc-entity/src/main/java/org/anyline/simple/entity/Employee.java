package org.anyline.simple.entity;

import org.anyline.entity.Point;
import org.anyline.util.ClassUtil;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Table(name="hr_employee")
public class Employee extends BaseEntity{

    private Long id;

    @Column(name = "NAME", length = 10)
    private String nm;

    @Column(name = "CODE", length = 10)
    private String workCode;
    //对应date类型
    private LocalDate birthday;

    //如果属性上没有注解会 会根据 ConfigTable.ENTITY_FIELD_COLUMN_MAP 转换;
    //默认"camel_"属性小驼峰转下划线 joinYmd > join_ymd
    private String joinYmd;

    //这一列在数据库中没有
    @Transient
    private int age;

    private float salary;

    //对应数据库blob类型
    private byte[] remark;

    //对应数据库blob类型
    private String description;

    //对应数据库JSON
    private Department department;
    private Map map;

    //这个属性在数据库中不存在
    private String tmpCol;

    // 对应数据库中的json类型 注意这里不要用String接收 否则在返回给前端调用toJson时会把引号 转义
    // 应该根据json格式定义一个类，如果不想定义可以用Object类型(会实例化一个LinkedHashMap赋值给Object)
    private Object other;


    //工作经历 对应[json]集合类型
    private List<Experience> experiences;
    //职务 对应{json}类型
    private Map<String,Post> posts;

    //头衔 对应[json]类型
    private List<String> titles;
    private String[] labels;
    //对应数据类型point
    private Double[] workLocation;

    //对应数据类型point
    private Point homeLocation;

    //多对多关系  一个在多个部门任职
    @ManyToMany
    @JoinTable(name = "HR_EMPLOYEE_DEPARTMENT"                          //中间关联表
            , joinColumns = @JoinColumn(name="EMPLOYEE_ID")             //关联表中与当前表关联的外键
            , inverseJoinColumns = @JoinColumn(name="DEPARTMENT_ID"))   //关联表中与当前表关联的外键
    private List<Department> departments;//查部门完整信息


    @ManyToMany
    @JoinTable(name = "HR_EMPLOYEE_DEPARTMENT"                          //中间关联表
            , joinColumns = @JoinColumn(name="EMPLOYEE_ID")             //关联表中与当前表关联的外键
            , inverseJoinColumns = @JoinColumn(name="DEPARTMENT_ID"))   //关联表中与当前表关联的外键
    private List<Long> departmentIds;//只查部门主键

    public static void main(String[] args) throws Exception{

        Class clazz = Employee.class;
        List<Field> fields = ClassUtil.getFieldsByAnnotation(clazz, "ManyToMany");
        for(Field field:fields){
            String joinTable_name = ClassUtil.parseAnnotationFieldValue(field, "JoinTable.name");
            String joinColumn_name = null;
            String inverseJoinColumn_name = null;
            Annotation anJoinTable = ClassUtil.getFieldAnnotation(field, "JoinTable");
            if(null != anJoinTable) {
                Method methodJoinColumns = anJoinTable.annotationType().getMethod("joinColumns");
                if(null != methodJoinColumns) {
                    Object[] ojoinColumns = (Object[]) methodJoinColumns.invoke(anJoinTable);
                    if (null != ojoinColumns && ojoinColumns.length > 0) {
                        Annotation joinColumn = (Annotation) ojoinColumns[0];
                        joinColumn_name = (String) joinColumn.annotationType().getMethod("name").invoke(joinColumn);
                    }
                }
                Method methodInverseJoinColumns = anJoinTable.annotationType().getMethod("inverseJoinColumns");
                if(null != methodInverseJoinColumns){
                    Object[] ojoinColumns = (Object[]) methodInverseJoinColumns.invoke(anJoinTable);
                    if (null != ojoinColumns && ojoinColumns.length > 0) {
                        Annotation joinColumn = (Annotation) ojoinColumns[0];
                        inverseJoinColumn_name = (String) joinColumn.annotationType().getMethod("name").invoke(joinColumn);
                    }
                }
            }
            System.out.println(joinTable_name);
            System.out.println(joinColumn_name);
            System.out.println(inverseJoinColumn_name);
        }
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getJoinYmd() {
        return joinYmd;
    }

    public void setJoinYmd(String joinYmd) {
        this.joinYmd = joinYmd;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getRemark() {
        return remark;
    }

    public void setRemark(byte[] remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getTmpCol() {
        return tmpCol;
    }

    public void setTmpCol(String tmpCol) {
        this.tmpCol = tmpCol;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public Double[] getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(Double[] workLocation) {
        this.workLocation = workLocation;
    }

    public Point getHomeLocation() {
        return homeLocation;
    }

    public void setHomeLocation(Point homeLocation) {
        this.homeLocation = homeLocation;
    }

    public Map<String, Post> getPosts() {
        return posts;
    }

    public void setPosts(Map<String, Post> posts) {
        this.posts = posts;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

}

package org.anyline.simple.entity;

//不注解表名，找父类的
public class ChildEntity extends BaseEntity{
    private String nm;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}

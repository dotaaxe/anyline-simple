package org.anyline.simple.entity;

//不注解表名，找父类的
public class ChildEntity extends BaseEntity{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

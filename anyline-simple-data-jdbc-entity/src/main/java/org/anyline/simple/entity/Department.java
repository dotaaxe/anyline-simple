package org.anyline.simple.entity;

import javax.persistence.Table;

@Table(name="hr_department")
public class Department {
    private Long id;
    private String code;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department(){

    }
    public Department(Long id){
        this.id = id;
    }
    public Department(Long id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
    }
    public Department(String code, String name){
        this.code = code;
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

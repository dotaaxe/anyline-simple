package org.anyline.simple.entity;

import javax.persistence.Table;
import java.time.LocalTime;

@Table(name="hr_department")
public class Department {
    private Long id;
    private String code;
    private String name;
    private LocalTime localTime;

    public Long getId() {
        return id;
    }

    public Department setId(Long id) {
        this.id = id;
        return this;
    }

    public Department(){
        this.localTime = LocalTime.now();
    }
    public Department(Long id){
        this.id = id;
    }
    public Department(Long id, String code, String name){
        this.id = id;
        this.code = code;
        this.name = name;
        this.localTime = LocalTime.now();
    }
    public Department(String code, String name){
        this.code = code;
        this.name = name;
        this.localTime = LocalTime.now();
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

    public LocalTime getLocalTime() {
        return localTime;
    }

    public Department setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
}

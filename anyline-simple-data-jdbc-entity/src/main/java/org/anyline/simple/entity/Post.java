package org.anyline.simple.entity;

import java.util.Date;

public class Post {
    private String name;
    private double perk;
    private Date date;
    public Post(){

    }
    public Post(String name, double perk, Date date){
        this.name = name;
        this.perk = perk;
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPerk() {
        return perk;
    }

    public void setPerk(double perk) {
        this.perk = perk;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

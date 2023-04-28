package org.anyline.simple.entity;

import java.util.Date;

public class Experience {
    private Long id;
    private int index;
    private Date fr;
    private Date to;
    private String post;
    private String company;
    public Experience(){}
    public Experience(int index, Date fr, Date to, String post, String company){
        this.index = index;
        this.fr = fr;
        this.to = to;
        this.post = post;
        this.company = company;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getFr() {
        return fr;
    }

    public void setFr(Date fr) {
        this.fr = fr;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

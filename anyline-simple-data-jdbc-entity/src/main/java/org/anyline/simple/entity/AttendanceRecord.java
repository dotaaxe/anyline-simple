package org.anyline.simple.entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

//考勤记录
@Table(name="HR_ATTENDANCE_RECORD")
public class AttendanceRecord {
    private Long id;
    @Column(name = "EMPLOYEE_ID")
    private Long employee;
    private Date date;

    public AttendanceRecord(){
        this.date = new Date();
    }
    public AttendanceRecord(Date date){
        this.date = date;
    }
    public AttendanceRecord(Long id, Long employee, Date date){
        this.id = id;
        this.employee = employee;
        this.date = date;
    }
    public AttendanceRecord(Long employee, Date date){
        this.employee = employee;
        this.date = date;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployee() {
        return employee;
    }

    public void setEmployee(Long employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

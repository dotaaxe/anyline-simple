package org.anyline.simple.entity;


import javax.persistence.Table;
import java.util.Date;
//@Table(name="sync_task")
public class SyncTask {
    private Long id;
    private String code;

    private String srcDatasourceCode;

    private String tarDatasourceCode;

    private String srcTable;

    private String tarTable;

    private String cols;

    private Date lastExeTime;

    private String lastExeValue;

    private Long lastExeQty;

    private String pk;

    private String orderColumn;

    private Integer step;

    private Integer exeQty;

    private String exeException;

    private String idx;

    private String remark;
    //排序列类型 1date  2非date
    private Integer orderColumnType;

    private Date uptTime;

    private Date regTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSrcDatasourceCode() {
        return srcDatasourceCode;
    }

    public void setSrcDatasourceCode(String srcDatasourceCode) {
        this.srcDatasourceCode = srcDatasourceCode;
    }

    public String getTarDatasourceCode() {
        return tarDatasourceCode;
    }

    public void setTarDatasourceCode(String tarDatasourceCode) {
        this.tarDatasourceCode = tarDatasourceCode;
    }

    public String getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(String srcTable) {
        this.srcTable = srcTable;
    }

    public String getTarTable() {
        return tarTable;
    }

    public void setTarTable(String tarTable) {
        this.tarTable = tarTable;
    }

    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public Date getLastExeTime() {
        return lastExeTime;
    }

    public void setLastExeTime(Date lastExeTime) {
        this.lastExeTime = lastExeTime;
    }

    public String getLastExeValue() {
        return lastExeValue;
    }

    public void setLastExeValue(String lastExeValue) {
        this.lastExeValue = lastExeValue;
    }

    public Long getLastExeQty() {
        return lastExeQty;
    }

    public void setLastExeQty(Long lastExeQty) {
        this.lastExeQty = lastExeQty;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getExeQty() {
        return exeQty;
    }

    public void setExeQty(Integer exeQty) {
        this.exeQty = exeQty;
    }

    public String getExeException() {
        return exeException;
    }

    public void setExeException(String exeException) {
        this.exeException = exeException;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderColumnType() {
        return orderColumnType;
    }

    public void setOrderColumnType(Integer orderColumnType) {
        this.orderColumnType = orderColumnType;
    }

    public Date getUptTime() {
        return uptTime;
    }

    public void setUptTime(Date uptTime) {
        this.uptTime = uptTime;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}

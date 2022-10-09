package org.anyline.data.jdbc.mongodb;


public class DemoData {
    private String id;

    //标题
    private String name;

    //日期
    private String date;

    //数量
    private int doubleData;

    private String code;

    public DemoData() {
        super();
        // TODO Auto-generated constructor stub
    }
    public DemoData(String id, String name, String date, int doubleData, String code) {
        super();
        this.id = id;
        this.name = name;
        this.date = date;
        this.doubleData = doubleData;
        this.code = code;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getDoubleData() {
        return doubleData;
    }
    public void setDoubleData(int doubleData) {
        this.doubleData = doubleData;
    }
}

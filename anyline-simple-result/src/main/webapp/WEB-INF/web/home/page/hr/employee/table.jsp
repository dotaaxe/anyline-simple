<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
根据集合生成Table,指定表头、合并方式<br/>
unions.add("DEPARTMENT_NM");//部门相同时合并<br>
unions.add("JOIN_YMD(DEPARTMENT_NM)");//入职日期相同并且部门已合并时时合<br>
<pre>
List<String> keys = new ArrayList<>();
keys.add("{num}");
keys.add("DEPARTMENT_NM");
keys.add("JOIN_YMD");
keys.add("{DEPARTMENT_NM}-{NM}-{JOIN_YMD}");

List<String> unions = new ArrayList<>();
unions.add("DEPARTMENT_NM");
unions.add("JOIN_YMD(DEPARTMENT_NM)");

String table = TableBuilder.init()
        .setHeaders(headers).setFields(keys)
        .setUnions(unions).setDatas(set).setClazz("list")
        .build().build();
或者
String table = TableBuilder.init()
        .setUnions(unions).setDatas(set).setClazz("list")
        .addConfig("序号","{num}","100px", "text-align:center;")
        .addConfig("部门","DEPARTMENT_NM","100px", "text-align:center;")
        .addConfig("入职日期","JOIN_YMD","100px", "text-align:center;")
        .addConfig("工号","CODE","100px", "text-align:center;")
        .addConfig("姓名","NM","100px", "text-align:right;color:red;")
        .build().build();
</pre>
<hr/>
${html}
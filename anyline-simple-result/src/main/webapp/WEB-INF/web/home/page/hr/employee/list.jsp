<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<style>
    .al-chk-item-border{display: inline-block;text-align: left;}
</style>
<div class="query">
    <div class="item">
        <div class="label">部门</div>
        <div class="data">
            <al:checkbox data="${depts}" value="${param.dept}" name="dept"></al:checkbox>
        </div>
    </div>
    <div class="item">
        <div class="label">姓名</div>
        <div class="data">
            <input type="text" name="nm" placeholder="请输入姓名"  class="layui-input">
        </div>
    </div>
    <div class="layui-btn" onclick="fnGloableQuery()">查询</div>
    <a class="layui-btn" href="a">添加</a>
</div>
<table class="list">
    <tr class="list-head">
        <td>序号</td>
        <td>部门</td>
        <td>姓名</td>
        <td>入职日期</td>
        <td>操作</td>
    </tr>
    <tbody id="listBody">

    </tbody>
</table>
<div id="listPage"></div>

<script type="text/javascript">
    function fnGetParam() {
        var params = al.pack.param('.query');
        return params;
    }
</script>
<al:navi url="/web/home/hr/emp/navi" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="0" jump="true" stat="true"></al:navi>
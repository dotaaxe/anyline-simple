<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<script>
</script>
<div class="query">
	<div class="item">
		<div class="label">部门</div>
		<div class="data">
			<al:select data="${depts}" value="${param.dept}" head="选择部门" name="dept"></al:select>
		</div>
	</div>
	<div class="item">
		<div class="label">姓名</div>
		<div class="data">
			<input type="text" name="epln" placeholder="请输入姓名"  class="layui-input">
		</div>
	</div>
	<div class="item">
		<div class="label">范围 </div>
		<div class="data">
			<input type="number" name="fr" placeholder="最低" style="width:100px;">
			<input type="number" name="to" placeholder="最高" style="width:100px;">
		</div>
	</div>
	<div class="layui-btn" onclick="fnGloableQuery()">查询</div>

</div>
<table class="list">
		<tr class="list-head">
			<td>序号</td>
			<td>部门</td>
			<td>姓名</td>
			<td>年月</td>
			<td>基本工资</td>
			<td>奖金</td>
			<td>合计</td>
			<td>合计(K)</td>
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
<al:navi url="/web/home/hr/slr/navi" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="0" jump="true" stat="true"></al:navi>
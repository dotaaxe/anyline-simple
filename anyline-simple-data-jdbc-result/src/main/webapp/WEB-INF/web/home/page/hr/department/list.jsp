<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<script>
</script>
<div class="query">
	<div class="item">
		<div class="label">名称</div>
		<div class="data">
			<input type="text" name="nm" placeholder="请输入名称"  class="layui-input">
		</div>
	</div>
	<div class="layui-btn" onclick="fnGloableQuery()">查询</div>
	<a class="layui-btn" href="a">添加</a>
</div>
<table class="list">
		<tr class="list-head">
			<td>序号</td>
			<td>名称</td>
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
<al:navi url="/web/home/hr/dept/navi" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="0" jump="true" stat="true"></al:navi>
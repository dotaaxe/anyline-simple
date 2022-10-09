<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<table class="list">
	<thead>
		<tr>
			<td>申请人</td>
			<td>申请时间</td>
			<td>审批状态</td>
		</tr>
	</thead>
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
<al:navi url="/web/home/hr/duty/lve/apr/navid" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="1" auto="true"></al:navi>
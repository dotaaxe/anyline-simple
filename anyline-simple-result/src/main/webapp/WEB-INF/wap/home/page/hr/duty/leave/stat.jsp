<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<div class="info">
	<div class="item">
		<div class="label">请假日期(起)</div>
		<div class="data">
			<input type="date" id="txtYmdFr" name="fr" value="${fr}" placeholder="起">
		</div>
	</div>
	<div class="item">
		<div class="label">请假日期(止)</div>
		<div class="data">
			<input type="date" id="txtYmdTo" name="to" value="${to}" placeholder="止">
		</div>
	</div>
</div>
<div class="btn" onclick="fnQuery()">查询</div>
<script>
	function fnQuery(){
		location.href = '/web/home/hr/duty/lve/stat?fr='+ $('#txtYmdFr').val()+'&to='+$('#txtYmdTo').val();
	}
</script>
<table class="list">
		<tr class="list-head">
			<td>申请人</td>
			<td>请假时间</td>
		</tr>
	<tbody id="listBody">
		<c:forEach var="item" items="${set}">
			<tr>
				<td style="text-align: left;">${item.REQUEST_USER_NM}</td>
				<td>
					<al:date format="MM-dd HH:mm">${item.TIME_FR}</al:date>
							至
					<al:date format="MM-dd HH:mm">${item.TIME_TO}</al:date>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
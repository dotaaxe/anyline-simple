<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>

<div>原数据(共${maps_size}行)(查询耗时:${query_time} ms):</div>
<table class="list">
	<tr class="list-head">
		<td>序号</td>
		<td>工号</td>
		<td>姓名</td>
		<td>月份</td>
		<td>类别</td>
		<td>金额</td>
	</tr>
	<c:forEach var="item" items="${maps}" end="9" varStatus="status">
		<tr>
			<td>
					${status.count}
			</td>
			<td>
					${item.EMPLOYEE_CODE}
			</td>
			<td>
					${item.EMPLOYEE_NM}
			</td>
			<td>
					${item.YM}
			</td>
			<td>
					${item.TYPE_NM}
			</td>
			<td>
					${item.PRICE}
			</td>
		</tr>
	</c:forEach>
</table>
<div>转换后结果(共${groups_size}行)(转换耗时:${pivot_time} ms):</div>
<%
	long fr = System.currentTimeMillis();
%>
<table class="list">
	<tr class="list-head">
		<td rowspan="2">工号</td>
		<td rowspan="2">姓名</td>
		<c:forEach var="ym" items="${yms}">
			<td colspan="${types.size}" style="text-align: center;">
				${ym}
			</td>
		</c:forEach>
	</tr>
	<tr class="list-head">
		<c:forEach var="ym" items="${yms}">
			<c:forEach var="type" items="${types}"><td>${type.NM}</td></c:forEach>
		</c:forEach>
	</tr>
	<c:forEach items="${groups}" var="group">
			<tr>
				<td>${group.EMPLOYEE_CODE}</td>
				<td>${group.EMPLOYEE_NM}</td>
				<c:forEach var="ym" items="${yms}">
					<c:forEach var="type" items="${types}">
						<td>
							<al:text data="${group}" property="${ym}-${type.CODE}"></al:text>
						</td>
					</c:forEach>
				</c:forEach>
			</tr>
	</c:forEach>
</table>
<div>显示时间:<%=System.currentTimeMillis()-fr%>ms</div>
<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<table class="list">
	<tr class="list-head">
		<td>部门</td>
		<td>姓名</td>
		<td>入职日期</td>
	</tr>
	<c:forEach items="${groups}" var="group">
		<c:forEach var="item" items="${group.ITEMS}" varStatus="status">
			<tr>
				<c:if test="${status.index eq 0}">
					<td rowspan="${group.ITEMS.size}">${group.DEPARTMENT_NM}</td>
				</c:if>
				<td>${item.NM}</td>
				<td>${item.JOIN_YMD}</td>
			</tr>
		</c:forEach>
	</c:forEach>
</table>
<textarea>${groups.json}</textarea>
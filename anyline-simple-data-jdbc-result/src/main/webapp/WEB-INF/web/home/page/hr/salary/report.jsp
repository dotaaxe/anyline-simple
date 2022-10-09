<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<div>
	<span>单月最高:</span>
	<span>
		${max.EMPLOYEE_NM}(${max.DEPARTMENT_NM})/${max.YM}/${max.TOTAL_PRICE}(${max.BASE_PRICE}+${max.REWARD_PRICE})
	</span>
</div>
<div>
	<span>月平均:</span>
	<span>
		合计:${avg.TOTAL_PRICE}
		基本工资:${avg.BASE_PRICE}
		奖金:${avg.REWARD_PRICE}
	</span>
</div>
<table class="list">
	<tr class="list-head">
		<td>姓名</td>
		<c:forEach var="ym" items="${yms}">
			<td>
				${ym}
			</td>
		</c:forEach>
		<td>小计(人)</td>
	</tr>
	<c:forEach items="${groups}" var="group">
			<tr>
				<td>${group.EMPLOYEE_NM}</td>
				<c:set var="subTotal" value="0"></c:set>
				<c:forEach var="ym" items="${yms}">
					<td>
						<al:text var="price" data="${group}" property="${ym}"></al:text>
						<al:number format="###,##0.00" value="${price}"></al:number>
						<c:set var="subTotal" value="${subTotal+price}"></c:set>
					</td>
				</c:forEach>
				<td>
					<al:number format="###,##0.00" value="${subTotal}"></al:number>
				</td>
			</tr>
	</c:forEach>
	<tr>
		<td>小计(月)</td>

		<c:forEach var="ym" items="${yms}">
			<td>
				<al:sum property="TOTAL_PRICE" data="${set}" selector="YM:${ym}" format="###,##0.00"></al:sum>
			</td>
		</c:forEach>
		<td>
			<al:sum property="TOTAL_PRICE" data="${set}" format="###,##0.00"></al:sum>
		</td>
	</tr>
</table>
<textarea>${groups.json}</textarea>
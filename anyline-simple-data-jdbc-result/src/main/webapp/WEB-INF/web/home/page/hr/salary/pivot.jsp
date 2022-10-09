<%@ taglib prefix="al" uri="http://www.anyline.org/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<div>原数据(共${set.size}行)(查询耗时:${query_time} ms):</div>
<table class="list">
	<tr class="list-head">
		<td>序号</td>
		<td>工号</td>
		<td>姓名</td>
		<td>月份</td>
		<td>底薪</td>
		<td>奖金</td>
		<td>合计</td>
	</tr>
	<c:forEach var="item" items="${set}" end="9" varStatus="status">
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
					${item.BASE_PRICE}
			</td>
			<td>
					${item.REWARD_PRICE}
			</td>
			<td>
					${item.TOTAL_PRICE}
			</td>
		</tr>
	</c:forEach>
</table>
<div>转换后结果(共${groups.size}行)(转换耗时:${pivot_time} ms):</div>
<%
long fr = System.currentTimeMillis();
%>
<div>
	<span>单月最高:</span>
	<span>
		${max.EMPLOYEE_NM}(${max.DEPARTMENT_NM})/${max.YM}/${max.TOTAL_PRICE}(${max.BASE_PRICE}+${max.REWARD_PRICE}-${max.DEDUCT_PRICE})
	</span>
</div>
<div>
	<span>月平均:</span>
	<span>
		合计:${avg.TOTAL_PRICE}
		基本工资:${avg.BASE_PRICE}
		奖金:${avg.REWARD_PRICE}
		扣除:${avg.DEDUCT_PRICE}
	</span>
</div>
<table class="list">
	<tr class="list-head">
		<td>工号</td>
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
				<td>${group.EMPLOYEE_CODE}</td>
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
		<td colspan="2">小计(月)</td>

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
<div>显示时间:<%=System.currentTimeMillis()-fr%>ms</div>
<textarea>${groups.json}</textarea>
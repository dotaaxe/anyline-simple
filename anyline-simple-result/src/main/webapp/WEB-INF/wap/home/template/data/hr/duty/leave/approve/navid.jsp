<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<c:forEach var="item" items="${set }" varStatus="status">
	<tr class="row-${item.ID }">
		<td>${item.REQUEST_USER_NM}</td>
		<td>
			<al:date format="MM-dd HH:mm">${item.TIME_FR}</al:date>
			至
			<al:date format="MM-dd HH:mm">${item.TIME_TO}</al:date>
		</td>
		<td>
			<img id="imgAprStatus_${item.ID}" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096${item.APPROVE_STATUS}.png" style="width: 20px;height: 20px;">
		</td>
	</tr>
	<tr class="row-${item.ID }">
		<td colspan="3" style="text-align: left;line-height: 25px;">
			<div>
				<al:date format="MM-dd HH:mm">${item.REQUEST_TIME}</al:date>
				提前
				<span class="alert${item.BEFORE_HOUR<0}">${item.BEFORE_HOUR}</span>
				小时申请
			</div>
			<div>请假${item.DAY_QTY}天,合计${item.HOUR_QTY}小时</div>
			<div>[${item.SORT_NM}]${item.REMARK}</div>
		</td>
	</tr>
</c:forEach>
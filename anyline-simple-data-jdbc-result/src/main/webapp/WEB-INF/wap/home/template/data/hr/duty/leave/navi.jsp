<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<c:forEach var="item" items="${set }" varStatus="status">
	<tr id="row_${item.ID }">
		<td>${item.REQUEST_USER_NM}</td>
		<td>
			<a href="/web/home/hr/duty/lve/v?id=${item.ID}">
			<al:date format="MM-dd HH:mm">${item.TIME_FR}</al:date>
			至
			<al:date format="MM-dd HH:mm">${item.TIME_TO}</al:date>
		</a>
		</td>
		<td>
			<img id="imgAprStatus_${item.ID}"
				 src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096${item.FINAL_APPROVE_STATUS}.png" style="width: 20px;height: 20px;" >

			<a data-power='' href="javascript:al.delete('${item.ID}','/js/home/hr/duty/lve/d');" onclick="">撤销</a>
		</td>
	</tr>
</c:forEach>
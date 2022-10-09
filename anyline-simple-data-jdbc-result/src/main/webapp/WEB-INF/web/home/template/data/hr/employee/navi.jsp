<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<c:forEach var="item" items="${set }" varStatus="status">
	<tr id="row_${item.ID }">
		<td>
			<al:serial index="${status.index }" data="${set }"/>
		</td>
		<td>${item.DEPARTMENT_NM}</td>
		<td>${item.NM}</td>
		<td>${item.JOIN_YMD}</td>
		<td>

			<a  href="/web/home/hr/emp/u?id=${item.ID }">
			<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/update.png" title="修改" style="width:20px;">
			</a>
			<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/del.png" title="删除" style="width:20px;"
				 onclick="al.delete('${item.ID}','/js/home/hr/emp/d')">
		</td>
		</td>
	</tr>
</c:forEach>
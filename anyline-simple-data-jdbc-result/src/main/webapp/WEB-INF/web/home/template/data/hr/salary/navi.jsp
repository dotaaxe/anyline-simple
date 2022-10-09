<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<c:forEach var="item" items="${set }" varStatus="status">
    <tr id="row_${item.ID }">
        <td>
            <al:serial index="${status.index }" data="${set }"/>
        </td>
        <td>${item.DEPARTMENT_NM}</td>
        <td>${item.EMPLOYEE_NM}</td>
        <td>${item.YM}</td>
        <td>${item.BASE_PRICE}</td>
        <td>${item.REWARD_PRICE}</td>
        <td>${item.TOTAL_PRICE}</td>
        <td>${item.TOTAL_PRICE_K}</td>
        <td></td>
    </tr>
</c:forEach>
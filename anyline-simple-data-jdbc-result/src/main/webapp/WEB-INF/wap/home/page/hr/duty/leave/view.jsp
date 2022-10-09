<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<div class="info" id="divInfo">

	<div class='item'>
		<div class="label">申请人</div>
		<div class="data">
			<input type="text" name="remark" value="${row.REQUEST_USER_NM}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">申请时间</div>
		<div class="data">
			${row.REQUEST_TIME}"
			提前
			<span class="alert${row.BEFORE_HOUR<0}">${row.BEFORE_HOUR}</span>
			小时
		</div>
	</div>
	<div class='item'>
		<div class="label" style="width:200px;">请假时间</div>
		<div class="data">
			${row.TIME_FR}至${row.TIME_TO}
		</div>
	</div>
	<div class='item'>
		<div class="label">天数</div>
		<div class="data">
			<input type="text"  value="${row.DAY_QTY}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">小时</div>
		<div class="data">
			<input type="text"  value="${row.HOUR_QTY}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">类型</div>
		<div class="data">
			<input type="text"  value="${row.SORT_NM}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">说明</div>
		<div class="data">${row.REMARK}
		</div>
	</div>
	<div class='item'>
		<div class="label">当前审批人</div>
		<div class="data">
			<input type="text"  value="${row.LAST_APPROVE_USER_NM}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">当前审批时间</div>
		<div class="data">
			<input type="text"  value="${row.LAST_APPROVE_TIME}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">当前审批结果</div>
		<div class="data">

			<img id="imgFinalAprStatus_${row.ID}" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096${row.LAST_APPROVE_STATUS}.png" style="width: 20px;height: 20px;">
		</div>
	</div>
	<div class='item'>
		<div class="label">当前审批说明</div>
		<div class="data">${row.LAST_APPROVE_REASON}
		</div>
	</div>
	<div class='item'>
		<div class="label">最终审批人</div>
		<div class="data">
			<input type="text"  value="${row.FINAL_APPROVE_USER_NM}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">最终审批时间</div>
		<div class="data">
			<input type="text"  value="${row.FINAL_APPROVE_TIME}" readonly/>
		</div>
	</div>
	<div class='item'>
		<div class="label">最终审批结果</div>
		<div class="data">

			<img id="imgFinalAprStatus_${row.ID}" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096${row.FINAL_APPROVE_STATUS}.png" style="width: 20px;height: 20px;">
		</div>
	</div>
	<div class='item'>
		<div class="label">最终审批说明</div>
		<div class="data">${row.FINAL_APPROVE_REASON}
		</div>
	</div>
</div>
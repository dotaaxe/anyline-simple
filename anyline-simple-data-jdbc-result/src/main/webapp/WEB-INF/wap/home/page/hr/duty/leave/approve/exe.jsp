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
			<al:date format="MM-dd HH:mm">${row.REQUEST_TIME}</al:date>
			提前
			<span class="alert${row.BEFORE_HOUR<0}">${row.BEFORE_HOUR}</span>
			小时
		</div>
	</div>
	<div class='item'>
		<div class="label" style="width:200px;">请假时间</div>
		<div class="data">
			<al:date format="MM-dd HH:mm">${row.TIME_FR}</al:date>
			至
			<al:date format="MM-dd HH:mm">${row.TIME_TO}</al:date>
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
	<c:if test="${not empty row.FINAL_APPROVE_TIME}">
		<div class='item'>
			<div class="label">审批人</div>
			<div class="data">
				<input type="text"  value="${row.APPROVE_USER_NM}" readonly/>
			</div>
		</div>
		<div class='item'>
			<div class="label">审批时间</div>
			<div class="data">
				<input type="text"  value="${row.APPROVE_TIME}" readonly/>
			</div>
		</div>
		<div class='item'>
			<div class="label">审批结果</div>
			<div class="data">
				<img  src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096${row.APPROVE_STATUS}.png" style="width: 20px;height: 20px;">
			</div>
		</div>
		<div class='item'>
			<div class="label">审批说明</div>
			<div class="data">${row.APPROVE_REASON}
			</div>
		</div>
	</c:if>
	<c:if test="${empty row.FINAL_APPROVE_TIME}">

		<div class='item'>
			<div class="label">审批说明</div>
			<div class="data">
				<input type="text" name="reason" class="reason" placeholder="不通过的请输入审批说明"/>

			</div>
		</div>
		<div class="btn-group" style="width:100%;">
			<div class="btn btn-approve" onclick="fnApprove('${row.ID}',-1)" style="width:120px;">
				不通过
			</div>
			<div class="btn btn-approve" onclick="fnApprove('${row.ID}',1)" style="width:120px;"> 通过
			</div>
		</div>
	</c:if>
</div>
<script>

	function fnApprove(id,val){
		var remark = $('.reason').val();
		if(val == -1){
			if(!remark){
				al.tips('请输入审批说明');
				return;
			}
		}
		$('.btn-approve').hide();
		al.ajax({
			url:'/js/home/hr/duty/lve/apr/exe',
			data:{status:val,reason:remark,id:id},
			callback:function(result,data,msg){
				if(!result){
					al.alert(msg);
					$('.btn-approve').show();
				}else{
					al.tips('审核完成');
					location.href = '/web/home/hr/duty/lve/apr/l?status=0';
				}
			}
		});
	}
</script>
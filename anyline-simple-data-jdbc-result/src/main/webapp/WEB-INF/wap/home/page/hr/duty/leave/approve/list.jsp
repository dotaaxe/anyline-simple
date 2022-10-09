<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<table class="list">
	<thead>
		<tr>
			<td>申请人</td>
			<td>申请时间</td>
			<td>审批状态</td>
		</tr>
	</thead>
	<tbody id="listBody">

	</tbody>
</table>
<div id="listPage"></div>

<script type="text/javascript">
	function fnGetParam() {
		var params = al.pack.param('.query');
		params['status'] = '${param.status}';
		return params;
	}

	function fnApprove(id){
		var html = "<input type='text' style='width:100%;' placeholder='审批说明' id='txtReason'/>";
		console.log(id);
		al.confirm({
			title:'请假审批',
			content:html,
			btn: ['通过', '不通过'],
			yes : function(idx) {
				return fnSubmitApprove(id, 1);
			},no:function (){
				return fnSubmitApprove(id, -1);
			}
		});
	}
	function fnSubmitApprove(id, val){
		var reason = $('#txtReason').val();
		console.log(reason);
		if(val == -1 && !reason){
			al.tips('不通过，请输入说明');
			return false;
		}
		al.ajax({
			url:'/js/home/hr/duty/lve/apr/exe',
			data:{status:val,reason:reason,id:id},
			callback:function(result,data,msg){
				if(!result){
					al.alert(msg);
				}else{
					layer.closeAll();
					$('.row-'+id).remove();
					$('#imgAprStatus_'+id).prop('src','//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096'+val+'.png');
				}
			}
		});
		return true;
	}
</script>
<al:navi url="/web/home/hr/duty/lve/apr/navi" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="1" auto="true"></al:navi>
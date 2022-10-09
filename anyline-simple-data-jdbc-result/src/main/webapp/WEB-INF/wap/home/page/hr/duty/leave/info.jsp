<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<div class="info" id="divInfo">
	<div class="item">
		<div class="label" style="width:200px;">开始时间</div>
		<div class="data">
			<input type="date" name="frd"  value="${row.YMD_FR}" placeholder="开始时间"/>
			<al:select name="frh" value="${row.HOUR_FR}" data="${WORK_TIME}" head="时间" clazz="required"></al:select>
		</div>
	</div>
	<div class="item">
		<div class="label" style="width:200px;">结束时间</div>
		<div class="data">
			<input type="date" name="tod" value="${row.YMD_TO}" placeholder="结束时间"/>
			<al:select name="toh" value="${row.HOUR_TO}" data="${WORK_TIME}" head="时间" clazz="required"></al:select>
		</div>
	</div>
	<div class="item">
		<div class="label" style="width:200px;">请假类型</div>
		<div class="data">
			<al:select data="${DUTY_LEAVE_SORT}" value="${row.SORT_CODE}" name="sort" head="选择类型" clazz="required"></al:select>
		</div>
	</div>
	<div class="item">
		<div class="label">说明</div>
		<div class="data">
			<textarea name="remark" placeholder="请假说明" style="padding: 5px;">${row.REMARK}</textarea>
		</div>
	</div>
</div>
<div class="btn" onclick="fnGloableSave()">提交</div>
<script>
	function fnGloableSave() {
		if (!al.validate('#divInfo')) {
			return;
		}

		var params = al.packParam('#divInfo');

		if(!params['frd'] || !params['frh']){
			al.alert('请输入开始时间');
			return;
		}
		if(!params['tod'] || !params['toh']){
			al.alert('请输入结束时间');
			return;
		}
		if(!params['sort']){
			al.alert('请选择类型');
			return;
		}
		al.ajax({
			url : '/js/home/hr/duty/lve/s',
			data : $.param(params, true),
			callback : function(result, data, msg) {
				if (result) {
					al.tips('保存完成');
					location.href='/web/home/hr/duty/lve/v?id=' + data;
				} else {
					al.alert(msg);
				}
			}
		});
	}
</script>

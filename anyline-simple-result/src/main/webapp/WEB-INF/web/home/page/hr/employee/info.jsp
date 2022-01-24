<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<table class="info" id="divInfo">
	<tr>
		<td class="label" style="width:200px;">所属部门</td>
		<td class="data">
			<input type="hidden"  name="id" value="${row.ID}" style="width:200px;" />
			<input type="text" class="required" placeholder="名称" name="nm" value="${row.NM}" />
		</td>
	</tr>
	<tr>
		<td></td>
		<td>
			<div class="layui-btn" onclick="fnGloableSave()">保存</div>
		</td>
	</tr>
</table>

<script>
	function fnGloableSave() {
		if (!al.validate('#divInfo')) {
			return;
		}
		var params = al.packParam('#divInfo');

		al.ajax({
			url : '/js/home/hr/emp/s',
			data : $.param(params, true),
			callback : function(result, data, msg) {
				if (result) {
					al.tips('保存完成');
					location.href = '/web/home/hr/emp/l';
				} else {
					al.alert(msg);
				}
			}
		});
	}
</script>

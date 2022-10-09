<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<table class="info" id="divInfo">
	<tr>
		<td class="label" style="width:200px;">名称</td>
		<td class="data">
			<input type="text" class="required" placeholder="名称" name="nm" value="${row.NM}" style="width:200px;" />
		</td>
	</tr>
	<tr>
		<td class="label">联系人</td>
		<td class="data">
			<input type="text" name="ctnm" value="${row.CONTACT_NM}" style="width:200px;" />
		</td>
	</tr>
	<tr>
		<td class="label">联系电话</td>
		<td class="data">
			<input type="text" name="ctml" value="${row.CONTACT_MOBILE}" style="width:200px;" />
		</td>
	</tr>
	<tr>
		<td class="label">简称</td>
		<td class="data">
			<input type="text" name="snm" value="${row.SHORT_NM}" style="width:200px;" />
		</td>
	</tr>
	<tr>
		<td class="label">分类</td>
		<td class="data">

		<input type="checkbox" value="1" name="is_customer" id="chkCust"/><label for="chkCust">客户</label>
		<input type="checkbox" value="1" name="is_supplier" id="chkSpl"/><label for="chkSpl">供应商</label>
		</td>
	</tr>
	<tr>
		<td class="label">排序</td>
		<td class="data">
			<input type="text" name="idx" style="width:100px;" value="<al:number format="0">${row.IDX}</al:number>" />
			<c:if test="${not empty prev }">
			<a data-power='' href="/web/usr/crm/cust/u?id=${prev.ID }">上一条</a>
			</c:if>
			<c:if test="${not empty next }">
			<a data-power='' href="/web/usr/crm/cust/u?id=${next.ID }">下一条</a>
			</c:if>
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
			url : '/js/home/hr/dept/s',
			data : $.param(params, true),
			callback : function(result, data, msg) {
				if (result) {
					al.tips('保存完成');
					fnSetHash('/web/home/hr/dept/v?id=' + data);
				} else {
					al.alert(msg);
				}
			}
		});
	}
</script>

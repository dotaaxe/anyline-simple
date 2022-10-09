<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<script>
	is_show_btn_add = -1;
</script>
<div class="query">
	<div class="label">名称</div>
	<div class="data">
		<input type="text" name="title" placeholder="请输入名称"  class="layui-input">
	</div>
</div>
<table class="layui-table">
	<thead>
		<tr>
			<td>序号</td>
			<td>名称</td>
			<td>负责人</td>
			<td>操作</td>
		</tr>
	</thead>
	<tbody id="listBody">

	</tbody>
</table>
<div id="listPage"></div>

<script type="text/javascript">
	function fnGetParam() {
		var params = al.pack.param('.query');
		return params;
	}
	function fnDel(id) {
		layer.open({
			type : 1,
			title : '删除应用',
			content : "<div style='margin:10px;'>确实要删除当前内容吗</div>",
			btn : [ '确定', '取消' ],
			shadeClose : false,
			yes : function(index) {
				al.ajax({
					url : '/js/usr/art/d',
					data : {
						id : id
					},
					callback : function(result, data, msg) {
						if (result) {
							$('#row_' + id).remove();
						} else {
							al.alert(msg);
						}
						layer.closeAll();
					}
				});
			}
		});
	}
</script>
<al:navi url="/web/home/hr/dept/navi" param="fnGetParam" function="fnGloableQuery" body="listBody" page="listPage" intime="true" type="1" auto="true"></al:navi>
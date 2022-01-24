<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<script type="text/javascript" src="https://alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.des.js"></script>

<div class="layui-fluid">
	<div class="layui-card">
		<div class="layui-card-body" id="divInfo">
			<div class="layui-form-item">
				<label class="layui-form-label">应用</label>
				<div class="layui-input-block">
					<input type="hidden" name="id" value="${row.ID }" />
					<al:select data="${apps }" valueKey="APP_KEY" name="app" value="${row.APP_KEY }"></al:select>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-form-label">类别</div>
				<div class="layui-input-block">
					<al:select data="${sorts }" name="sort" value="${row.SORT_ID }"></al:select>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-form-label">图标</div>
				<div class="layui-input-block">
					<img style="width:42px;height:32px;" data-upload-field="img" data-upload-echo="this"
						src="<al:evl value="${row.IMG_FILE_URL },//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/empty_w.png"></al:evl>">
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-form-label">标题</div>
				<div class="layui-input-block">
					<input type="text" name="title" value="${row.TITLE}" style="width:600px;" disabled/>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-form-label">排序</div>
				<div class="layui-input-block">
					<input type="text" name="idx" style="width:100px;" value="<al:number format="0">${row.IDX}</al:number>"  disabled/>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-form-label">概要</div>
				<div class="layui-input-block">
					<textarea id="txtSummary" id="txtSummary" style="width:600px;height:80px;" name="summary" disabled>${row.SUMMARY }</textarea>
				</div>
			</div>
			<div class="layui-form-item layui-form-text">
				<div class="layui-form-label">内容</div>
				<div class="layui-input-block" id="tdDes">${row.CONTENT}</div>
			</div>
		</div>
	</div>
</div>


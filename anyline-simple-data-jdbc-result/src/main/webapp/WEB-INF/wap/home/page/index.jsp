<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/wap/common/inc/tag.jsp"%>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/jquery.qrcode.min.js"></script>
<div class="common-wrapper">
	<div class="head-img">
		<div class="my-id-pic" style="height:145px;background-color:#FF3957;"></div>
		<div class="my-info cf">
			<div class="my-img-wrap">
				<span class="my-img"> <input type="hidden" id="hidImg" value="${SESSION_CUR_USER.IMG_FILE_PATH }" /> <img
					src="<al:evl value="${SESSION_CUR_USER.IMG_FILE_URL},//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/user0.png"></al:evl>" id="imgTenantImg">
				</span>
			</div>
			<div class="my-person-info">
				<p class="my-bk-head-name">${SESSION_CUR_USER.NM }
					<c:if test="${not empty SESSION_CUR_USER.SIGN}"><br/><span style="font-size: 75%;">${SESSION_CUR_USER.SIGN}</span></c:if>
				</p>
				<div class="my-bk-head-user">
					<span class="my-bk-head-type">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/auth_f1.png" style="height:28px;display: none;"></span>
					<div class="my-bk-head-plus">
						用户ID:<span class="plus-icon-text">${SESSION_CUR_USER.CODE }</span>
					</div>
				</div>
			</div>
			<div class="head-meassage">
				<span class="head-meassage-name">编辑</span> <span class="info-hint"></span>
			</div>
		</div>

		<ul class="my-watch cf" style="padding-top:10px;">

			<li style="text-align:center;padding-top:18px;font-size:12px;color:#FFF;" onclick="">
				<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/purse000.png" style="width:20px;"> 我的待办
			</li>
			<li class="mar bdr-l bdr-r" style="text-align:center;padding-top:18px;font-size:12px;color:#FFF;" onclick="">
				<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/card000.png" style="width:20px;"> 我的消息
			</li>

			<li style="text-align:center;padding-top:18px;font-size:12px;color:#FFF;" onclick="">
				<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/code000.png" style="width:15px;"> 个人中心
			</li>
		</ul>
	</div>

		<jsp:include page="/WEB-INF/wap/home/inc/index_menu.jsp"></jsp:include>

	<sso:exit redirect="/" callback="fnUserExit">
		<div class="btn" style="width:100%;margin-top:10px;">安全退出</div>
	</sso:exit>

	<al:checkClient type="app">
		<div class="btn" style="width:90%;margin-top:10px;" onclick="app.exit();">退出应用</div>
	</al:checkClient>
	<script>

		function fnUserExit(result,data,msg){
			al.cookie.set('${_cookie_name}','',100000000);
			return true;
		}
	</script>
</div>
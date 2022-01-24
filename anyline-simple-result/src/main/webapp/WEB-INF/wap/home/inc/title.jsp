<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header id="ly_header">
	<div class="header">
		<div class="a-back">
			<a href="javascript:history.back();"> <span><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/bak9.png"></span></a>
		</div>
		<div class="header-title">
			${WEB_USER_CUR_PAGE_TITLE}
			<c:if test="${empty WEB_USER_CUR_PAGE_TITLE}">B.BLOSSOM Fashion</c:if>
			<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/home9.png" style="height:25px;float:right;margin: 8px 10px 0px 0px;" onclick="window.location.href='/index'">
		</div>
	</div>
</header>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!doctype html>
<html>
<head>
<jsp:include page="/WEB-INF/wap/common/inc/head.jsp"></jsp:include>
<title>${APP_NM }</title>
</head>
<body>
	<div class="mobile">
		<jsp:include page="/WEB-INF/wap/home/inc/title.jsp"></jsp:include>
		<div class="w" style="min-height:700px;">
		<jsp:include page="${anyline_template_content_path }"></jsp:include>
		</div>
		<jsp:include page="/WEB-INF/wap/common/inc/bottom.jsp"></jsp:include>

	</div>
</body>
</html>

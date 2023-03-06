<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
    <jsp:include page="/WEB-INF/web/common/inc/head.jsp"></jsp:include>
</head>
<body>
<div>
    <jsp:include page="/WEB-INF/web/home/inc/menu.jsp"></jsp:include>
</div>
<div>
<jsp:include page="${anyline_template_content_path }"></jsp:include>
</div>
</body>
</html>
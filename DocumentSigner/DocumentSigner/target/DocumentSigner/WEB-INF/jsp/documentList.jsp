<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document List</title>
</head>
<body>
	<p style="font-size: 20px; font-weight: bolder">Document List</p>
	<c:forEach var="document" items="${documentInfo}">
		<a href="<c:url value="documentDetails"/>?documentName=${document.userId}"></a>
	</c:forEach>
</body>
</html>
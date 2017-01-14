<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Download</title>
</head>
<body background="${pageContext.request.contextPath}/images/green.jpg"
	style="text-align: center; background-size: cover">
	<h2>Document Signer</h2>
	<h3>Download</h3>
	<form:form action="downloadDocument" method="get" style="margin-left:35%">
		<table>
			<tr>
				<td><label>Enter Document ID:</label></td>
				<td><form:input type="Number" path="id" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Download" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>
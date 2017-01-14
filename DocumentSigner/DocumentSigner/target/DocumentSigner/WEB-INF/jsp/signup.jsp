<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Signup Page</title>
</head>
<body background="${pageContext.request.contextPath}/images/green.jpg"
	style="text-align: center; background-size: cover">
	<h1>Signup Page</h1>
	<center>
		<form:form action="signupPage" commandName="user" method="post">
			<table>
				<tr>
					<td><label>Enter Name:</label></td>
					<td><form:input type="text" path="name" /></td>
				</tr>
				<tr>
					<td><label>Enter Email:</label></td>
					<td><form:input type="email" path="email" /></td>
				</tr>
				<tr>
					<td><label>Enter Password:</label></td>
					<td><form:input type="password" path="password" /></td>
				</tr>
				<tr>
					<td><label>Enter City:</label></td>
					<td><form:input type="text" path="city" /></td>
				</tr>
				<tr>
					<td><label>Enter Country:</label></td>
					<td><form:input type="text" path="country" /></td>
				</tr>
				<tr>
					<td><label>Enter Mobile No:</label></td>
					<td><form:input type="text" path="mobile" /></td>
				</tr>
				<tr>
					<td><form:input type="submit" path="" value="Submit" /></td>
				</tr>
			</table>
		</form:form>
	</center>
</body>
</html>
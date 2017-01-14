<%@page import="org.bridgelabz.documentsigner.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
<script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>
</head>
<body background="${pageContext.request.contextPath}/images/green.jpg"
	style="text-align: center; background-size: cover">
	<h1>Login Page</h1>
	<center>
		<p id="message"></p>
		<form action="login" method="post" onsubmit="return loginFunction();">
			<table>
				<tr>
					<td><label>Enter Email:</label></td>
					<td><input type="email" name="email" id="email"></td>
				</tr>
				<tr>
					<td><label>Enter Password:</label></td>
					<td><input type="password" name="password" id="password"></td>
				</tr>
				<tr>
					<td><input type="submit" value="Submit" /></td>
				</tr>
			</table>
		</form>
	</center>

	<script type="text/javascript">
	 	function loginFunction() {
			var emailV = $("#email").val();
			var pass = $("#password").val();
			
			$.ajax({
				url : "login",
				method : "POST",
				data : JSON.stringify({email : emailV, password : pass}),
				contentType: 'application/json; charset=UTF-8',
				dataType : "json"
			})
			.done(function(data) {
				if (data.status == 1) {
					window.location.href = 'document';
					window.location = 'document';
				} else {
					$("#message").text(data.displayMessage);
				}
			}).fail(function() {

			}).always(function() {

			});

			//document
			return false;
		}
	</script>
</body>
</html>
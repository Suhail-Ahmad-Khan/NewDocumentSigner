<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Document Signer</title>
</head>
<body background="${pageContext.request.contextPath}/images/green.jpg"
	style="text-align: center; background-size: cover">
	<h2>Document Signer</h2>
	<h3>Add new Signatures</h3>
	<form:form action="addSignatures" commandName="signature" method="post"
		style="margin-left:35%" enctype="multipart/form-data">
		<table>
			<tr>
				<td><label>Enter Description:</label></td>
				<td><form:input type="textarea" path="description" /></td>
			</tr>
			<tr>
				<td><label> Enter Document</label></td>
				<td><input type="file" name="file" id="file"></input></td>
			</tr>
			<tr>
				<td><input type="submit" value="Add Signatures" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Successful Login Page</title>
</head>
<body background="${pageContext.request.contextPath}/images/green.jpg"
	style="text-align: center; background-size: cover">

	<center>

		<h1>Successful login</h1>
		<div>
		<a href="addDocuments">Add Documents</a> 
		<a href="addSignatures">Add Signature</a> 
		<a href="documentList">List My Documents</a>
		<a href="signatureList">List My Signatures</a> 
		<a href="signout">Signout</a>
		</div>
		<a href="generateTokens">Generate New Tokens</a>
		<!-- <a href="downloadDocument">Download Document</a>
		<a href="downloadSignature">Download Signature</a> -->
		
	</center>

</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
</head>
<body>
	<h1> Home </h1>
	<!-- login form -->
	<form action="controller">
		Username: <input type="text" name="user">
		Password: <input type="password" name="pass">
		<input type="submit" name="action" value="login"> 
	</form>
	<br>
	
	<!-- search bar -->
	<form action="controller"> 
		<input type="text" name="search">
		<input type="submit" name="action" value="search">
	</form>
	
	<!-- now showing and coming soon tables -->
	<table>
		<tr><th>Now Showing</th><th>Coming Soon</th></tr>
		<c:forEach var="i" begin="1" end="3">
			<tr>
				<td> <a href=controller?action=details&title=movie${i}>Movie <c:out value="${i}"/></a></td>
				<td> <a href=controller?action=details&title=movie${i}>Movie <c:out value="${i}"/></a></td>
			</tr>
			<tr>
				<td> poster </td>
				<td> poster </td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
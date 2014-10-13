<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home Page</title>
</head>
<body>

<h1>Home page</h1>

	<c:if test="${! empty login}" var="loggedin">
	   Welcome ${login} [<a href="profile">Profile page</a>]
	</c:if>
	<c:if test="${loggedOut}">
	   You have successfully logged out.
	</c:if>

    
    <c:if test="${! loggedin}">
        <a href="register">Register</a>
        <a href="login">Login</a>
    </c:if>
    <c:if test="${loggedin }">
        <a href="logout">Logout</a>
    </c:if>

</body>
</html>
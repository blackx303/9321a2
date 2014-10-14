<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
</head>
<body>
<h1>Login Page</h1>

    <c:if test="${! empty invalid }">
        Sorry, invalid username (&quot;${invalid}&quot;) or password.
    </c:if>
    <c:if test="${pending}">
        Please confirm your account before logging in.
    </c:if>
	<form action="login" method="post">
	   <label for="username">Username: </label><input type="text" name="username" /><br />
	   <label for="password">Password: </label><input type="password" name="password" />
	   <input type="submit" value="Login" />
	</form>
	
	<a href="home">Home</a>

</body>
</html>
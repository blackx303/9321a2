<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Page</title>
</head>
<body>

<h1>Registration Page</h1>

    <c:if test="${! empty usertaken}">
        <p>Sorry, the username &quot;${usertaken}&quot; is already taken.</p>
    </c:if>
    <c:if test="${! empty bademail }">
        <p>Please enter a valid email address (you entered &quot;${bademail}&quot;).</p>
    </c:if>
    <c:if test="${! empty badusername }">
        <p>Usernames must be between 5 and 16 characters, start with a letter, and contain only letters, digits and underscores. Usernames are case insensitive. (You provided &quot;${badusername}&quot;)</p>
    </c:if>
    <form action="register" method="post">
        <label for="username">Username: </label><input type="text" name="username" />
        <label for="password">Password: </label><input type="password" name="password" />
        <label for="email">Email: </label><input type="text" name="email" />
        <input type="submit" value="Register!" />
    </form>



</body>
</html>
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
    <!-- search bar -->
    <form action="controller"> 
        <input type="text" name="search">
        <input type="submit" name="action" value="search">
    </form>
    
    <!-- Login/out etc -->
    <c:if test="${! empty login}" var="loggedin">
       Welcome ${login} 
       <c:if test="${login eq 'admin'}" var="adminlogin">[<a href="manage">Manage Application</a>]</c:if>
       <c:if test="${! adminlogin}">[<a href="profile">Profile page</a>]</c:if>
    </c:if>
    <c:if test="${! loggedin}">
        <a href="register">Register</a>
        <a href="login">Login</a>
    </c:if>
    <c:if test="${loggedin }">
        <a href="logout">Logout</a>
    </c:if>
    <br />
    <!-- End login/out etc -->

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

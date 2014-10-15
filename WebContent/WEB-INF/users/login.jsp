<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<c:set var="title" scope="request">Login Page</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

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
	
<%@ include file="/WEB-INF/_footer.jsp" %>

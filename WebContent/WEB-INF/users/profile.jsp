<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">${login}'s Profile Page</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />
    
    <form action="profile" method="post">
        <label for="nickname">Nickname: </label><input type="text" name="nickname" value="${nickname}" /><br />
        <label for="firstname">First name: </label><input type="text" name="firstname" value="${firstname}" /><br />
        <label for="lastname">Last name: </label><input type="text" name="lastname" value="${lastname}" /><br />
        <label for="email">Email: </label><input type="text" name="email" value="${email}" /><br />
        <input type="submit" value="Save" />
    </form>

<%@ include file="/WEB-INF/_footer.jsp" %>
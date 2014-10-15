<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">${login}'s Profile Page</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />
    
    <c:if test="${param['saved'] != null}">
        Details saved successfully.
    </c:if>
    <form action="profile" method="post">
        <label for="nickname">Nickname: </label><input type="text" name="nickname" value="${nickname}" /><br />
        <label for="firstname">First name: </label><input type="text" name="firstname" value="${firstname}" /><br />
        <label for="lastname">Last name: </label><input type="text" name="lastname" value="${lastname}" /><br />
        <label for="email">Email: </label><input type="text" name="email" value="${email}" /><br />
        <input type="submit" value="Save" />
    </form>
    <c:if test="${param['passwordchanged'] != null}">
        Password changed.
    </c:if>
    <c:if test="${(! empty passwordsmatch) && ! passwordsmatch}">
        New password does not match confirm password.
    </c:if>
    <c:if test="${unauthorised}">
        Couldn't authenticate (incorrect current password).
    </c:if>
    <form action="profile" method="post">
	    <label>Current Password: <input type="password" name="oldpassword" /></label><br />
	    <label>New Password: <input type="password" name="newpassword" /></label><br />
	    <label>Confirm Password: <input type="password" name="confirmpassword" /></label><br />
	    <input type="submit" value="Change Password" />
    </form>

<%@ include file="/WEB-INF/_footer.jsp" %>
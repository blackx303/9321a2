<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">Bad Link</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

    Sorry, the link you followed has either been activated already, expired or is broken.
    <a href="/home">Home</a>

<%@ include file="/WEB-INF/_footer.jsp" %>

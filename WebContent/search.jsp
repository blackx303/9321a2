<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>Search Results</title>
</head>
<body>
	<h1> Search Results </h1>
	<!-- if nothing found show error -->
	
	
	<!-- Search results table -->
	Showing ${showing} search result(s) for "<c:out value="${search}"/>".
	<hr>
	
		<c:forEach items="${results}" var="i">
			
				<p><a href="details?action=details&title=${i.title}&releaseDate=${i.releaseDate}"><c:out value="${i.title}"/></a></p>
			
				
				<p><img src="poster?t=${i.title}&r=${i.releaseDate}" height="200" width="150"/></p>
				<p>Release Date: <c:out value="${i.releaseDate }"/></p>
			
			<c:if test="${not empty i.genres }">
				
					<p>Genre: <c:out value="${i.genres }"/></p>
				
			</c:if>
			<c:if test="${not empty i.actors }">
				
					<p>Actors: <c:out value="${i.actors }"/></p>
				
			</c:if>
			<c:if test="${not empty i.ageRating }">
				
					<p>Age Rating: <c:out value="${i.ageRating }"/></p>
				
			</c:if>
			<hr>
		</c:forEach>
	
<%@ include file="/WEB-INF/_footer.jsp" %>

</html>
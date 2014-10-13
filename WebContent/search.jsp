<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Results</title>
</head>
<body>
	<h1> Search Results </h1>
	<!-- if nothing found show error -->
	
	
	<!-- Search results table -->
	You searched for <c:out value="${search}"/>
	Results: <c:out value="${results}"/>
	<table>
		<c:forEach items="${results}" var="i">
			<tr>
				<td> <a href="controller?action=details&title=${i.title}"><c:out value="${i.title}"/></a></td>
			</tr>
			<tr>
				<td>Release Date: <c:out value="${i.releaseDate }"/></td>
			</tr>
			<c:if test="${not empty i.genre }">
				<tr>
					<td>Genre: <c:out value="${i.genre }"/></td>
				</tr>
			</c:if>
			<c:if test="${not empty i.ageRating }">
				<tr>
					<td>Age Rating: <c:out value="${i.ageRating }"/></td>
				</tr>
			</c:if>

		</c:forEach>
	</table>
	
</body>
</html>
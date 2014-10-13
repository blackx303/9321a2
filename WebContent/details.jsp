<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie Details</title>
</head>
<body>
	<h1> ${title} </h1>
	<!-- movie details -->
	<table>
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
		<c:if test="${not empty i.rating }">
			<tr>
				<td>Age Rating: <c:out value="${i.rating }"/></td>
			</tr>
		</c:if>
	</table>
	
	<!-- reviews of the movie, none if unreleased-->
	<h1> Reviews </h1>
	<table rules="groups">
		<c:forEach var="i" begin="1" end="3">
			<tr>
				<td> Reviewer </td>
			</tr>
			<tr>
				<td> Rating: ${i }</td>			
			</tr>
			<tr>
				<td> longgggggg asssssssss reviewwwwwwwwwww goes hereeeeeeeee</td>
			</tr>
			
		</c:forEach>
	</table>
	
	<!-- must be logged in to post a review and cannot review an unreleased movie-->
	<h1> Post a review </h1>
	<form action="controller">
		<textarea rows="10">
		</textarea>
		
	</form>
</body>
</html>
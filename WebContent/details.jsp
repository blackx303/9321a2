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
<script>
function validateForm() {
    var x = document.forms["review"]["reviewText"].value;
    if (x==null || x=="") {
        alert("Review is empty!");
        return false;
    }
}
</script>
<title>Movie Details</title>
</head>
<body>
	<h1> ${movie.title} </h1>
	<!-- movie details -->
	<p><img src="poster?t=${movie.title}&r=${movie.releaseDate}" height="400" width="300"/></p>
	<p>Release Date: <c:out value="${movie.releaseDate }"/></p>
		
		<c:if test="${not empty movie.genres }">
			
				<p>Genre: <c:out value="${movie.genres }"/></p>
			
		</c:if>
		<c:if test="${not empty movie.ageRating }">
			
				<p>Age Rating: <c:out value="${movie.ageRating }"/></p>
			
		</c:if>
		<c:if test="${not empty movie.actors }">
			
				<p>Actors: <c:out value="${movie.actors }"/></p>
			
		</c:if>
		<c:if test="${not empty movie.director }">
			
				<p>Director: <c:out value="${movie.director }"/></p>
			
		</c:if>
		<c:if test="${not empty movie.synopsis }">
			
				<p>Synopsis: <c:out value="${movie.synopsis }"/></p>
			
		</c:if>
	
	<!-- reviews of the movie, none if unreleased-->
	<hr>
	<c:choose>
		<c:when test="${movie.isReleased() }">
			<h1> Reviews </h1>
			<c:choose>
				<c:when test="${! empty reviews }">
					<c:forEach items="${reviews}" var="i" >
					
						<p>Username: <c:out value="${i.username }"/></p>
					
						<p>Rating: <c:out value="${i.rating }"/></p>
					
						<p>${i.review_text}</p>
						<hr>
					</c:forEach>
				</c:when>
				<c:otherwise>
				<p> No reviews </p>
				</c:otherwise>
			</c:choose>
	
			<!-- must be logged in to post a review and cannot review an unreleased movie-->
			<c:choose>
			<c:when test="${! empty login}">
				<hr>
				<h1> Post a review </h1>
				<form name="review" action="review" method="post" onsubmit="return validateForm()">
					Rating: 
					<select name="rating">
						<option value="1">1</option>
						<option value="2">2</option>
						<option value="3">3</option>
						<option value="4">4</option>
						<option value="5">5</option>
					</select><br>
					<textarea rows="10" name="reviewText"></textarea>
					<input type="hidden" name="title" value="${movie.title }">
					<input type="hidden" name="releaseDate" value="${movie.releaseDate }">
					<input type="submit" name="action" value="post" >
					
				</form>
			</c:when>
				<c:otherwise>
					Log in to post a review.
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<h1> Check back after release for reviews! </h1>
		</c:otherwise>
	</c:choose>
<%@ include file="/WEB-INF/_footer.jsp" %>
</html>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">Manage Movies</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

    <!-- Title, Poster (JPG/PNG), list of actors and actresses, Genre(s) that it belongs to, Director, Short Synopsis (100 words), Age rating. -->
    <form action="movie" method="post" enctype="multipart/form-data">
        <label>Title: <input type="text" autofocus required name="title" /></label>
        <label>Release Date: <input type="date" required name="releasedate" value="2014-01-01"/></label>
        <label>Poster (jpg/png): <input type="file" name="poster" /></label>
        <label>Actors/Actresses: <input type="text" name="actors" /></label>
        <fieldset>
            Genre(s): 
                <c:forEach items="${genres}" var="genre">
                    <label>${genre}<input type="checkbox" name="genre_${genre}"></label>
                </c:forEach> </fieldset>
        <label>Director: <input type="text" name="director" /></label>
        <label>Age Rating: <select required name="agerating">
                <c:forEach items="${ageratings}" var="agerating">
                    <option value="${agerating}">${agerating}</option>
                </c:forEach>
        </select></label>
        <label>Synopsis: <textarea name="synopsis" maxlength="1024"></textarea></label>
        <input type="submit" value="Add Movie" />
    </form>
    
    <!-- Movies list -->
    <table>
        <tr><th>Poster</th><th>Title</th><th>Release Date</th><th>Age Rating</th><th>Genre(s)</th>
                <th>Director</th><th>Actors/Actresses</th><th>Synopsis</th></tr>
        <c:forEach items="${movies}" var="movie">
            <tr><td><img url="poster?t=${movie.title}&r=${movie.releaseDate}" /></td><td>${movie.title}</td>
                    <td>${movie.releaseDate}</td><td>${movie.ageRating}</td><td><ul>
                <c:forEach items="${movie.genres}" var="genre">
                    <li>${genre}</li>
                </c:forEach>
                    </ul></td><td>${movie.director}</td><td>${movie.actors}</td><td>${movie.synopsis}</td></tr>
        </c:forEach>
    </table>
<%@ include file="/WEB-INF/_footer.jsp" %>

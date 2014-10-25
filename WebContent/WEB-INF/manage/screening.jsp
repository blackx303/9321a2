<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="title" scope="request">Screening Management</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

    <!-- For adding new Screenings -->
    <form action="screening" method="post">
        <label>Movie: <select name="movie">
            <c:forEach items="${movies}" var="movie">
                <option value="${movie.releaseDate};${movie.title}">${movie.title} (${movie.releaseDate})</option>
            </c:forEach></select></label>
        <label>Cinema: <select name="cinema">
            <c:forEach items="${cinemas}" var="cinema">
                <option value="${cinema.location}">${cinema.location}</option>
            </c:forEach></select></label>
        <label>Screening Date: <input type="date" value="2014-12-01" name="screeningdate" /></label>
        <label>Screening Time: <input type="time" value="20:00" name="screeningtime" /></label>
        <input type="submit" value="Add Screening" />
    </form>

    <!-- Table of screenings -->
    <table>
        <tr><th>Movie</th><th>Cinema</th><th>Screening Time</th></tr>
        <c:forEach items="${screenings}" var="screening">
            <tr><td>${screening.title} (released ${screening.releaseDate})</td>
                    <td>${screening.cinema}</td><td>${fn:substring(screening.screeningTime, 0, 16)}</td></tr>
        </c:forEach>
    </table>

<%@ include file="/WEB-INF/_footer.jsp" %>

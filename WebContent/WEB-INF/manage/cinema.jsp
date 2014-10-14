<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">Cinema Management</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

    <!-- For adding new Cinemas -->
    <form action="cinema" method="post">
        Add Cinema
        <label>Cinema Location: <input type="text" name="location" /></label>
        <label for="capacity">Capacity: <input type="number" min="1" value="50" name="capacity" /></label>
        <c:forEach items="${amenitytypes}" var="type">
            <label>${type}<input type="checkbox" name="amenity_${type}"></label>
        </c:forEach>
        <input type="submit" value="Add Cinema" />
    </form>
    
    <!-- Table of cinemas -->
    <table>
        <tr><th>Cinema Location</th><th>Seating Capacity</th><th>Amenities</th></tr>
        <c:forEach items="${cinemas}" var="cinema">
            <tr><td>${cinema.location}</td><td>${cinema.capacity}</td><td><ul>
            <c:forEach items="${cinema.amenities}" var="amenity">
                <li>${amenity}</li>
            </c:forEach>
            </ul></td></tr>
       </c:forEach>
    </table>

<%@ include file="/WEB-INF/_footer.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" scope="request">Home Page</c:set>
<jsp:include page="/WEB-INF/_header.jsp" />

    <!-- search bar -->
    <c:if test="${! empty login }">
	    <form action="search"> 
	        <input type="text" name="search">
		        <fieldset>
	            Genre(s): 
	                <c:forEach items="${genres}" var="genre">
	                    <label>${genre}<input type="checkbox" name="genre_${genre}"></label>
	                </c:forEach> </fieldset>
	        <input type="submit" name="action" value="search">
	    </form>
    </c:if>
    
    <!-- Login/out etc -->
    <c:if test="${! empty login}" var="loggedin">
       Welcome ${login} 
       <c:if test="${login eq 'admin'}" var="adminlogin">[<a href="manage">Manage Application</a>]</c:if>
       <c:if test="${! adminlogin}">[<a href="profile">Profile page</a>]</c:if>
    </c:if>
    <c:if test="${! loggedin}">
        <a href="register">Register</a>
        <a href="login">Login</a>
    </c:if>
    <c:if test="${loggedin }">
        <a href="logout">Logout</a>
    </c:if>
    <br />
    <!-- End login/out etc -->

    <!-- now showing and coming soon tables -->
    <h1> Now Showing </h1>
    <table>
    	<tr>
        <c:forEach items="${nowShowing }" var="m">
        	<td>
	            <p><a href="details?action=details&title=${m.title}&releaseDate=${m.releaseDate}">${m.title }</a></p>
	            <p> Age Rating: ${m.ageRating }</p>
	            <p><img src="poster?t=${m.title}&r=${m.releaseDate}" height="200" width="150"/></p>
            </td>
        </c:forEach>
        </tr>
    </table>
    <h1> Coming Soon </h1>
    <table>
    	<tr>
        <c:forEach items="${comingSoon }" var="m">
        	<td>
	            <p><a href="details?action=details&title=${m.title}&releaseDate=${m.releaseDate}">${m.title }</a></p>
	            <p> Age Rating: ${m.ageRating }</p>
	            <p> Release Date: ${m.releaseDate }</p>
	            <p><img src="poster?t=${m.title}&r=${m.releaseDate}" height="200" width="150"/></p>
            </td>
        </c:forEach>
        </tr>
    </table>

<%@ include file="/WEB-INF/_footer.jsp" %>

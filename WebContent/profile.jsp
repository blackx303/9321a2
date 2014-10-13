<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>${login} Profile Page</title>
</head>
<body>
    <h1>${login} Profile Page</h1>
    
    <form action="profile" method="post">
        <label for="nickname">Nickname: </label><input type="text" name="nickname" value="${nickname}" /><br />
        <label for="firstname">First name: </label><input type="text" name="firstname" value="${firstname}" /><br />
        <label for="lastname">Last name: </label><input type="text" name="lastname" value="${lastname}" /><br />
        <label for="email">Email: </label><input type="text" name="email" value="${email}" /><br />
        <input type="submit" value="Save" />
    </form>
</body>
</html>
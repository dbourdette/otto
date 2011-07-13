<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="fr">
<head>
	<meta charset="utf-8">
	<title>Otto event server</title>
</head>

<body>
	<form:form action="/events" commandName="form" method="post">
		<form:errors path="name" />
		<form:input path="name"/>
		<input type="submit" />
	</form:form>
	
	<ul>
	<c:forEach var="collection" items="${collections}">
		<li><a href="events/${collection}">${collection}</a></li>
	</c:forEach>
	</ul>
</body>
</html>
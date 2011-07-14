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
	<form:form action="/events/${name}" commandName="form" method="delete">
		<input type="submit" value="supprimer" />
	</form:form>

    <a href="/events/${name}">logs</a> - graph  <br/><br/>

    ${graph}

	<form action="/events/${name}" method="post">
		<input type="text" size="150" name="values" />
		<input type="submit" value="poster" />
	</form>
</body>
</html>
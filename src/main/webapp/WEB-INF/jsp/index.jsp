<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib tagdir="/WEB-INF/tags/widgets" prefix="widget" %>

<!DOCTYPE html>
<html lang="fr">

<widget:head />

<body>
	<widget:header />
	
	<article>
		<h2>Existing collections</h2>
	
		<ul>
			<c:forEach var="collection" items="${collections}">
				<li><a href="events/${collection}">${collection}</a></li>
			</c:forEach>
		</ul>
		
		<h2>Create a new collection</h2>
	
		<form:form action="/events" commandName="form" method="post">
			<form:errors path="name" />
			<form:input path="name"/>
			<input type="submit" value="Créer" />
		</form:form>
	</article>
	
	<widget:footer />
</body>
</html>
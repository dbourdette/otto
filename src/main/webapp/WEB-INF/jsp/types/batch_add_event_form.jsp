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
		<a href="/types/${name}">index</a> - <a href="/types/${name}/events">logs</a> - <a href="/types/${name}/graph">graph</a> - batch add events <br/><br/>
		
		<form:form action="/types/${name}/events/batch" commandName="form" method="post">
			<p>values</p>
			<form:errors path="values" />
			<form:textarea rows="5" cols="50" path="values" />
			<p>number of times</p>
			<form:errors path="count" />
			<form:input path="count" />
			<br/><br/>
			<input type="submit" value="poster" />
		</form:form>
	</article>
	
	<widget:footer />
</body>
</html>
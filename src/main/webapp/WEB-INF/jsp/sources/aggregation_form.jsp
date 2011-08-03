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
		<widget:nav />
	
		<h2>Source time frame</h2>
	
		<form:form action="/sources/${name}/aggregation" commandName="form" method="post">
			<p>
				time frame : <form:select path="timeFrame" items="${timeFrames}" />
				<form:errors path="timeFrame" />
			</p>
			<input type="submit" value="Save" />
		</form:form>
	</article>
	
	<widget:footer />
</body>
</html>
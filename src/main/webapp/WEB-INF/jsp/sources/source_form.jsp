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
	
		<h2>New event type</h2>
	
		<form:form action="/sources" commandName="form" method="post">
			<p>
				name : <form:input path="name"/>
				<form:errors path="name" />
			</p>
			<p>
				size : <form:input path="size"/> (ex : 100M)
				<form:errors path="size" />
			</p>
			<p>
				max events : <form:input path="maxEvents"/> events
				<form:errors path="maxEvents" />
			</p>
			<input type="submit" value="Create" />
		</form:form>
	</article>
	
	<widget:footer />
</body>
</html>
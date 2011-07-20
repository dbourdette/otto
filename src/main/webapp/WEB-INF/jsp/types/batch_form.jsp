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
		
		<form:form action="/types/${name}/events/batch" commandName="form" method="post">
			<p>
				<span>number of times</span>
				<form:errors path="count" />
				<form:input path="count" size="4" />
			</p>
			<p>
				<span>date</span>
				<form:radiobuttons path="dateType" />
			</p>
			<p>
				<span>values type</span>
				<form:radiobuttons path="valuesType" />
			</p>
			<p>
				<span>values</span><br/>
				<form:errors path="values" />
				<form:textarea rows="5" cols="50" path="values" />
			</p>
			<br/>
			<input type="submit" value="poster" />
		</form:form>
	</article>
	
	<widget:footer />
</body>
</html>
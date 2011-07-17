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
		<a href="/types/${name}">index</a> - <a href="/types/${name}/events">logs</a> - graph - <a href="/types/${name}/events/batch">batch add events</a> <br/><br/>
	
		${graph}
		
		<a href="/types/${name}/graph.csv">csv</a>
	</article>
	
	<widget:footer />
</body>
</html>
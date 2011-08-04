<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib tagdir="/WEB-INF/tags/widgets" prefix="widget" %>

<!DOCTYPE html>
<html lang="fr">

<widget:head />

<body>
	<widget:header />
	
	<article>
		<widget:nav />
	
		<form:form action="/sources/${name}/graph" commandName="form" method="GET">
			<p style="float: right">
                <input type="submit" value="display" />
            </p>
            <p style="float: right;margin-right: 20px;">
				Split on <form:input path="splitColumn"/> Sum on <form:input path="sumColumn"/>
			</p>
            <p style="float: left">
				From <form:input path="start" size="10" /> to <form:input path="end" size="10" /> with step <form:input path="stepInMinutes" size="5"/> minute(s)
			</p>
            <p style="clear:both;"/>
		</form:form>
	
		${graph}
		
		<c:set var="start"><fmt:formatDate value="${form.start}" pattern="yyyy-MM-dd" /></c:set>
		<c:set var="end"><fmt:formatDate value="${form.end}" pattern="yyyy-MM-dd" /></c:set>
		
		<c:set var="query">start=${start}&end=${end}&stepInMinutes=${form.stepInMinutes}&sumColumn=${form.sumColumn}</c:set>
		
		<a href="/sources/${name}/graph.csv?${query}">csv</a>
		- <a href="/sources/${name}/graph/table?${query}" target="_blank">table</a>
	</article>
	
	<widget:footer />
</body>
</html>
<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="source">${name}</div>

<div class="nav">
	<c:if test="${navItem eq 'index'}">
		index
	</c:if>
	<c:if test="${not (navItem eq 'index')}">
		<a href="/sources/${name}">index</a>
	</c:if>
	<c:if test="${navItem eq 'logs'}">
		- logs
	</c:if>
	<c:if test="${not (navItem eq 'logs')}">
		- <a href="/sources/${name}/events">logs</a>
	</c:if>
	<c:if test="${navItem eq 'graph'}">
		- graph
	</c:if>
	<c:if test="${not (navItem eq 'graph')}">
		- <a href="/sources/${name}/graph">graph</a>
	</c:if>
	<c:if test="${navItem eq 'batch'}">
		- batch
	</c:if>
	<c:if test="${not (navItem eq 'batch')}">
		- <a href="/sources/${name}/events/batch">batch</a>
	</c:if>
</div>
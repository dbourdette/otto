<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%--
  ~ Copyright 2011 Damien Bourdette
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<h1>Source ${fn:escapeXml(name)}</h1>

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
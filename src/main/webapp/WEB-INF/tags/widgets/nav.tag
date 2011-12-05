<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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

<h1>Source
<c:if test="${not empty source.displayGroup}">${fn:escapeXml(source.displayGroup)} /</c:if>
<c:if test="${not empty source.displayName}">${fn:escapeXml(source.displayName)}</c:if>
<c:if test="${empty source.displayName}">${fn:escapeXml(name)}</c:if>
<a href="/sources/${name}/edit">edit</a>
</h1>

<div class="nav">
    <c:if test="${navItem eq 'index'}">index</c:if>
    <c:if test="${not (navItem eq 'index')}"><a href="/sources/${name}">index</a></c:if>

    <c:if test="${navItem eq 'logs'}">- logs</c:if>
    <c:if test="${not (navItem eq 'logs')}">- <a href="/sources/${name}/events">logs</a></c:if>

    <c:if test="${navItem eq 'reports'}">- reports</c:if>
    <c:if test="${not (navItem eq 'reports')}">- <a href="/sources/${name}/reports">reports</a></c:if>

    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <c:if test="${navItem eq 'batch'}">- batch</c:if>
        <c:if test="${not (navItem eq 'batch')}">- <a href="/sources/${name}/events/batch">batch</a></c:if>
    </sec:authorize>
</div>
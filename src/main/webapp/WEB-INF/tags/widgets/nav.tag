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

<div class="page-header">
    <h1>
        Source
        <c:if test="${not empty source.displayGroup}">${fn:escapeXml(source.displayGroup)} /</c:if>
        <c:if test="${not empty source.displayName}">${fn:escapeXml(source.displayName)}</c:if>
        <c:if test="${empty source.displayName}">${fn:escapeXml(name)}</c:if>
    </h1>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <div class="pull-right">
            <a href="/sources/${name}/reports" style="margin-right: 10px">Reports</a>
            <a href="/sources/${name}/configuration" style="margin-right: 10px">Configuration</a>
            <a href="/sources/${name}/statistics" style="margin-right: 10px">Statistics</a>
            <a href="/sources/${name}/events" style="margin-right: 10px">Raw logs</a>
            <a href="/sources/${name}/events/batch">Batch</a>
        </div>
    </sec:authorize>
</div>
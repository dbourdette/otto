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
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <div class="pull-right">
            <div class="btn-group">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    Navigate
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li>
                        <a href="/sources/${name}/reports">Reports</a>
                    </li>
                    <li>
                        <a href="/sources/${name}/configuration">Configuration</a>
                    </li>
                    <li>
                        <a href="/sources/${name}/statistics">Statistics</a>
                    </li>
                    <li>
                        <a href="/sources/${name}/events">Raw logs</a>
                    </li>
                    <li>
                        <a href="/sources/${name}/events/batch">Batch</a>
                    </li>
                </ul>
            </div>
        </div>
    </sec:authorize>
    <h1>
        Source
        <c:if test="${not empty source.displayGroup}">${fn:escapeXml(source.displayGroup)} /</c:if>
        <c:if test="${not empty source.displayName}">${fn:escapeXml(source.displayName)}</c:if>
        <c:if test="${empty source.displayName}">${fn:escapeXml(name)}</c:if>
    </h1>
</div>
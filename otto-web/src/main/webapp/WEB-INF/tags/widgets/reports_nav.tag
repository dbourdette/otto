<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

<form:form action="" commandName="form" method="GET">
    <p style="float: left">
        Period <form:select path="period" items="${form.periods}" />
    </p>

    <p style="float: right">
        <button type="submit" tabindex="7" class="btn btn-primary">display</button>
    </p>

    <p style="float: right;margin-right: 20px;">
        <c:forEach var="reportConfig" items="${form.reportConfigs}">
            <label class="radio inline">
            <form:radiobutton path="reportId" value="${reportConfig.id}"/> ${reportConfig.title}
            </label>
        </c:forEach>
    </p>

    <p style="clear:both;"/>
</form:form>

<c:set var="query" value="period=${form.period}&amp;reportId=${form.reportId}"/>

<div class="nav">
    <c:if test="${subNavItem eq 'graph'}">graph</c:if>
    <c:if test="${not (subNavItem eq 'graph')}"><a href="/sources/${name}/reports/graph?${query}">graph</a></c:if>

    <c:if test="${subNavItem eq 'pie'}">- pie</c:if>
    <c:if test="${not (subNavItem eq 'pie')}">- <a href="/sources/${name}/reports/pie?${query}">pie</a></c:if>

    <c:if test="${subNavItem eq 'stats'}">- table</c:if>
    <c:if test="${not (subNavItem eq 'stats')}">- <a href="/sources/${name}/reports/stats?${query}">table</a></c:if>

    - <a href="/sources/${name}/reports/csv?${query}">csv <i class="icon-download"></i></a>
</div>
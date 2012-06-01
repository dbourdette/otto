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

<form:form action="" commandName="form" method="GET" cssClass="container" id="reportForm">
    <span id="simplePanel">
        Period <form:select path="period" items="${form.periods}" />
        <form:hidden path="reportId" id="reportId" />
        <form:hidden path="advanced" id="advanced" />
        <a href="#" onclick="showAdvanced(); return false">advanced form</a>
    </span>
    <span id="advancedPanel">
        From : <form:input path="from" data-datepicker="datepicker" cssClass="input-small" />
        To : <form:input path="to" data-datepicker="datepicker" cssClass="input-small" />
        <a href="#" onclick="showSimple(); return false">basic form</a>
    </span>

    <div class="btn-group pull-right">
        <button class="btn btn-primary" type="submit">${fn:escapeXml(form.reportTitle)}</button>
        <button class="btn dropdown-toggle btn-primary" data-toggle="dropdown">
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <c:forEach var="reportConfig" items="${form.reportConfigs}">
                <li><a href="#" onclick="$('#reportId').val('${reportConfig.id}'); $('#reportForm').submit(); return false;">${fn:escapeXml(reportConfig.title)}</a></li>
            </c:forEach>
        </ul>
    </div>
    <script type="text/javascript">
        function showAdvanced() {
            $("#advanced").val('true');
            $("#simplePanel").hide();
            $("#advancedPanel").show();
        }

        function showSimple() {
            $("#advanced").val('false');
            $("#simplePanel").show();
            $("#advancedPanel").hide();
        }

        <c:if test="${form.advanced}">showAdvanced();</c:if>
        <c:if test="${not form.advanced}">showSimple();</c:if>
    </script>
</form:form>

<c:set var="query" value="period=${form.period}&amp;reportId=${form.reportId}&amp;advanced=${form.advanced}&amp;from=${form.formattedFrom}&amp;to=${form.formattedTo}"/>

<div class="nav">
    <c:if test="${subNavItem eq 'graph'}">graph</c:if>
    <c:if test="${not (subNavItem eq 'graph')}"><a href="/sources/${name}/reports/graph?${query}">graph</a></c:if>

    <c:if test="${subNavItem eq 'pie'}">- pie</c:if>
    <c:if test="${not (subNavItem eq 'pie')}">- <a href="/sources/${name}/reports/pie?${query}">pie</a></c:if>

    <c:if test="${subNavItem eq 'stats'}">- table</c:if>
    <c:if test="${not (subNavItem eq 'stats')}">- <a href="/sources/${name}/reports/stats?${query}">table</a></c:if>

    - <a href="/sources/${name}/reports/csv?${query}">csv <i class="icon-download"></i></a>
</div>
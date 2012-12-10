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

<c:set var="query" value="period=${form.period}&amp;reportId=${form.reportId}&amp;advanced=${form.advanced}&amp;from=${form.formattedFrom}&amp;to=${form.formattedTo}"/>

<form:form action="" commandName="form" method="GET" cssClass="row" id="reportForm">
    <div class="span4">
        <span id="simplePanel">
            Period <form:select path="period" items="${form.periods}" cssClass="span2" />
            <form:hidden path="reportId" id="reportId" />
            <form:hidden path="advanced" id="advanced" />
            <a href="#" onclick="showAdvanced(); return false">advanced form</a>
        </span>
        <span id="advancedPanel">
            From : <form:input path="from" data-datepicker="datepicker" cssClass="input-small" />
            To : <form:input path="to" data-datepicker="datepicker" cssClass="input-small" />
            <a href="#" onclick="showSimple(); return false">basic form</a>
        </span>
    </div>

    <div class="span4">
        <ul class="nav nav-pills">
            <li><span>Reports :</span></li>
            <c:forEach var="reportConfig" items="${form.reportConfigs}">
                <li class="${form.reportTitle eq reportConfig.title ? 'active' : ''}"><a href="#" onclick="$('#reportId').val('${reportConfig.id}'); $('#reportForm').submit(); return false;">${fn:escapeXml(reportConfig.title)}</a></li>
            </c:forEach>
        </ul>
    </div>

    <div class="span4">
        <ul class="nav nav-pills pull-right">
            <li><span>Format :</span></li>
            <c:forEach var="format" items="${formats}">
                <li class="${currentFormat eq format.name ? 'active' : ''}">
                    <a href="/sources/${name}/reports/${format.name}?${query}">
                        ${format.name}
                        <c:if test="${not empty format.contentType}"><i class="icon-download"></i></c:if>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</form:form>

<script type="text/javascript">
    $('#period').change(function() {$('#reportForm').submit()});

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
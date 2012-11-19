<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../directives.jsp"%>

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

<!DOCTYPE html>
<html lang="en">

<layout:head/>

<body>
<layout:header/>

<div class="container">
    <widget:nav />

    <h2>
        <c:if test="${not empty form.id}">
            <a href="/sources/${name}/aggregation/${form.id}/delete" class="pull-right btn btn-danger">delete</a>
        </c:if>
        Aggregation configuration
    </h2>

    <div class="row">
        <div class="span12">
            <br>
            Aggregation allows events to be merged if they holds the exact same attributes.
            <br>
            Date attribute is rounded according to configured time frame (MILLISECOND means no rounding).
            <br>
            For all aggregated event, sum attribute is increased and available in reports.
            <br><br>
        </div>
    </div>

    <form:form action="/sources/${name}/aggregation" commandName="form" method="post" cssClass="form-horizontal">
        <bootstrap:control path="timeFrame" label="Time frame">
            <form:select path="timeFrame" id="timeFrame" items="${timeFrames}" />
        </bootstrap:control>
        <bootstrap:control path="attributeName" label="Sum attribute">
            <form:input path="attributeName" id="attributeName"/>
        </bootstrap:control>
        <bootstrap:submit cancelUrl="/sources/${name}/configuration" />
    </form:form>
</div>

<layout:footer/>
</body>
</html>
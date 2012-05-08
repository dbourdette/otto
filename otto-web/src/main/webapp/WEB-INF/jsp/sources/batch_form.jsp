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

    <form:form action="/sources/${name}/events/batch" commandName="form" method="post" cssClass="form-horizontal">
        <bootstrap:control path="count" label="Number of times">
            <form:input path="count" id="count" cssClass="input-mini" />
        </bootstrap:control>
        <bootstrap:control path="dateType" label="Date">
            <label class="radio inline">
                <form:radiobutton path="dateType" value="CURRENT" />CURRENT
            </label>
            <label class="radio inline">
                <form:radiobutton path="dateType" value="RANDOM_TODAY" />RANDOM_TODAY
            </label>
            <label class="radio inline">
                <form:radiobutton path="dateType" value="RANDOM_LAST_7_DAYS" />RANDOM_LAST_7_DAYS
            </label>
        </bootstrap:control>
        <bootstrap:control path="valuesType" label="Values type">
            <label class="radio inline">
                <form:radiobutton path="valuesType" value="KEY_VALUES" />KEY_VALUES
            </label>
            <label class="radio inline">
                <form:radiobutton path="valuesType" value="JSON" />JSON
            </label>
        </bootstrap:control>
        <bootstrap:control path="values" label="Values">
            <form:textarea rows="10" style="width:400px" path="values" id="values" />
        </bootstrap:control>
        <bootstrap:submit label="Post" />
    </form:form>
</div>

<layout:footer/>
</body>
</html>
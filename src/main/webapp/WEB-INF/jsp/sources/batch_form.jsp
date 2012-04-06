<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
<%@ taglib tagdir="/WEB-INF/tags/widgets" prefix="widget" %>

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
        <c:set var="errors"><form:errors path="count"/></c:set>
        <div class="control-group ${not empty errors ? 'error' : ''}">
            <label class="control-label" for="count">number of times</label>
            <div class="controls">
                <form:input path="count" id="count" cssClass="input-mini" />
                <form:errors path="count" cssClass="help-inline" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">date</label>
            <div class="controls">
                <label class="radio">
                    <form:radiobutton path="dateType" value="CURRENT" />CURRENT
                </label>
                <label class="radio">
                    <form:radiobutton path="dateType" value="RANDOM_TODAY" />RANDOM_TODAY
                </label>
                <label class="radio">
                    <form:radiobutton path="dateType" value="RANDOM_LAST_7_DAYS" />RANDOM_LAST_7_DAYS
                </label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">values type</label>
            <div class="controls">
                <label class="radio">
                    <form:radiobutton path="valuesType" value="KEY_VALUES" />KEY_VALUES
                </label>
                <label class="radio">
                    <form:radiobutton path="valuesType" value="JSON" />JSON
                </label>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="values">values</label>
            <div class="controls">
                <form:textarea rows="10" style="width:400px" path="values" id="values" />
                <form:errors path="values" />
            </div>
        </div>
        <div class="span6 offset5">
            <button type="submit" class="btn btn-primary">Post</button>
        </div>
    </form:form>
</div>

<layout:footer/>
</body>
</html>
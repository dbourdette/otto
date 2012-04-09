<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../directives.jsp" %>

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
    <widget:nav/>

    <h2>
        <c:if test="${not empty form.id}">
            <a href="/sources/${name}/report/${form.id}/delete" class="pull-right btn btn-danger">delete</a>
        </c:if>
        Report
    </h2>

    <form:form action="/sources/${name}/report" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id"/>
        <bootstrap:control path="title" label="Title">
            <form:input path="title" size="30"/>
        </bootstrap:control>
        <bootstrap:control path="labelAttributes" label="Label attributes">
            <form:input path="labelAttributes" size="50"/> (comma separated values)
        </bootstrap:control>
        <bootstrap:control path="valueAttribute" label="Value attribute">
            <form:input path="valueAttribute" cssClass="input-medium"/>
        </bootstrap:control>
        <bootstrap:control path="sort" label="Sort by">
            <form:select path="sort">
                <form:option value=""/>
                <form:options items="${form.sorts}"/>
            </form:select>
        </bootstrap:control>
        <fieldset>
            <legend>Tokenize</legend>
            <bootstrap:control path="tokenize" label="Tokenize">
                <form:checkbox path="tokenize"/>
            </bootstrap:control>
            <bootstrap:control path="tokenizeSeparator" label="Tokenize separator">
                <form:input path="tokenizeSeparator" size="4" cssClass="input-mini"/> (default is single space)
            </bootstrap:control>
            <bootstrap:control path="tokenizeStopWords" label="Tokenize stop words">
                <form:input path="tokenizeStopWords" size="50" cssClass="input-xxlarge"/>
            </bootstrap:control>
        </fieldset>
        <fieldset>
            <legend>Operations</legend>
            <bootstrap:control path="noAccent" label="Remove accent">
                <form:checkbox path="noAccent"/>
            </bootstrap:control>
            <bootstrap:control path="noPunctuation" label="Remove punctuation">
                <form:checkbox path="noPunctuation"/>
            </bootstrap:control>
            <bootstrap:control path="lowerCase" label="Lower case">
                <form:checkbox path="lowerCase"/>
            </bootstrap:control>
        </fieldset>
        <bootstrap:submit cancelUrl="/sources/${name}/configuration" />
    </form:form>
</div>

<layout:footer/>
</body>
</html>
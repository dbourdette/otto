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

    <div class="row">
        <div class="span12">
            <br>
            Configure a report available to all users through graphs and stats (see <a href="#example">examples below</a>).
            <br><br>
        </div>
    </div>

    <form:form action="/sources/${name}/report" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id"/>
        <bootstrap:control path="title" label="Title">
            <form:input path="title" size="30"/>
        </bootstrap:control>
        <bootstrap:control path="labelAttributes" label="Label attributes">
            <form:input path="labelAttributes" size="50"/> attribute used to create an entry in report (comma separated values)
        </bootstrap:control>
        <bootstrap:control path="valueAttribute" label="Value attribute">
            <form:input path="valueAttribute" cssClass="input-medium"/> adds the value of this attribute instead of adding 1. This is particularly useful when aggregation is active.
        </bootstrap:control>
        <bootstrap:control path="sort" label="Sort by">
            <form:select path="sort">
                <form:option value=""/>
                <form:options items="${form.sorts}"/>
            </form:select>
        </bootstrap:control>
        <fieldset>
            <legend>Tokenize label</legend>
            <div>
                <br>
                For this report, the label attribute will be split according to separator.
                Each token will count as a new entry for this report.
                <br><br>
            </div>
            <bootstrap:control path="tokenize" label="Tokenize">
                <form:checkbox path="tokenize"/>
            </bootstrap:control>
            <bootstrap:control path="tokenizeSeparator" label="Separator">
                <form:input path="tokenizeSeparator" size="4" cssClass="input-mini"/> (default is single space)
            </bootstrap:control>
            <bootstrap:control path="tokenizeStopWords" label="Stop words">
                <form:input path="tokenizeStopWords" size="50" cssClass="input-xxlarge"/> ignored tokens
            </bootstrap:control>
        </fieldset>
        <fieldset>
            <legend>Operations on label</legend>
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

    <h2 id="example">Examples</h2>
    <div class="well">
        Here are some stored events :
        <br>
        <pre style="color: white;background-color: black;margin: 10px 0">
{ "_id" : { "$oid" : "4ed7ac2a0aebe37106dc3ff1"} , "date" : { "$date" : "2011-12-01T16:33:48Z"}, "url" : "http://fake.com/url1", tags : "tag1,tag2" }
{ "_id" : { "$oid" : "4ed7ac2a0aebe37106dc3ff1"} , "date" : { "$date" : "2011-12-01T16:33:05Z"}, "url" : "http://fake.com/url2", tags : "tag3" }
{ "_id" : { "$oid" : "4ed7ac2a0aebe37106dc3ff1"} , "date" : { "$date" : "2011-12-01T16:32:42Z"}, "url" : "http://fake.com/url1", tags : "tag1,tag2" }</pre>
        Now, if we create a report using attribute "url" as a Label, we'll get the following result :
        <table class="table table-bordered" style="margin: 10px 0">
            <tr>
                <td>http://fake.com/url1</td>
                <td>2</td>
            </tr>
            <tr>
                <td>http://fake.com/url2</td>
                <td>1</td>
            </tr>
        </table>
        For a report on tags (Label attributes = 'tags', Tokenize = true, Separator = ',') :
        <table class="table table-bordered" style="margin: 10px 0">
            <tr>
                <td>tag1</td>
                <td>2</td>
            </tr>
            <tr>
                <td>tag2</td>
                <td>2</td>
            </tr>
            <tr>
                <td>tag3</td>
                <td>1</td>
            </tr>
        </table>
    </div>
</div>

<layout:footer/>
</body>
</html>
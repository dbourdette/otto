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

    <h2>Add an index</h2>

    <form:form action="/sources/${name}/indexes" commandName="form" method="post" cssClass="form-horizontal">
        <bootstrap:control path="key" label="Key">
            <form:input path="key"/> (ex : date)
        </bootstrap:control>
        <bootstrap:control path="ascending" label="Ascending">
            <form:checkbox path="ascending"/> (should be descending for date key)
        </bootstrap:control>
        <bootstrap:control path="background" label="Background">
            <form:checkbox path="background"/> (build index without blocking database)
        </bootstrap:control>
        <bootstrap:submit label="Create" cancelUrl="/sources/${name}/configuration" />
    </form:form>
</div>

<layout:footer/>
</body>
</html>
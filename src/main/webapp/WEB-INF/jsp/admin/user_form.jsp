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
    <widget:admin_nav />

    <h2>User form</h2>

    <form:form action="/users" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id" />
        <bootstrap:control path="username" label="Username">
            <form:input path="username" id="username"/>
        </bootstrap:control>
        <bootstrap:control path="password" label="Password">
            <form:password path="password" showPassword="true" id="password"/> (leave blank for external auth)
        </bootstrap:control>
        <bootstrap:control path="admin" label="Admin">
            <form:checkbox path="admin" id="admin"/>
        </bootstrap:control>
        <bootstrap:control path="sources" label="Sources">
            <form:input path="sources" size="80" id="sources"/> (comma separated allowed sources)
        </bootstrap:control>
        <bootstrap:submit cancelUrl="/users" />
    </form:form>

    <c:if test="${not empty form.id}">

        <h2>
            Delete this user
            <form:form id="deleteForm" action="/users" commandName="form" method="delete">
                <form:hidden path="id" />
                <button type="submit" class="btn btn-danger">Delete</button>
            </form:form>
        </h2>
    </c:if>
</div>

<layout:footer/>
</body>
</html>
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
    <widget:admin_nav/>

    <h2>Mail configuration</h2>

    <form:form action="/mail/edit" commandName="form" method="post" cssClass="form-horizontal">
        <bootstrap:control path="smtp" label="Stmp">
            <form:input path="smtp" id="smtp"/>
        </bootstrap:control>
        <bootstrap:control path="port" label="Port">
            <form:input path="port" id="port"/>
        </bootstrap:control>
        <bootstrap:control path="user" label="User">
            <form:input path="user" id="user"/> (leave blank for no auth)
        </bootstrap:control>
        <bootstrap:control path="password" label="Password">
            <form:input path="password" id="password"/>
        </bootstrap:control>
        <bootstrap:control path="sender" label="Sender">
            <form:input path="sender" id="sender"/>
        </bootstrap:control>
        <bootstrap:submit label="Update" cancelUrl="/mail" />
    </form:form>
</div>

<layout:footer/>
</body>
</html>
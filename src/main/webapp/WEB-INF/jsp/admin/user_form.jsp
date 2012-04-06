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
    <widget:admin_nav />

    <h2>User form</h2>

    <form:form action="/users" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id" />
        <div class="control-group">
            <label class="control-label" for="username">username</label>
            <div class="controls"><form:input path="username" id="username"/><form:errors path="username" /></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="password">password</label>
            <div class="controls"><form:password path="password" showPassword="true" id="password"/> (leave blank for external auth)<form:errors path="password" /></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="admin">admin</label>
            <div class="controls"><form:checkbox path="admin" id="admin"/><form:errors path="admin" /></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="sources">sources</label>
            <div class="controls"><form:input path="sources" size="80" id="sources"/> (comma separated allowed sources)<form:errors path="sources" /></div>
        </div>
        <div class="span6 offset5">
            <button type="submit" class="btn btn-primary">Save</button>
            <a href="/users"><button type="submit" class="btn">Cancel</button></a>
        </div>
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
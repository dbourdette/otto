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

    <h2>All users<a href="/users/form"><button class="btn btn-primary pull-right">Add user</button></a></h2>

    <table class="table table-bordered">
        <thead>
            <tr>
                <td>user</td>
                <td>admin</td>
                <td>sources</td>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td><a href="/users/form?id=${user.id}">${fn:escapeXml(user.username)}</a></td>
                <td>${user.admin ? "yes" : "no"}</td>
                <td>${user.sources}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<layout:footer/>
</body>
</html>
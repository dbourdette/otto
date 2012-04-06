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

    <h2>
        Mail configuration
        <a href="/mail/send"><button class="btn btn-primary pull-right">Send mail</button></a>
        <a href="/mail/edit"><button class="btn btn-primary pull-right" style="margin-right: 10px;">Edit</button></a>
    </h2>
    <table class="table table-bordered">
        <tr>
            <td style="width: 200px">smtp</td>
            <td>${configuration.smtp}</td>
        </tr>
        <tr>
            <td>port</td>
            <td>${configuration.port}</td>
        </tr>
        <tr>
            <td>user</td>
            <td>${configuration.user}</td>
        </tr>
        <tr>
            <td>password</td>
            <td>${configuration.password}</td>
        </tr>
        <tr>
            <td>sender</td>
            <td>${configuration.sender}</td>
        </tr>
    </table>
</div>

<layout:footer/>
</body>
</html>


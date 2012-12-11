<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../../directives.jsp"%>

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
<layout:head title="All global schedules"/>

<body>
<layout:header/>

<div class="container">
    <widget:admin_nav />

    <h2>Global schedules<a href="/admin/schedules/form"><button class="btn btn-primary pull-right">Add schedule</button></a></h2>

    <div class="row">
        <table class="table table-bordered table-striped span12">
            <thead>
            <tr>
                <th>report</th>
                <th>subject</th>
                <th colspan="2">cron expression</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="schedule" items="${schedules}">
                <tr>
                    <td>${schedule.report}</td>
                    <td><i class="icon-envelope"></i> <a href="/admin/schedules/form?id=${schedule.id}">${schedule.title}</a></td>
                    <td>${schedule.cronExpression}</td>
                    <td style="width: 80px;padding: 2px"><a href="/admin/schedules/${schedule.id}/send" class="btn">send now</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<layout:footer/>
</body>
</html>
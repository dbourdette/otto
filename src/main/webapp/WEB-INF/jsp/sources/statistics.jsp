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

    <div class="well">Event count in db : ${source.count}</div>

    <h3>Event frequency</h3>
    <table class="table table-bordered">
        <tr>
            <td style="width: 200px;">today</td>
            <td><fmt:formatNumber value="${todayFrequency.eventsPerMinute}" pattern="# ###.######"/> events per minute</td>
        </tr>
        <tr>
            <td>yesterday</td>
            <td><fmt:formatNumber value="${yesterdayFrequency.eventsPerMinute}" pattern="# ###.######"/> events per minute</td>
        </tr>
        <tr>
            <td>last week</td>
            <td><fmt:formatNumber value="${lastWeekFrequency.eventsPerMinute}" pattern="# ###.######"/> events per minute</td>
        </tr>
    </table>

    <h3>Mongodb collections</h3>
    <table class="table table-bordered">
        <tr>
            <td style="width: 200px;">events</td>
            <td>${source.collectionName}</td>
        </tr>
        <tr>
            <td>configuration</td>
            <td>${source.configCollectionName}</td>
        </tr>
        <tr>
            <td>report configuration</td>
            <td>${source.reportsCollectionName}</td>
        </tr>
        <tr>
            <td>mail report configuration</td>
            <td>${source.mailReportsCollectionName}</td>
        </tr>
    </table>

    <h3>Statistics</h3>
    <table class="table table-bordered">
        <c:forEach var="stat" items="${source.stats}">
            <tr>
                <td style="width: 200px;">${stat.key}</td>
                <td>${stat.value}</td>
            </tr>
        </c:forEach>
    </table>

    <br/><br/>
</div>

<layout:footer/>
</body>
</html>
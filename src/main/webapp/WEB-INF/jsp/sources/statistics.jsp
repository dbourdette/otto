<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ taglib uri="http://github.com/dbourdette/otto/quartz" prefix="quartz" %>

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
<html lang="fr">

<widget:head/>

<body>
<widget:header/>

<article>
    <widget:nav/>

    <div>
        Event count in db : ${source.count} <br/>

        <h3>Event frequency</h3>
        <table>
            <colgroup>
                <col class="label">
                <col>
            </colgroup>
            <tr>
                <td>today</td>
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
        <table>
            <colgroup>
                <col class="label">
                <col>
            </colgroup>
            <tr>
                <td>events</td>
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
        <table>
            <colgroup>
                <col class="label">
                <col>
            </colgroup>
            <c:forEach var="stat" items="${source.stats}">
                <tr>
                    <td>${stat.key}</td>
                    <td>${stat.value}</td>
                </tr>
            </c:forEach>
        </table>

        <br/><br/>

    </div>
</article>

<widget:footer/>
</body>
</html>
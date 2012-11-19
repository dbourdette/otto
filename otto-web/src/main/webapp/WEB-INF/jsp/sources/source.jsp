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

    <sec:authorize access="hasRole('ROLE_ADMIN')">

        <div class="row">
            <div class="span2">
                <strong>Group and name</strong>
            </div>
            <div class="span10">
                <a href="/sources/${name}/edit" id="edit">
                    <c:if test="${not empty source.displayGroup}">${fn:escapeXml(source.displayGroup)} /</c:if>
                    <c:if test="${not empty source.displayName}">${fn:escapeXml(source.displayName)}</c:if>
                    <c:if test="${empty source.displayName}">${fn:escapeXml(name)}</c:if>
                </a>
            </div>
        </div>


        <div class="row">
            <div class="span2">
                <strong>Capping</strong>
            </div>
            <div class="span10">
                <c:if test="${source.capped}">
                    size : ${source.size}, max : <fmt:formatNumber value="${source.max}" type="number"/>
                </c:if>
                <c:if test="${not source.capped}">
                    <a href="/sources/${name}/capping/form">no capping</a>
                </c:if>
            </div>
        </div>

        <div class="row">
            <div class="span2">
                <strong>Aggregation</strong>
            </div>
            <div class="span10">
                <a href="/sources/${name}/aggregation/form">
                    <c:if test="${source.aggregationConfig.timeFrame eq 'MILLISECOND'}">none</c:if>
                    <c:if test="${not (source.aggregationConfig.timeFrame eq 'MILLISECOND')}">${source.aggregationConfig.timeFrame} on attribute ${source.aggregationConfig.attributeName}</c:if>
                </a>
            </div>
        </div>

        <div class="row">
            <div class="span2"><strong>Default gragh parameters</strong></div>
            <div class="span10">
                <a href="/sources/${name}/default-graph-params/form">
                    <c:if test="${empty source.defaultGraphParameters.period}">
                        none
                    </c:if>
                    <c:if test="${not empty source.defaultGraphParameters.period}">
                        period <b>${source.defaultGraphParameters.period}</b>
                    </c:if>
                </a>
            </div>
        </div>

        <br>

        <div class="row">
            <div class="span2"><strong>Reports</strong></div>
            <table class="table table-bordered table-striped span9">
                <thead>
                <tr>
                    <th>title</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="report" items="${reports}">
                        <tr>
                            <td><a href="/sources/${name}/report/${report.id}">${report.title}</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="span1">
                <a href="/sources/${name}/report" class="btn btn-primary pull-right">Add</a>
            </div>
        </div>

        <br>

        <div class="row">
            <div class="span2"><strong>Schedules</strong></div>
            <table class="table table-bordered table-striped span9">
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
                        <td><i class="icon-envelope"></i> <a href="/sources/${name}/schedule/${schedule.id}">${schedule.title}</a></td>
                        <td>${schedule.cronExpression}</td>
                        <td style="width: 80px;padding: 2px"><a href="/sources/${name}/schedule/${schedule.id}/send" class="btn">send now</a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <div class="span1">
                <a href="/sources/${name}/schedule" class="btn btn-primary pull-right">Add</a>
            </div>
        </div>

        <br>

        <div class="row">
            <div class="span2"><strong>Transform operations</strong></div>
            <table class="table table-bordered table-striped span9">
                <thead>
                <tr>
                    <th>parameter</th>
                    <th>operations</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="parameter" items="${source.transformConfig.config}">
                        <tr>
                            <td><a href="/sources/${name}/transform/${parameter.name}">${parameter.name}</a></td>
                            <td>${parameter.operations}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="span1">
                <a href="/sources/${name}/transform/${parameter.name}" class="btn btn-primary pull-right">Add</a>
            </div>
        </div>

        <br>

        <div class="row">
            <div class="span2"><strong>Mongodb indexes</strong></div>
            <table class="table table-bordered table-striped span9">
                <thead>
                <tr>
                    <th>name</th>
                    <th colspan="2">definition</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="index" items="${indexes}">
                        <tr>
                            <td>${fn:escapeXml(index.name)}</td>
                            <td>${index}</td>
                            <td style="width: 80px;padding: 2px; text-align: center">
                                <c:if test="${not (index.name eq '_id_')}">
                                    <a href="/sources/${name}/indexes/${index.name}/drop" class="btn btn-danger">drop</a>
                                </c:if>
                            </td>
                        </tr>

                    </c:forEach>
                </tbody>
            </table>
            <div class="span1">
                <a href="/sources/${name}/indexes/form" class="btn btn-primary pull-right">Add</a>
            </div>
        </div>

        <div class="row">
            <div class="span2">
                <strong>Delete source</strong>
            </div>
            <div class="span1">
                <a href="/sources/${name}/delete" class="btn btn-danger">Delete</a>
            </div>
        </div>
    </sec:authorize>
</div>

<layout:footer/>
</body>
</html>
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

    <form class="form-inline" action="/sources/${name}/events" method="get">
        <input type="text" name="query" class="span8" placeholder="Query" value="${fn:escapeXml(query.query)}">
        <input type="text" name="sort" class="span3" placeholder="Sort" value="${fn:escapeXml(query.sort)}">
        <button type="submit" class="btn">Search</button>
    </form>

    <div class="well">
        <h3 class="span4"><strong>${events.totalCount} events</strong></h3>
        <div class="span1 offset6">
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <a href="/sources/${name}/events/delete">
                    <button class="btn btn-danger">Clear</button></a>
            </sec:authorize>
        </div>
    </div>

    <widget:pagination path="/sources/${name}/events" page="${events}" params="${query.queryParams}"/>

    <table class="table table-bordered table-condensed">
        <c:forEach var="event" items="${events.items}">
            <tr>
                <td>${event}</td>
            </tr>
        </c:forEach>
    </table>

    <widget:pagination path="/sources/${name}/events" page="${events}" params="${query.queryParams}"/>
</div>

<layout:footer/>
</body>
</html>
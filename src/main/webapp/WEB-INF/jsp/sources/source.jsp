<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

<widget:head />

<body>
	<widget:header />
	
	<article>
		<widget:nav />

        <div>
            Event count in db : ${source.count} <br/>
            <br/>
            Capping : ${source.capped ? 'yes' : 'no'}
            <c:if test="${source.capped}">
                (size : ${source.size}, max : <fmt:formatNumber value="${source.max}" type="number" />)
            </c:if>
            <c:if test="${not source.capped}">
                (<a href="/sources/${name}/capping/form">add capping</a>)
            </c:if>
            <br/><br/>
            <a href="/sources/${name}/aggregation/form">Aggregation</a> :
            <c:if test="${aggregation.timeFrame eq 'MILLISECOND'}">none</c:if>
            <c:if test="${not (aggregation.timeFrame eq 'MILLISECOND')}">${aggregation.timeFrame} on attribute ${aggregation.attributeName}</c:if>
            <br/><br/>
            <a href="/sources/${name}/default-graph-params/form">Default gragh parameters</a> :
            Step ${defaultGraphParameters.stepInMinutes}
            <c:if test="${not empty defaultGraphParameters.splitColumn}">, split on ${defaultGraphParameters.splitColumn}</c:if>
            <c:if test="${not empty defaultGraphParameters.sumColumn}">, sum on ${defaultGraphParameters.sumColumn}</c:if>

            <h3>Event frequency</h3>
            <table>
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
                <tr>
                    <td>events</td>
                    <td>${source.collectionName}</td>
                </tr>
                <tr>
                    <td>configuration</td>
                    <td>${source.configCollectionName}</td>
                </tr>
            </table>

            <h3>Statistics</h3>
            ${source.stats}
            <br/><br/>

            <a href="/sources/${name}/delete">delete source</a>
        </div>
	</article>
	
	<widget:footer />
</body>
</html>
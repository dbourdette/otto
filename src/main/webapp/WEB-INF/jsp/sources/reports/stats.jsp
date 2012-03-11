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

        <widget:reports_nav />

        <div>
            Mean event frequency for this period : <fmt:formatNumber value="${frequency.eventsPerMinute}" pattern="# ###.######"/> events per minute

            <br><br>

            <c:set var="count" value="0" />
            <c:forEach var="value" items="${values}">
                <c:set var="count" value="${value.value + count}" />
            </c:forEach>

            <span>${form.reportConfig.title}</span><br/>
            <table>
                <tr>
                    <td><b>total</b></td>
                    <td id="total"><b>${count}</b></td>
                </tr>
                <c:forEach var="value" items="${values}">
                    <tr>
                        <td>${value.name}</td>
                        <td>${value.value}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

	</article>
	
	<widget:footer />
</body>
</html>
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
            <form:form action="/sources/${name}/stats" commandName="form" method="GET">
                <p style="float: left">
                    Period <form:select path="period" items="${form.periods}"></form:select>
                </p>
                <p style="float: right">
                    <input type="submit" value="display" tabindex="7" />
                </p>
                <p style="float: right;margin-right: 20px;">
                    Split on <form:input path="splitColumn" tabindex="4" />
                    Sum on <form:input path="sumColumn" tabindex="5" />
                    Sort by <form:select path="sort" tabindex="6">
                                <form:option value="" />
                                <form:options items="${form.sorts}" />
                            </form:select>
                </p>
                <p style="clear:both;"/>
            </form:form>
        </div>

        <div>
            <c:if test="${not empty sums}">
                <table>
                    <span>Item sums</span><br/>
                    <c:set var="sum" value="0" />
                    <c:forEach var="value" items="${sums}">
                        <tr>
                            <td>${value.name}</td>
                            <td>${value.value}</td>
                        </tr>
                        <c:set var="sum" value="${value.value + sum}" />
                    </c:forEach>
                    <tr>
                        <td><b>total sum</b></td>
                        <td><b>${sum}</b></td>
                    </tr>
                </table>
            </c:if>

            <table>
                <span>Item count</span><br/>
                <c:set var="count" value="0" />
                <c:forEach var="value" items="${counts}">
                    <tr>
                        <td>${value.name}</td>
                        <td>${value.value}</td>
                    </tr>
                    <c:set var="count" value="${value.value + count}" />
                </c:forEach>
                <tr>
                    <td><b>total count</b></td>
                    <td><b>${count}</b></td>
                </tr>
            </table>
        </div>

	</article>
	
	<widget:footer />
</body>
</html>
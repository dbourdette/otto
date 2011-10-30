<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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
        <widget:admin_nav />

        <div>
            <h2>mongo db</h2>
            <table>
                <colgroup>
                    <col class="label">
                    <col>
                </colgroup>
                <tr>
                    <td>url</td>
                    <td>${config.mongoUrl}</td>
                </tr>
                <tr>
                    <td>db name</td>
                    <td>${config.mongoDbName}</td>
                </tr>
                <tr>
                    <td>username</td>
                    <td>${config.mongoUsername}</td>
                </tr>
            </table>
            <h2>security</h2>
            <table>
                <colgroup>
                    <col class="label">
                    <col>
                </colgroup>
                <tr>
                    <td>default username</td>
                    <td>${config.securityDefaultUsername}</td>
                </tr>
                <tr>
                    <td>default password</td>
                    <td>*****</td>
                </tr>
            </table>
            <h2>config</h2>
            <form:form action="/configuration" commandName="form" method="post">
                <p>
                    monitoring source : <form:input path="monitoringSource"/>
                    <form:errors path="monitoringSource" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>
	</article>

	<widget:footer />
</body>
</html>
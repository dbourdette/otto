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
	
		<h2>User form</h2>

        <div>
            <form:form action="/users" commandName="form" method="post">
                <form:hidden path="id" />
                <p>
                    username : <form:input path="username"/>
                    <form:errors path="username" />
                </p>
                <p>
                    password : <form:password path="password" showPassword="true"/> (leave blank for external auth)
                    <form:errors path="password" />
                </p>
                <p>
                    admin : <form:checkbox path="admin"/>
                    <form:errors path="admin" />
                </p>
                <p>
                    sources : <form:input path="sources" size="80"/> (comma separated allowed sources)
                    <form:errors path="sources" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>

        <c:if test="${not empty form.id}">
            <br><br>

            <h2>Delete this user</h2>

            <div>
                <form:form action="/users" commandName="form" method="delete">
                    <form:hidden path="id" />
                    <input type="submit" value="delete" />
                </form:form>
            </div>
        </c:if>
	</article>
	
	<widget:footer />
</body>
</html>
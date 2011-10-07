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
	
		<h2>Mail configuration</h2>

        <div>
            <form:form action="/mail/edit" commandName="form" method="post">
                <p>
                    smtp : <form:input path="smtp"/>
                    <form:errors path="smtp" />
                </p>
				<p>
                    port : <form:input path="port"/>
                    <form:errors path="port" />
                </p>
				<p>
                    user : <form:input path="user"/> (leave blank for no auth)
                    <form:errors path="user" />
                </p>
				<p>
                    password : <form:input path="password"/>
                    <form:errors path="password" />
                </p>
				<p>
                    sender : <form:input path="sender"/>
                    <form:errors path="sender" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>
	</article>
	
	<widget:footer />
</body>
</html>
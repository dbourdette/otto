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
		<widget:nav />
	
		<h2>Default graph parameters</h2>

        <div>
            <form:form action="/sources/${name}/default-graph-params" commandName="form" method="post">
                <p>
                    Step <form:input path="stepInMinutes" size="5"/> minute(s)
                    <form:errors path="stepInMinutes" />
                </p>
                <p>
                    Split on <form:input path="splitColumn" />
                    <form:errors path="splitColumn" />
                </p>
                <p>
                    Sum on <form:input path="sumColumn" />
                    <form:errors path="sumColumn" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>
	</article>
	
	<widget:footer />
</body>
</html>
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

		<h2>Report</h2>

        <div>
            <form:form action="/sources/${name}/report" commandName="form" method="post">
                <form:hidden path="id" />
                <p>
                    title : <form:input path="title" size="30" />
                    <form:errors path="title" />
                </p>
                <p>
                    label attributes : <form:input path="labelAttributes" size="50" />  (comma separated values)
                    <form:errors path="labelAttributes" />
                </p>
                <p>
                    value attribute : <form:input path="valueAttribute" />
                    <form:errors path="valueAttribute" />
                </p>
                <p>
                    tokenize : <form:checkbox path="tokenize" />
                    <form:errors path="tokenize" />
                </p>
                <p>
                    tokenize separator : <form:input path="tokenizeSeparator" size="4" /> (default is single space)
                    <form:errors path="tokenizeSeparator" />
                </p>
                <p>
                    tokenize stop words : <form:input path="tokenizeStopWords" size="50" />
                    <form:errors path="tokenizeStopWords" />
                </p>
                <p>
                    remove accent : <form:checkbox path="noAccent" />
                    <form:errors path="noAccent" />
                </p>
                <p>
                    remove punctuation : <form:checkbox path="noPunctuation" />
                    <form:errors path="noPunctuation" />
                </p>
                <p>
                    lower case : <form:checkbox path="lowerCase" />
                    <form:errors path="lowerCase" />
                </p>
                <p>
                    sort by : <form:select path="sort">
                                <form:option value="" />
                                <form:options items="${form.sorts}" />
                            </form:select>
                    <form:errors path="sort" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>
	</article>

	<widget:footer />
</body>
</html>
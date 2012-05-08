<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

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

<div class="page-header">
    <h1>Configuration</h1>
</div>

<div class="nav">
	<c:if test="${navItem eq 'configuration'}">configuration</c:if>
	<c:if test="${not (navItem eq 'configuration')}"><a href="/configuration">configuration</a></c:if>

	<c:if test="${navItem eq 'users'}">- users</c:if>
	<c:if test="${not (navItem eq 'users')}">- <a href="/users">users</a></c:if>

    <c:if test="${navItem eq 'security'}">- security</c:if>
    <c:if test="${not (navItem eq 'security')}">- <a href="/security">security</a></c:if>

	<c:if test="${navItem eq 'mail'}">- mail</c:if>
	<c:if test="${not (navItem eq 'mail')}">- <a href="/mail">mail</a></c:if>
</div>
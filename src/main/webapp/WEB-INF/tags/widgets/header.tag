<%@ tag language="java" pageEncoding="UTF-8" %>

<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

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

<header>
    <div class="logo"><a href="/">
        <span class="o">o</span><span class="t">T</span><span class="t">T</span><span class="o">o</span></a>
    </div>
    <sec:authorize access="isAuthenticated()">
        <div class="logout"><sec:authentication property="principal.username"/> <a href="/logout">logout</a></div>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <div class="nav">
            <a href="/logs">logs</a>
            <a href="/configuration">configuration</a>
            <a href="/javamelody">javamelody</a>
        </div>
    </sec:authorize>
</header>

<widget:message/>
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

<div class="container">
    <div class="navbar">
        <div class="navbar-inner">
            <div class="container">
                <a class="brand" href="/" style="font-weight: bold;color: white;">oTTo</a>
                <sec:authorize access="isAuthenticated()">
                    <ul class="nav pull-right">
                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                            <li><a href="/logs">logs</a></li>
                            <li><a href="/configuration">configuration</a></li>
                        </sec:authorize>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <sec:authentication property="principal.username"/>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="/logout">logout</a></li>
                            </ul>
                        </li>
                    </ul>
                </sec:authorize>
            </div>
        </div>
    </div>
</div>

<widget:message/>


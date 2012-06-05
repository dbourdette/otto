<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../directives.jsp"%>

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
<html lang="en">
<layout:head/>

<body>
<layout:header/>

<div class="container">
    <widget:admin_nav />

    <div class="pull-right">
        <a href="/security"><button type="submit" class="btn btn-info">Configure</button></a>
    </div>
    <h2>Test authorization provider</h2>

    <c:if test="${not empty tried}">
        <c:if test="${success}">
            <div class="container">
                <div class="alert">
                    <a class="close" data-dismiss="alert">×</a> Success
                </div>
            </div>
        </c:if>
        <c:if test="${not success}">
            <div class="container">
                <div class="alert alert-error">
                    <a class="close" data-dismiss="alert">×</a> Failure
                    <c:if test="${not empty stack}"><br/><br/>${stack}</c:if>
                </div>
            </div>
        </c:if>
    </c:if>

    <div class="well">
        <div class="row">
            <div class="span6 offset3">
                <form:form class="form-inline" method="post" commandName="form" action="/security/test">
                    <form:errors path="*">
                        <div class="container">
                            <div class="alert alert-danger">
                                <a class="close" data-dismiss="alert">×</a>
                                    ${fn:escapeXml(messages[0])}
                            </div>
                        </div>
                    </form:errors>
                    <form:input path="username" value="" type="text" placeholder="Login"/>
                    <form:password path="password" placeholder="Password"/>
                    <button type="submit" class="btn">Submit</button>
                </form:form>
            </div>
        </div>
    </div>
</div>

<layout:footer/>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<%@ taglib tagdir="/WEB-INF/tags/layout" prefix="layout" %>
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
<html lang="en">
<layout:head/>

<body>
<layout:header/>

<div class="container">
    <widget:admin_nav/>

    <h2>Send mail</h2>

    <form:form action="/mail/send" commandName="form" method="post" cssClass="form-horizontal">
        <div class="control-group">
            <label class="control-label" for="to">to</label>
            <div class="controls"><form:input path="to" id="to" style="width:500px"/> (comma separated values)<form:errors path="to" /></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="subject">subject</label>
            <div class="controls"><form:input path="subject" id="subject" style="width:500px"/><form:errors path="subject" /></div>
        </div>
        <div class="control-group">
            <label class="control-label" for="html">html</label>
            <div class="controls"><form:textarea path="html" rows="20" style="width:500px" id="html"/><form:errors path="html" /></div>
        </div>
        <div class="span6 offset5">
            <button type="submit" class="btn btn-danger">Send</button>
            <a href="/mail"><button type="submit" class="btn">Cancel</button></a>
        </div>
    </form:form>
</div>

<layout:footer/>
</body>
</html>
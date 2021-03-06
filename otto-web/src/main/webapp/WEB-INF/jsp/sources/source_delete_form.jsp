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
    <widget:nav />

    <div class="row">
        <h2 class="span6 offset2">You are about to delete ${name} source</h2>
        <div class="span2">
            <form:form id="deleteForm" action="/sources/${name}" commandName="form" method="delete" cssClass="form-inline">
                <button type="submit" class="btn btn-danger">Confirm</button>
                <a href="/sources/${name}/configuration"><button type="submit" class="btn">Cancel</button></a>
            </form:form>
        </div>
    </div>

</div>

<layout:footer/>
</body>
</html>
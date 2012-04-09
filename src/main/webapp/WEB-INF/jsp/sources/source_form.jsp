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
    <h2>New source</h2>

    <div>
        <form:form action="/sources" commandName="form" method="post" cssClass="form-horizontal">
            <bootstrap:control path="name" label="Name">
                <form:input path="name"/> Technical name used by apis
            </bootstrap:control>
            <bootstrap:control path="displayGroup" label="Display group">
                <form:input path="displayGroup"/> For web ui
            </bootstrap:control>
            <bootstrap:control path="displayName" label="Display name">
                <form:input path="displayName"/> For web ui
            </bootstrap:control>
            <fieldset>
                <legend>Capping (optional)</legend>
                <bootstrap:control path="size" label="Size">
                    <div class="input-append"><form:input path="size" cssClass="input-small" cssStyle="text-align: right"/><span class="add-on">ex : 100M</span> Only size is required for capping</div>
                </bootstrap:control>
                <bootstrap:control path="maxEvents" label="Max">
                    <form:input path="maxEvents" cssClass="input-small" cssStyle="text-align: right"/> events (max can only be enforced at creation time)
                </bootstrap:control>
            </fieldset>
            <bootstrap:submit label="Create" cancelUrl="/sources" />
        </form:form>
    </div>
</div>

<layout:footer/>
</body>
</html>
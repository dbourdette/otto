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

    <form:form action="/security" commandName="form" method="post" cssClass="form-horizontal" id="form">
        <div class="pull-right">
            <button type="submit" class="btn btn-primary">Save</button>
            <a href="/security/test"><button type="submit" class="btn btn-info">Test</button></a>
            <a href="/security"><button type="submit" class="btn">Cancel</button></a>
        </div>
        <h2>Authorization provider</h2>
        <form:errors path="*">
            <div class="container">
                <div class="alert alert-danger">
                    <a class="close" data-dismiss="alert">×</a>
                        ${fn:escapeXml(messages[0])}
                </div>
            </div>
        </form:errors>
        <form:hidden path="code" id="code" />
        <div id="editor" class="span12" style="height: 600px; position: relative; margin: 5px 0 10px 0;">${form.code}</div>
    </form:form>

    <script>
        var editor = ace.edit("editor");
        editor.setTheme("ace/theme/textmate");
        editor.session.setMode("ace/mode/groovy");
		editor.setShowPrintMargin(false);

        $('#form').submit( function() {
            $('#code').val(editor.getValue());
            return true;
        });
    </script>
</div>

<layout:footer/>
</body>
</html>
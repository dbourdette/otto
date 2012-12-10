<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@include file="../../directives.jsp"%>

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

    <h2>Report format</h2>

    <form:form action="/admin/formats" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id" />
        <bootstrap:control path="name" label="Name">
            <form:input path="name" id="name"/>
        </bootstrap:control>
        <bootstrap:control path="index" label="Index">
            <form:input path="index" id="index"/>
        </bootstrap:control>
        <legend>Download as...</legend>
        <bootstrap:control path="downloadAs" label="Download as">
            <form:checkbox path="downloadAs" id="downloadAs"/>  (will pop to download report)
        </bootstrap:control>
        <bootstrap:control path="contentType" label="Content type">
            <form:input path="contentType" id="contentType"/>  (mime type, ex : application/csv)
        </bootstrap:control>
        <bootstrap:control path="extension" label="Extension">
            <form:input path="extension" id="extension"/>  (file extension, ex : csv)
        </bootstrap:control>
        <legend>Groovy code</legend>
        <form:hidden path="code" id="code" />
        <div id="editor" class="span12" style="height: 600px; position: relative; margin: 5px 0 10px 0;">${fn:escapeXml(form.code)}</div>
        <bootstrap:submit label="Save" cancelUrl="/admin/formats" />
    </form:form>

    <h2>Available properties</h2>
    <pre class="well"><h3>table</h3>
public interface DataTable {
    public List&lt;String&gt; getColumns();

    public List&lt;Interval&gt; getRows();

    public Integer getValue(Interval row, String column);

    public int getSum(String column);

    public void top(int count);

    public void sortAlphabetically();

    public void sortBySum();
}</pre>

    <c:if test="${not empty form.id}">
        <h2>
            Delete this format
            <form:form id="deleteForm" action="/admin/formats" commandName="form" method="delete">
                <form:hidden path="id" />
                <button type="submit" class="btn btn-danger">Delete</button>
            </form:form>
        </h2>
    </c:if>

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
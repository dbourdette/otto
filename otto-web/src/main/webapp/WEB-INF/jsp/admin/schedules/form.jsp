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

    <h2>Global mail schedule</h2>

    <form:form action="/admin/schedules" commandName="form" method="post" cssClass="form-horizontal">
        <form:errors path="*" />
        <form:hidden path="id" />
        <fieldset>
            <legend>Mail</legend>
            <bootstrap:control path="title" label="Subject">
                <form:input path="title" size="80" cssClass="input-xxlarge" />
            </bootstrap:control>
            <bootstrap:control path="to" label="To">
                <form:textarea path="to" cssClass="input-xxlarge" rows="5"/> (comma separated values)
            </bootstrap:control>
        </fieldset>
        <fieldset>
            <legend>Planification</legend>
            <bootstrap:control path="cronExpression" label="Cron expression">
                <form:input path="cronExpression"/> (leave blank for no planification)
            </bootstrap:control>
        </fieldset>
        <pre class="well">
<b>expressions are made of 5 tokens : </b>
*    *    *    *    *
┬    ┬    ┬    ┬    ┬
│    │    │    │    └───── day of week (0 - 6) (0 is Sunday, or use names)
│    │    │    └────────── month (1 - 12)
│    │    └─────────────── day of month (1 - 31)
│    └──────────────────── hour (0 - 23)
└───────────────────────── min (0 - 59)

A field may be an asterisk (*), which always stands for "every".

<b>"nicknames" are supported :</b>
@yearly	  :    Run once a year, ie.  "0 0 1 1 *".
@annually :    Run once a year, ie.  "0 0 1 1 *".
@monthly  :    Run once a month, ie. "0 0 1 * *".
@weekly	  :    Run once a week, ie.  "0 0 * * 0".
@daily	  :    Run once a day, ie.   "0 0 * * *".
@hourly	  :    Run once an hour, ie. "0 * * * *".

<b>Examples :</b>
Every minute                 : * * * * *
Every 1 minute               : * * * * *
23:00:00 every weekday night : 0 23 ? * MON-FRI

full doc on <a href="http://unixhelp.ed.ac.uk/CGI/man-cgi?crontab+5" target="_blank">cron expression <i class="icon-share"></i></a>
    </pre>
        <fieldset>
            <legend>Custom rendering</legend>
            <form:hidden path="groovyTemplate" id="groovyTemplate" />
            <div id="editor" class="span12" style="height: 600px; position: relative; margin: 5px 0 10px 0;">${fn:escapeXml(form.groovyTemplate)}</div>
        </fieldset>


    <pre class="well"><h3>Available properties</h3>
<b>title</b>
Title of this report

<b>data</b>
public interface DataTable {
    public List&lt;String&gt; getColumns();

    public List&lt;Interval&gt; getRows();

    public Integer getValue(Interval row, String column);

    public int getSum(String column);

    public void top(int count);

    public void sortAlphabetically();

    public void sortBySum();
}</pre>


        <bootstrap:submit cancelUrl="/admin/schedules" />
    </form:form>

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
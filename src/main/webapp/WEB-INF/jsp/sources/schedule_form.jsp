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

    <h2>
        <c:if test="${not empty form.id}">
            <a href="/sources/${name}/schedule/${form.id}/delete" class="pull-right btn btn-danger">delete</a>
        </c:if>
        Mail schedule
    </h2>

    <div class="row">
        <div class="span12">
            <br>
            This schedules a report by mail.
            <br><br>
        </div>
    </div>

    <form:form action="/sources/${name}/schedule" commandName="form" method="post" cssClass="form-horizontal">
        <form:hidden path="id" />
        <fieldset>
            <legend>Report</legend>
            <bootstrap:control path="report" label="Report">
                <form:select path="report" items="${reports}" itemValue="title" itemLabel="title" />
            </bootstrap:control>
            <bootstrap:control path="period" label="Period">
                <form:select path="period" items="${form.periods}" />
            </bootstrap:control>
        </fieldset>
        <fieldset>
            <legend>Mail</legend>
            <bootstrap:control path="to" label="To">
                <form:input path="to" size="50" cssClass="input-xxlarge"/> (comma separated values)
            </bootstrap:control>
            <bootstrap:control path="title" label="Subject">
                <form:input path="title" size="80" cssClass="input-xxlarge" />
            </bootstrap:control>
        </fieldset>
        <fieldset>
            <legend>Planification</legend>
            <bootstrap:control path="cronExpression" label="Cron expression">
                <form:input path="cronExpression"/>
            </bootstrap:control>
        </fieldset>
        <bootstrap:submit cancelUrl="/sources/${name}/configuration" />
    </form:form>

    <h2>Cron expressions</h2>

    <div class="well">
        Expression is used to schedule action. Expression are made of seven sub-expressions separated with white-space :
        <br/>
        <br/>
        1. Minutes
        <br/>
        2. Hours
        <br/>
        3. Day-of-Month
        <br/>
        4. Month
        <br/>
        5. Day-of-Week
        <br/>
        6. Year (optional field)
        <br/>
        <br/>
        An example of a complete cron-expression is the string "0 12 ? * WED" - which means "every Wednesday at 12:00 pm".
        <br/>
        <br/>
        Individual sub-expressions can contain ranges and/or lists. For example,
        the day of week field in the previous (which reads "WED") example could
        be replaces with "MON-FRI", "MON, WED, FRI", or even "MON-WED,SAT".
        <br/>
        <br/>
        Wild-cards (the '*' character) can be used to say "every" possible value
        of this field. Therefore the '*' character in the "Month" field of the
        previous example simply means "every month". A '*' in the Day-Of-Week
        field would obviously mean "every day of the week".
        <br/>
        <br/>
        All of the fields have a set of valid values that can be specified.
        These values should be fairly obvious - such as the numbers 0 to 59 for
        seconds and minutes, and the values 0 to 23 for hours. Day-of-Month can
        be any value 0-31, but you need to be careful about how many days are in
        a given month! Months can be specified as values between 0 and 11, or by
        using the strings JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV
        and DEC. Days-of-Week can be specified as vaules between 1 and 7 (1 =
        Sunday) or by using the strings SUN, MON, TUE, WED, THU, FRI and SAT.
        <br/>
        <br/>
        The '/' character can be used to specify increments to values. For
        example, if you put '0/15' in the Minutes field, it means 'every 15
        minutes, starting at minute zero'. If you used '3/20' in the Minutes
        field, it would mean 'every 20 minutes during the hour, starting at
        minute three' - or in other words it is the same as specifying '3,23,43'
        in the Minutes field.
        <br/>
        <br/>
        The '?' character is allowed for the day-of-month and day-of-week
        fields. It is used to specify "no specific value". This is useful when
        you need to specify something in one of the two fields, but not the
        other. See the examples below clarification.
        <br/>
        <br/>
        The 'L' character is allowed for the day-of-month and day-of-week
        fields. This character is short-hand for "last", but it has different
        meaning in each of the two fields. For example, the value "L" in the
        day-of-month field means "the last day of the month" - day 31 for
        January, day 28 for February on non-leap years. If used in the
        day-of-week field by itself, it simply means "7" or "SAT". But if used
        in the day-of-week field after another value, it means "the last xxx day
        of the month" - for example "6L" or "FRIL" both mean "the last friday of
        the month". When using the 'L' option, it is important not to specify
        lists, or ranges of values, as you'll get confusing results.
        <br/>
        <br/>
        The 'W' is used to specify the weekday (Monday-Friday) nearest the given
        day. As an example, if you were to specify "15W" as the value for the
        day-of-month field, the meaning is: "the nearest weekday to the 15th of
        the month".
        <br/>
        <br/>
        The '#' is used to specify "the nth" XXX weekday of the month. For
        example, the value of "6#3" or "FRI#3" in the day-of-week field means
        "the third Friday of the month".
        <br/>
        <br/>
        Example Cron Expressions
        <br/>
        <br/>
        Example 1 - an expression to create a trigger that simply
        fires every 5 minutes
        <br/>
        <br/>
        "0/5 * * * ?"
        <br/>
        <br/>
        Example 2 - an expression to create a trigger that fires at
        10:30, 11:30, 12:30, and 13:30, on every Wednesday and Friday.
        <br/>
        <br/>
        "30 10-13 ? * WED,FRI"
        <br/>
        <br/>
        Example 3 - an expression to create a trigger that fires
        every half hour between the hours of 8 am and 10 am on the 5th and 20th
        of every month. Note that the trigger will NOT fire at 10:00 am, just at
        8:00, 8:30, 9:00 and 9:30
        <br/>
        <br/>
        "0/30 8-9 5,20 * ?"
        <br/>
        <br/>
        Note that some scheduling requirements are too complicated to express
        with a single trigger - such as "every 5 minutes between 9:00 am and
        10:00 am, and every 20 minutes between 1:00 pm and 10:00 pm". The
        solution in this scenario is to simply create two schedules.
    </div>
</div>

<layout:footer/>
</body>
</html>
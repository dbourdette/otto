<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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
<html lang="fr">

<widget:head />

<body>
	<widget:header />

	<article>
		<widget:nav />

		<h2>Mail report</h2>

        <div>
            <form:form action="/sources/${name}/mailreport" commandName="form" method="post">
                <form:hidden path="id" />
                <p>
                    cronExpression : <form:input path="cronExpression"/>
                    <form:errors path="cronExpression" />
                </p>
                <p>
                    to : <form:input path="to" size="50"/> (comma separated values)
                    <form:errors path="to" />
                </p>
                <p>
                    title : <form:input path="title" size="80"/>
                    <form:errors path="title" />
                </p>
                <p>
                    period : <form:select path="period" items="${form.periods}"></form:select>
                    <form:errors path="period" />
                </p>
                <p>
                    report title : <form:input path="reportTitle" />
                    <form:errors path="reportTitle" />
                </p>
                <input type="submit" value="Save" />
            </form:form>
        </div>

        <h2>Cron expressions</h2>

        <div>
            Cron-Expressions are used to configure instances of CronTrigger.
            Cron-Expressions are strings that are actually made up of seven
            sub-expressions, that describe individual details of the schedule. These
            sub-expression are separated with white-space, and represent:
            <br/>
            <br/>
            1. Seconds
            <br/>
            2. Minutes
            <br/>
            3. Hours
            <br/>
            4. Day-of-Month
            <br/>
            5. Month
            <br/>
            6. Day-of-Week
            <br/>
            7. Year (optional field)
            <br/>
            <br/>
            An example of a complete cron-expression is the string "0 0 12 ? * WED"
            - which means "every Wednesday at 12:00 pm".
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
            other. See the examples below (and CronTrigger JavaDoc) for
            clarification.
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
            Here are a few more examples of expressions and their meanings - you can
            find even more in the JavaDoc for CronTrigger
            <br/>
            Example Cron Expressions
            <br/>
            <br/>
            CronTrigger Example 1 - an expression to create a trigger that simply
            fires every 5 minutes
            <br/>
            <br/>
            "0 0/5 * * * ?"
            <br/>
            <br/>
            CronTrigger Example 2 - an expression to create a trigger that fires
            every 5 minutes, at 10 seconds after the minute (i.e. 10:00:10 am,
            10:05:10 am, etc.).
            <br/>
            <br/>
            "10 0/5 * * * ?"
            <br/>
            <br/>
            CronTrigger Example 3 - an expression to create a trigger that fires at
            10:30, 11:30, 12:30, and 13:30, on every Wednesday and Friday.
            <br/>
            <br/>
            "0 30 10-13 ? * WED,FRI"
            <br/>
            <br/>
            CronTrigger Example 4 - an expression to create a trigger that fires
            every half hour between the hours of 8 am and 10 am on the 5th and 20th
            of every month. Note that the trigger will NOT fire at 10:00 am, just at
            8:00, 8:30, 9:00 and 9:30
            <br/>
            <br/>
            "0 0/30 8-9 5,20 * ?"
            <br/>
            <br/>
            Note that some scheduling requirements are too complicated to express
            with a single trigger - such as "every 5 minutes between 9:00 am and
            10:00 am, and every 20 minutes between 1:00 pm and 10:00 pm". The
            solution in this scenario is to simply create two triggers, and register
            both of them to run the same job.
        </div>
	</article>

	<widget:footer />
</body>
</html>
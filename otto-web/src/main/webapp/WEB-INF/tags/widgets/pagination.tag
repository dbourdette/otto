<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ attribute name="path" required="true" %>
<%@ attribute name="page" required="true" type="com.github.dbourdette.otto.util.Page" %>

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

<div class="row">
    <div class="span4 offset4">
        <ul class="pager">
            <li class="previous" style="${page.index > 1 ? 'visibility:visible' : 'visibility:hidden'}">
                <a href="${path}?page=${page.index - 1}">&larr; prev</a>
            </li>
            <li class="next">
                <a href="${path}?page=${page.index + 1}">next &rarr;</a>
            </li>
            <li>
                <form action="${path}" method="get" style="margin: 0">
                    <input type="text" name="page" class="input-mini" value="${page.index}" /> / ${page.pageCount}
                </form>
            </li>
        </ul>
    </div>
</div>
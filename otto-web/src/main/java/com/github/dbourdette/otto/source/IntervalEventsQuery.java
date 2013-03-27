package com.github.dbourdette.otto.source;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.github.dbourdette.otto.util.Page;
import com.github.dbourdette.otto.web.util.IntervalUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class IntervalEventsQuery implements EventsQuery {
    private DateTime from;

    private DateTime to;

    private Integer page = 0;

    private int pageSize;

    private Map<String, String> filters = new HashMap<String, String>();

    public IntervalEventsQuery() {
    }

    public IntervalEventsQuery(Interval interval) {
        setInterval(interval);
    }

    public IntervalEventsQuery(DateTime from, DateTime to) {
        setInterval(from, to);
    }

    public Page<DBObject> findPage(DBCollection events) {
        return Page.fromCursor(createCursor(events), page, pageSize);
    }

    public DBCursor createCursor(DBCollection events) {
        return events.find(createQuery()).sort(new BasicDBObject("date", -1));
    }

    public BasicDBObject createQuery() {
        BasicDBObject query = IntervalUtils.query(from, to);

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            query.append(entry.getKey(), entry.getValue());
        }

        return query;
    }

    public void addFilter(String key, String value) {
        filters.put(key, value);
    }

    public void readFilters(HttpServletRequest request, String prefix) {
        for (Object object : request.getParameterMap().keySet()) {
            String name = (String) object;

            if (StringUtils.startsWith(name, prefix)) {
                String key = StringUtils.substringAfter(name, prefix);

                addFilter(key, request.getParameter(name));
            }
        }
    }

    public void setInterval(Interval interval) {
        setInterval(interval.getStart(), interval.getEnd());
    }

    public void setInterval(DateTime from, DateTime to) {
        this.from = from;
        this.to = to;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPage(Integer page) {

        this.page = page;
    }
}

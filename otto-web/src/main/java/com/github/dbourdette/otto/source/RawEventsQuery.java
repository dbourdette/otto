package com.github.dbourdette.otto.source;

import com.github.dbourdette.otto.util.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class RawEventsQuery implements EventsQuery {

    private static final int PAGE_SIZE = 100;

    private Integer page = 0;

    private String query;

    private String sort;

    public Page<DBObject> findPage(DBCollection events) {
        return Page.fromCursor(createCursor(events), page, PAGE_SIZE);
    }

    public DBCursor createCursor(DBCollection events) {
        return events.find(createQuery()).sort(createSort());
    }

    public DBObject createQuery() {
        return StringUtils.isNotEmpty(query) ? (DBObject) JSON.parse(query) : new BasicDBObject();
    }

    public DBObject createSort() {
        return StringUtils.isNotEmpty(sort) ? (DBObject) JSON.parse(sort) : new BasicDBObject("date", -1);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public String getSort() {
        return sort;
    }

    public String getQueryParams() {
        StringBuilder builder = new StringBuilder();

        if (StringUtils.isNotEmpty(query)) {
            builder.append("query=");
            builder.append(StringEscapeUtils.escapeHtml(query));
        }

        if (StringUtils.isNotEmpty(sort)) {
            builder.append("&amp;sort=");
            builder.append(StringEscapeUtils.escapeHtml(sort));
        }

        return builder.toString();
    }


}

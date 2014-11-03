package com.github.dbourdette.otto.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Page<T> {
    private List<T> items = new ArrayList<T>();

    private int index;

    private int totalCount;

    private int pageSize;

    public static Page<DBObject> fromCursor(DBCursor cursor, Integer index, int pageSize) {
        index = fixPage(index);

        Page<DBObject> page = new Page<DBObject>();

        cursor = cursor.skip((index - 1) * pageSize).limit(pageSize);

        page.index = index;
        page.pageSize = pageSize;
        page.totalCount = cursor.count();
        page.items = cursor.toArray();

        return page;
    }

    public static int fixPage(Integer page) {
        if (page == null) {
            return 1;
        }

        if (page < 1) {
            return 1;
        }

        return page;
    }

    private Page() {}

    public int getItemCount() {
        return items.size();
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public long getPageCount() {
        return PageUtils.getPageCount(totalCount, pageSize);
    }

    public int getIndex() {
        return index;
    }

    public int getPageSize() {
        return pageSize;
    }
}

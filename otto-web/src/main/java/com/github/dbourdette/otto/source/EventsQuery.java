package com.github.dbourdette.otto.source;

import com.github.dbourdette.otto.util.Page;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public interface EventsQuery {

    public Page<DBObject> findPage(DBCollection events);
}

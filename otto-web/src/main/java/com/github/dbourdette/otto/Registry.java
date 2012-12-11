package com.github.dbourdette.otto;

import com.google.code.morphia.Datastore;
import com.mongodb.DB;

/**
 *
 * Static references to useful beans.
 * These allows domain objects to get pointers to resources and such.
 *
 * @author damien bourdette
 */
public class Registry {
    public static DB mongoDb;

    public static Datastore datastore;
}

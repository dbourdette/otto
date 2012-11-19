package com.github.dbourdette.otto;

import com.google.code.morphia.Datastore;
import com.mongodb.DB;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;

/**
 *
 * Static references to useful beans.
 * These allows domain objects to get pointers to resources and such.
 *
 * @author damien bourdette
 */
public class Registry {
    public static DB mongoDb;

    public static Cache sourceCache;

    public static Datastore datastore;

    static {
        CacheManager cacheManager = new CacheManager();

        CacheConfiguration config = new CacheConfiguration();
        config.setName("sourceCache");
        config.setTimeToIdleSeconds(60);
        config.setTimeToLiveSeconds(10 * 60);
        config.setMaxEntriesLocalHeap(1000);
        config.setOverflowToDisk(false);

        Cache sourceCache = new Cache(config);

        cacheManager.addCache(sourceCache);

        Registry.sourceCache = sourceCache;
    }
}

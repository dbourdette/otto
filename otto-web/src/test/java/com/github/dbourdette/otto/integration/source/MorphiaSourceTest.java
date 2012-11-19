package com.github.dbourdette.otto.integration.source;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class MorphiaSourceTest {
    @Before
    public void clean() throws UnknownHostException, NoSuchFieldException {
        Mongo mongo = new Mongo();

        DB db = mongo.getDB("otto-integration");
        db.dropDatabase();

        Morphia morphia = new Morphia();
        morphia.map(Source.class);

        Registry.mongoDb = db;
        Registry.datastore = morphia.createDatastore(mongo, "otto-integration");
    }

    @Test
    public void create() {
        Source.create("test");

        assertThat(Registry.datastore.find(Source.class).countAll()).isEqualTo(1);
    }

    @Test
    public void exists() {
        Source.create("test");

        assertThat(Source.exists("test")).isTrue();
    }

    @Test
    public void findByName() {
        Source.create("test");

        assertThat(Source.findByName("test").getName()).isEqualTo("test");
    }

    @Test
    public void delete() {
        Source.create("test");

        Source.findByName("test").delete();

        assertThat(Source.exists("test")).isFalse();
    }

    @Test
    public void updateDisplayName() {
        Source source =  Source.create("test");
        source.setDisplayGroup("test group");
        source.setDisplayName("test name");

        source.save();

        assertThat(Registry.datastore.find(Source.class).get().getDisplayName()).isEqualTo("test name");
    }
}

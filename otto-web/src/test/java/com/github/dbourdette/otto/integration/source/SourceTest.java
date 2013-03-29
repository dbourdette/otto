package com.github.dbourdette.otto.integration.source;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.source.reports.ReportConfig;
import com.github.dbourdette.otto.source.reports.ReportConfigs;
import com.github.dbourdette.otto.web.exception.SourceAlreadyExists;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceTest {
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
    public void createTwice() {
        Source.create("test");

        try {
            Source.create("test");

            fail("Source test already exists");
        } catch (SourceAlreadyExists e) {

        }
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
    public void findAll() {
        Source.create("test1");
        Source.create("test2");

        assertThat(Source.findAll().size()).isEqualTo(2);
        assertThat(Source.findAll()).contains(Source.findByName("test1"));
        assertThat(Source.findAll()).contains(Source.findByName("test2"));
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

    @Test
    public void reportConfigs() {
        Source source = Source.create("test");

        ReportConfig config = new ReportConfig();
        config.setTitle("hits");
        config.setValueAttribute("hits");

        config.setSourceName(source.getName());
        config.save();

        Assert.assertEquals(1, ReportConfigs.forSource(source).getReportConfigs().size());
        Assert.assertEquals("hits", ReportConfigs.forSource(source).getReportConfigs().get(0).getValueAttribute());

        config = ReportConfigs.forSource(source).getReportConfig(config.getId());

        Assert.assertEquals("hits", config.getValueAttribute());

        config.setValueAttribute("slug");
        config.save();

        Assert.assertEquals("slug", ReportConfigs.forSource(source).getReportConfigs().get(0).getValueAttribute());
    }
}

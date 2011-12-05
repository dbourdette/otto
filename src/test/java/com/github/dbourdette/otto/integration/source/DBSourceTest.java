package com.github.dbourdette.otto.integration.source;

import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.dbourdette.otto.source.DBSource;
import com.github.dbourdette.otto.source.config.ReportConfig;
import com.github.dbourdette.otto.web.form.SourceForm;
import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class DBSourceTest {
    private DBSource source;

    @Before
    public void init() throws UnknownHostException, NoSuchFieldException {
        Mongo mongo = new Mongo();
        DB db = mongo.getDB("otto-integration");

        SourceForm sourceForm = new SourceForm();
        sourceForm.setName("test");

        if (DBSource.exists(db, "test")) {
            DBSource.loadFromDb(db, "test").drop();
        }

        source = DBSource.create(db, sourceForm);
    }

    @Test
    public void reportConfigs() {
        ReportConfig config = new ReportConfig();
        config.setTitle("hits");
        config.setSumOn("hits");

        source.saveReportConfig(config);
        source.saveReportConfig(config);

        Assert.assertEquals(1, source.getReportConfigs().size());
        Assert.assertEquals("hits", source.getReportConfigs().get(0).getSumOn());

        config = source.getReportConfig(config.getId());

        Assert.assertEquals("hits", config.getSumOn());

        config.setSumOn("slug");
        source.saveReportConfig(config);

        Assert.assertEquals("slug", source.getReportConfigs().get(0).getSumOn());
    }
}

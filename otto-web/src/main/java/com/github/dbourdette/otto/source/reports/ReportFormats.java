package com.github.dbourdette.otto.source.reports;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.web.exception.FormatNotFound;
import com.google.code.morphia.Datastore;

@Service
public class ReportFormats {

    @Inject
    private Datastore datastore;
    
    @PostConstruct
    public void initDB() throws IOException {
        if (datastore.find(ReportFormat.class).countAll() != 0) {
            return;
        }

        datastore.save(ReportFormat.loadDefault("graph", "google-chart").withIndex(1));
        datastore.save(ReportFormat.loadDefault("pie", "google-pie").withIndex(2));
        datastore.save(ReportFormat.loadDefault("table", "table").withIndex(3));
        datastore.save(ReportFormat.loadDefault("csv", "csv").withDownloadAs("application/csv", "csv").withIndex(4));
    }

    public ReportFormat findById(String id) {
        ReportFormat format = datastore.find(ReportFormat.class).filter("id", new ObjectId(id)).get();

        assertExists(format);

        return format;
    }

    public ReportFormat findByName(String name) {
        ReportFormat format = datastore.find(ReportFormat.class).filter("name", name).get();

        assertExists(format);

        return format;
    }

    public List<ReportFormat> findBySource(Source source) {
        return datastore.find(ReportFormat.class).filter("sourceName", source.getName()).order("index").asList();
    }

    public List<ReportFormat> findForAllSources() {
        return datastore.find(ReportFormat.class).filter("sourceName", Source.ALL_SOURCES).order("index").asList();
    }

    public void save(ReportFormat format) {
        datastore.save(format);
    }

    public void delete(String id) {
        datastore.delete(findById(id));
    }

    public static void assertExists(ReportFormat format) {
        if (format == null) {
            throw new FormatNotFound();
        }
    }
}

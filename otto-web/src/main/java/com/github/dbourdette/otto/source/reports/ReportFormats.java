package com.github.dbourdette.otto.source.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;

public class ReportFormats {
    public static final String ALL_SOURCES = "*";

    public static ReportFormat findById(String id) {
        return Registry.datastore.find(ReportFormat.class).filter("id", new ObjectId(id)).get();
    }

    public static ReportFormat findByName(String name) {
        return Registry.datastore.find(ReportFormat.class).filter("name", name).get();
    }

    public static List<ReportFormat> findBySource(Source source) {
        return Registry.datastore.find(ReportFormat.class).filter("sourceName", source.getName()).order("index").asList();
    }

    public static List<ReportFormat> findForAllSources() {
        return Registry.datastore.find(ReportFormat.class).filter("sourceName", ALL_SOURCES).order("index").asList();
    }

    public static List<ReportFormat> findDefaults() {
        try {
            List<ReportFormat> formats = new ArrayList<ReportFormat>();

            formats.add(ReportFormat.loadDefault("graph", "google-chart"));
            formats.add(ReportFormat.loadDefault("pie", "google-pie"));
            formats.add(ReportFormat.loadDefault("table", "table"));
            formats.add(ReportFormat.loadDefault("csv", "csv").withContentType(""));

            return formats;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load default format templates", e);
        }
    }

    public static void save(ReportFormat format) {
        Registry.datastore.save(format);
    }

    public static void delete(String id) {
        Registry.datastore.delete(findById(id));
    }
}

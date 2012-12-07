package com.github.dbourdette.otto.source.display;

import java.util.List;

import org.bson.types.ObjectId;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.source.Source;
import com.github.dbourdette.otto.web.util.MongoCollections;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity(value = MongoCollections.SOURCES_DISPLAYS, noClassnameStored = true)
public class ReportDisplay {
    private static final String ALL_SOURCES = "*";

    @Id
    private ObjectId id;

    @Property
    private String sourceName;

    @Property
    private String groovyCode;

    public static List<ReportDisplay> findBySource(Source source) {
        return Registry.datastore.find(ReportDisplay.class).filter("sourceName", source.getName()).asList();
    }

    public static List<ReportDisplay> findDefault() {
        return Registry.datastore.find(ReportDisplay.class).filter("sourceName", ALL_SOURCES).asList();
    }

    public String getGroovyCode() {
        return groovyCode;
    }

    public void setGroovyCode(String groovyCode) {
        this.groovyCode = groovyCode;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}

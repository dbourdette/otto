package com.github.dbourdette.otto.source.reports;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.web.util.MongoCollections;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

@Entity(value = MongoCollections.SOURCES_FORMATS, noClassnameStored = true)
public class ReportFormat {
    @Id
    private ObjectId id;

    @Property
    private String sourceName;

    @Property
    @NotEmpty
    private String name;

    @Property
    private int index = 1;

    @Property
    private String code;

    @Property
    private boolean downloadAs;

    @Property
    private String contentType;

    @Property
    private String extension;

    public static ReportFormat loadDefault(String name, String filename) throws IOException {
        URL url = ReportFormat.class.getResource("defaults/" + filename + ".template");

        ReportFormat format = new ReportFormat();

        format.sourceName = ReportFormats.ALL_SOURCES;
        format.name = name;
        format.code = FileUtils.readFileToString(FileUtils.toFile(url), "UTF-8");

        return format;
    }

    public String render(SimpleDataTable table) {
        Map<String, Object> bindings = new HashMap<String, Object>();

        bindings.put("table", table);

        return render(bindings);
    }

    public String render(Map bindings) {
        try {
            return buildTemplate().make(bindings).toString();
        } catch (Exception e) {
            return ExceptionUtils.getFullStackTrace(e);
        }
    }

    public Template buildTemplate() throws IOException, ClassNotFoundException {
        return new SimpleTemplateEngine().createTemplate(code);
    }

    public ReportFormat withContentType(String contentType) {
        this.contentType = contentType;

        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isDownloadAs() {
        return downloadAs;
    }

    public void setDownloadAs(boolean downloadAs) {
        this.downloadAs = downloadAs;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}

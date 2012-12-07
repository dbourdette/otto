package com.github.dbourdette.otto.source.reports;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.Registry;
import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.data.filler.LowerCaseOperation;
import com.github.dbourdette.otto.data.filler.NoAccentOperation;
import com.github.dbourdette.otto.data.filler.NoPunctuationOperation;
import com.github.dbourdette.otto.data.filler.OperationChain;
import com.github.dbourdette.otto.data.filler.TokenizeOperation;
import com.github.dbourdette.otto.web.form.Sort;
import com.github.dbourdette.otto.web.util.MongoCollections;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Entity(value = MongoCollections.SOURCES_REPORTS, noClassnameStored = true)
public class ReportConfig {
    @Id
    private ObjectId id;

    @Property
    private String sourceName;

    @NotEmpty
    @Property
    private String title;

    @Property
    private String labelAttributes;

    @Property
    private String valueAttribute;

    @Property
    private boolean tokenize;

    @Property
    private String tokenizeSeparator;

    @Property
    private String tokenizeStopWords;

    @Property
    private Sort sort;

    @Property
    private boolean lowerCase;

    @Property
    private boolean noAccent;

    @Property
    private boolean noPunctuation;

    public ReportConfig() {
    }

    public ReportConfig(String sourceName) {
        this.sourceName = sourceName;
    }

    public void save() {
        Registry.datastore.save(this);
    }

    public void delete() {
        Registry.datastore.delete(this);
    }

    public OperationChain buildChain(SimpleDataTable report) {
        OperationChain chain = OperationChain.forTable(report);

        chain.setLabelAttributes(labelAttributes);
        chain.setValueAttribute(valueAttribute);

        if (tokenize) {
            TokenizeOperation tokenize = new TokenizeOperation();

            if (StringUtils.isNotEmpty(tokenizeSeparator)) {
                tokenize.setSeparator(tokenizeSeparator);
            }

            if (StringUtils.isNotEmpty(tokenizeStopWords)) {
                tokenize.setStopWords(StringUtils.split(tokenizeStopWords, ","));
            }

            chain.add(tokenize);
        }

        if (noAccent) {
            chain.add(new NoAccentOperation());
        }

        if (noPunctuation) {
            chain.add(new NoPunctuationOperation());
        }

        if (lowerCase) {
            chain.add(new LowerCaseOperation());
        }

        return chain;
    }

    public Sort[] getSorts() {
        return Sort.values();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabelAttributes() {
        return labelAttributes;
    }

    public void setLabelAttributes(String labelAttributes) {
        this.labelAttributes = labelAttributes;
    }

    public String getValueAttribute() {
        return valueAttribute;
    }

    public void setValueAttribute(String valueAttribute) {
        this.valueAttribute = valueAttribute;
    }

    public boolean isTokenize() {
        return tokenize;
    }

    public void setTokenize(boolean tokenize) {
        this.tokenize = tokenize;
    }

    public String getTokenizeSeparator() {
        return tokenizeSeparator;
    }

    public void setTokenizeSeparator(String tokenizeSeparator) {
        this.tokenizeSeparator = tokenizeSeparator;
    }

    public String getTokenizeStopWords() {
        return tokenizeStopWords;
    }

    public void setTokenizeStopWords(String tokenizeStopWords) {
        this.tokenizeStopWords = tokenizeStopWords;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public boolean isLowerCase() {
        return lowerCase;
    }

    public void setLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    public boolean isNoAccent() {
        return noAccent;
    }

    public void setNoAccent(boolean noAccent) {
        this.noAccent = noAccent;
    }

    public boolean isNoPunctuation() {
        return noPunctuation;
    }

    public void setNoPunctuation(boolean noPunctuation) {
        this.noPunctuation = noPunctuation;
    }
}

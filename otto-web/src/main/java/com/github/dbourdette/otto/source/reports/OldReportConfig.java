package com.github.dbourdette.otto.source.reports;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.dbourdette.otto.data.SimpleDataTable;
import com.github.dbourdette.otto.data.filler.LowerCaseOperation;
import com.github.dbourdette.otto.data.filler.NoAccentOperation;
import com.github.dbourdette.otto.data.filler.NoPunctuationOperation;
import com.github.dbourdette.otto.data.filler.OperationChain;
import com.github.dbourdette.otto.data.filler.TokenizeOperation;
import com.github.dbourdette.otto.web.form.Sort;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class OldReportConfig {
    private String id;

    @NotEmpty
    private String title;

    private String labelAttributes;

    private String valueAttribute;

    private boolean tokenize;

    private String tokenizeSeparator;

    private String tokenizeStopWords;

    private Sort sort;

    private boolean lowerCase;

    private boolean noAccent;

    private boolean noPunctuation;

    public static List<OldReportConfig> readAll(DBCursor cursor) {
        List<OldReportConfig> result = new ArrayList<OldReportConfig>();

        while (cursor.hasNext()) {
            result.add(read((BasicDBObject) cursor.next()));
        }

        return result;
    }

    public static OldReportConfig read(BasicDBObject object) {
        OldReportConfig config = new OldReportConfig();

        config.setId(((ObjectId) object.get("_id")).toString());
        config.setTitle(object.getString("title"));
        config.setLabelAttributes(object.getString("labelAttributes"));
        config.setValueAttribute(object.getString("valueAttribute"));
        config.setTokenize(object.getBoolean("tokenize"));
        config.setTokenizeSeparator(object.getString("tokenizeSeparator"));
        config.setTokenizeStopWords(object.getString("tokenizeStopWords"));
        config.setNoAccent(object.getBoolean("noAccent", false));
        config.setNoPunctuation(object.getBoolean("noPunctuation", false));
        config.setLowerCase(object.getBoolean("lowerCase", false));

        if (object.containsField("sort")) {
            config.setSort(Sort.valueOf((String) object.get("sort")));
        }

        return config;
    }

    public BasicDBObject toDBObject() {
        BasicDBObject object = new BasicDBObject();

        if (StringUtils.isNotEmpty(id)) {
            object.put("_id", new ObjectId(id));
        }

        object.put("title", title);
        object.put("labelAttributes", labelAttributes);
        object.put("valueAttribute", valueAttribute);
        object.put("tokenize", tokenize);
        object.put("tokenizeSeparator", tokenizeSeparator);
        object.put("tokenizeStopWords", tokenizeStopWords);
        object.put("noAccent", noAccent);
        object.put("noPunctuation", noPunctuation);
        object.put("lowerCase", lowerCase);

        if (sort != null) {
            object.put("sort", sort.name());
        }

        return object;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Sort[] getSorts() {
        return Sort.values();
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

package com.github.dbourdette.otto.source.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.github.dbourdette.otto.report.Report;
import com.github.dbourdette.otto.report.filler.OperationChain;
import com.github.dbourdette.otto.report.filler.LowerCaseOperation;
import com.github.dbourdette.otto.report.filler.NoAccentOperation;
import com.github.dbourdette.otto.report.filler.NoPunctuationOperation;
import com.github.dbourdette.otto.report.filler.SplitOperation;
import com.github.dbourdette.otto.report.filler.SumOperation;
import com.github.dbourdette.otto.report.filler.TokenizeOperation;
import com.github.dbourdette.otto.web.form.Sort;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class ReportConfig {
    private String id;

    private String title;

    private String splitOn;

    private String sumOn;

    private String tokenizeOn;

    private String tokenizeStopWords;

    private Sort sort;

    private boolean lowerCase;

    private boolean noAccent;

    private boolean noPunctuation;

    public static List<ReportConfig> readAll(DBCursor cursor) {
        List<ReportConfig> result = new ArrayList<ReportConfig>();

        while (cursor.hasNext()) {
            result.add(read((BasicDBObject) cursor.next()));
        }

        return result;
    }

    public static ReportConfig read(BasicDBObject object) {
        ReportConfig config = new ReportConfig();

        config.setId(((ObjectId) object.get("_id")).toString());
        config.setTitle(object.getString("title"));
        config.setSplitOn(object.getString("splitOn"));
        config.setSumOn(object.getString("sumOn"));
        config.setTokenizeOn(object.getString("tokenizeOn"));
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
        object.put("splitOn", splitOn);
        object.put("sumOn", sumOn);
        object.put("tokenizeOn", tokenizeOn);
        object.put("tokenizeStopWords", tokenizeStopWords);
        object.put("noAccent", noAccent);
        object.put("noPunctuation", noPunctuation);
        object.put("lowerCase", lowerCase);

        if (sort != null) {
            object.put("sort", sort.name());
        }

        return object;
    }

    public OperationChain buildChain(Report report) {
        OperationChain chain = OperationChain.forGraph(report);

        if (StringUtils.isNotEmpty(splitOn)) {
            SplitOperation split = new SplitOperation();
            split.setColumns(splitOn);
            chain.add(split);
        }

        if (StringUtils.isNotEmpty(tokenizeOn)) {
            TokenizeOperation tokenize = new TokenizeOperation();
            tokenize.setColumn(tokenizeOn);

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

        if (StringUtils.isNotEmpty(sumOn)) {
            SumOperation sum = new SumOperation();
            sum.setColumn(sumOn);
            chain.add(sum);
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

    public String getSplitOn() {
        return splitOn;
    }

    public void setSplitOn(String splitOn) {
        this.splitOn = splitOn;
    }

    public String getSumOn() {
        return sumOn;
    }

    public void setSumOn(String sumOn) {
        this.sumOn = sumOn;
    }

    public String getTokenizeOn() {
        return tokenizeOn;
    }

    public void setTokenizeOn(String tokenizeOn) {
        this.tokenizeOn = tokenizeOn;
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

package com.github.dbourdette.otto.source.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.github.dbourdette.otto.report.Report;
import com.github.dbourdette.otto.report.filler.FillerChain;
import com.github.dbourdette.otto.report.filler.SplitFiller;
import com.github.dbourdette.otto.report.filler.SumFiller;
import com.github.dbourdette.otto.report.filler.TokenizeFiller;
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

        if (sort != null) {
            object.put("sort", sort.name());
        }

        return object;
    }

    public FillerChain buildChain(Report report) {
        FillerChain chain = FillerChain.forGraph(report);

        if (StringUtils.isNotEmpty(splitOn)) {
            SplitFiller split = new SplitFiller();
            split.setColumns(splitOn);
            chain.add(split);
        }

        if (StringUtils.isNotEmpty(tokenizeOn)) {
            TokenizeFiller tokenize = new TokenizeFiller();
            tokenize.setColumn(tokenizeOn);

            if (StringUtils.isNotEmpty(tokenizeStopWords)) {
                tokenize.setStopWords(StringUtils.split(tokenizeStopWords, ","));
            }

            chain.add(tokenize);
        }

        if (StringUtils.isNotEmpty(sumOn)) {
            SumFiller sum = new SumFiller();
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

}

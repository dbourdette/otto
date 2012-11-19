package com.github.dbourdette.otto.selenium.pages;

import com.github.dbourdette.otto.source.config.AggregationConfig;

import static fr.javafreelance.fluentlenium.core.filter.FilterConstructor.withText;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceAggregationForm extends PageSupport {
    public String getUrl(String name) {
        return ROOT + "/sources/" + name + "/aggregation/form";
    }

    public void update(AggregationConfig config) {
        $("#timeFrame").find("option", withText(config.getTimeFrame().name())).click();
        $("#attributeName").text(config.getAttributeName());

        $("#attributeName").submit();
    }
}

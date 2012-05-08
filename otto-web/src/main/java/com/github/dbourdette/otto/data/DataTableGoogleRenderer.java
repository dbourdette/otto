package com.github.dbourdette.otto.data;

import java.util.UUID;

/**
 * @author damien bourdette
 */
public abstract class DataTableGoogleRenderer implements DataTableRenderer {
    protected static final int WIDTH = 1080, HEIGHT = 750;

    @Override
    public String render(DataTable table) {
        String elementId = "chart_div_" + UUID.randomUUID().toString();

        return toGoogleHtml(elementId, toGooglejs(table, elementId));
    }

    protected abstract String toGooglejs(DataTable table, String elementId);

    public String toGoogleHtml(String elementId, String js) {
        StringBuilder builder = new StringBuilder();

        builder.append("<div id=\"" + elementId + "\"></div>");

        builder.append("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");
        builder.append("google.setOnLoadCallback(drawChart);");
        builder.append("function drawChart() {");

        builder.append(js);

        builder.append("}");
        builder.append("</script>");

        return builder.toString();
    }
}

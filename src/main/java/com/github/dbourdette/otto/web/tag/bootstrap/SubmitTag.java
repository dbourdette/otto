package com.github.dbourdette.otto.web.tag.bootstrap;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class SubmitTag extends SimpleTagSupport {
    private String label = "Save";

    private String cancelUrl;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter writer = getJspContext().getOut();

        writer.write("<div class=\"span6 offset5\">\n");
        writer.write("<button type=\"submit\" class=\"btn btn-primary\">" + label + "</button>\n");

        if (StringUtils.isNotEmpty(cancelUrl)) {
            writer.write("<a href=\"" + cancelUrl + "\"><button type=\"submit\" class=\"btn\">Cancel</button></a>\n");
        }

        writer.write("</div>\n");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
}

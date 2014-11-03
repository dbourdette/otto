package com.github.dbourdette.otto.web.tag.bootstrap;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.NestedPathTag;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class ControlTag extends BodyTagSupport {
    private String label;

    private String path;

    @Override
    public int doStartTag() throws JspException {
        String error = getErrorMessage();

        write("<div class=\"control-group" + getErrorCssClass() + " \">");
        write("<label class=\"control-label\" for=\"" + path + "\">" + label + "</label>");
        write("<div class=\"controls\">");

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        if (hasError()) {
            write("<span class=\"help-inline\">");
            write(getErrorMessage());
            write("</span>");
        }

        write("</div>");
        write("</div>");

        return EVAL_PAGE;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String getErrorCssClass() {
        return hasError() ? " error" : "";
    }

    private boolean hasError() {
        return StringUtils.isNotEmpty(getErrorMessage());
    }

    private String getErrorMessage() {
        RequestContext requestContext = (RequestContext) this.pageContext.getAttribute(RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE);

        String nestedPath = (String) this.pageContext.getAttribute(NestedPathTag.NESTED_PATH_VARIABLE_NAME, PageContext.REQUEST_SCOPE);

        BindStatus bindStatus = new BindStatus(requestContext, nestedPath + path, false);

        return bindStatus.getErrorMessage();
    }

    private void write(String string) {
        try {
            pageContext.getOut().write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

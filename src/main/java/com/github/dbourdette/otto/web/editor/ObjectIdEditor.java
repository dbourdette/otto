package com.github.dbourdette.otto.web.editor;

import java.beans.PropertyEditorSupport;

import org.bson.types.ObjectId;

/**
 * @author damien bourdette
 */
public class ObjectIdEditor extends PropertyEditorSupport {
    @Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (org.apache.commons.lang.StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            setValue(new ObjectId(text));
        }
	}

	@Override
	public String getAsText() {
		ObjectId value = (ObjectId) getValue();
		return (value != null ? value.toString() : "");
	}
}

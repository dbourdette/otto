package org.otto.web.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.Min;

import com.google.common.base.Splitter;
import com.mongodb.BasicDBObject;

/**
 * @author damien bourdette
 */
public class BatchForm {
	private String values;

	@Min(1)
	private Integer count = 1;

	public List<BasicDBObject> buildDBObjects() {
		List<BasicDBObject> objects = new ArrayList<BasicDBObject>();

		for (int i = 0; i < count; i++) {
			BasicDBObject object = new BasicDBObject();

			Iterable<String> keyValues = Splitter.on(",").trimResults().omitEmptyStrings().split(values);

			for (String keyValue : keyValues) {
				Iterator<String> iterator = Splitter.on("=").trimResults().omitEmptyStrings().split(keyValue)
						.iterator();

				object.append(iterator.next(), iterator.next());
			}

			if (object.containsField("date") || (!(object.get("date") instanceof Date))) {
				object.append("date", new Date());
			}

			objects.add(object);
		}

		return objects;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}

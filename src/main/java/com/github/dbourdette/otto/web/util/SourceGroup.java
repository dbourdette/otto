package com.github.dbourdette.otto.web.util;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.github.dbourdette.otto.source.Source;

/**
 * Used for jsp layout by group
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceGroup {
    private String name;

    private Set<Source> sources = new TreeSet<Source>(new Comparator<Source>() {
        @Override
        public int compare(Source source1, Source source2) {
            String name = StringUtils.isEmpty(source1.getDisplayName()) ? source1.getName() : source1.getDisplayName();
            String name1 = StringUtils.isEmpty(source2.getDisplayName()) ? source2.getName() : source2.getDisplayName();

            return name.compareTo(name1);
        }
    });

    public SourceGroup(String name) {
        this.name = name == null ? "" : name;
    }

    public void addSource(Source source) {
        sources.add(source);
    }

    public Set<Source> getSources() {
        return sources;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SourceGroup{" +
                "name='" + name + '\'' +
                ", sources=" + sources +
                '}';
    }
}

package com.github.dbourdette.otto.web.util;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.github.dbourdette.otto.source.DBSource;

/**
 * Used for jsp layout by group
 *
 * @author damien bourdette
 * @version \$Revision$
 */
public class SourceGroup {
    private String name;

    private Set<DBSource> sources = new TreeSet<DBSource>(new Comparator<DBSource>() {
        @Override
        public int compare(DBSource dbSource, DBSource dbSource1) {
            String name = StringUtils.isEmpty(dbSource.getDisplayName()) ? dbSource.getName() : dbSource.getDisplayName();
            String name1 = StringUtils.isEmpty(dbSource1.getDisplayName()) ? dbSource1.getName() : dbSource1.getDisplayName();

            return name.compareTo(name1);
        }
    });

    public SourceGroup(String name) {
        this.name = name == null ? "" : name;
    }

    public void addSource(DBSource source) {
        sources.add(source);
    }

    public Set<DBSource> getSources() {
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

package org.otto.util;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
public class PageUtils {
    public static int getPageCount(int totalCount, int pageSize) {
        if (totalCount <= 0) {
            return 1;
        }

        int pageCount = totalCount / pageSize;

        if (pageCount * pageSize != totalCount) {
            pageCount += 1;
        }

        return pageCount;
    }
}

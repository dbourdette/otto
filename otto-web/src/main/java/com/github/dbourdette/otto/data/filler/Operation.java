package com.github.dbourdette.otto.data.filler;

import java.util.List;

/**
 * A baic operation to transform the value that will be written in the {@link com.github.dbourdette.otto.data.DataTable}.
 */
public interface Operation {
    public List<String> handle(String column);
}

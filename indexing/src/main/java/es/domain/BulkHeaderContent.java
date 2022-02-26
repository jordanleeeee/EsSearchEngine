package es.domain;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

/**
 * @author Jordan
 */
public class BulkHeaderContent {
    @NotNull
    @Property(name = "_index")
    public String index;

    @NotNull
    @Property(name = "_id")
    public String id;

}

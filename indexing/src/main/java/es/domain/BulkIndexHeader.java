package es.domain;

import core.framework.api.json.Property;

/**
 * @author Jordan
 */
public class BulkIndexHeader {
    @Property(name = "index")
    public BulkHeaderContent content;
}

package es.domain;

import core.framework.api.json.Property;

/**
 * @author Jordan
 */
public class BulkUpdateHeader {
    @Property(name = "update")
    public BulkHeaderContent content;
}

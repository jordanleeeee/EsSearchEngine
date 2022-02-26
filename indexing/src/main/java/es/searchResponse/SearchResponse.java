package es.searchResponse;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.util.List;

/**
 * @author Jordan
 */
public class SearchResponse {
    @NotNull
    @Property(name = "hits")
    public Hits hits;

    public static class Hits {
        @NotNull
        @Property(name = "hits")
        public List<HitDetails> hitDetails;
    }

    public static class HitDetails {
        @NotNull
        @Property(name = "_id")
        public String _id;

        @NotNull
        @Property(name = "_source")
        public Source source;
    }

    public static class Source {
        @NotNull
        @Property(name = "url")
        public String url;

        @NotNull
        @Property(name = "links")
        public List<String> links;
    }
}

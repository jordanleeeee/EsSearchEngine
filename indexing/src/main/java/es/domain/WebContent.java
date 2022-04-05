package es.domain;

import core.framework.api.json.Property;
import core.framework.api.validate.NotNull;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan
 */
public class WebContent {
    @NotNull
    @Property(name = "url")
    public String url;

    @Property(name = "title")
    public String title;

    @NotNull
    @Property(name = "body")
    public Body body;

    @NotNull
    @Property(name = "links")
    public List<String> links;

    @NotNull
    @Property(name = "size")
    public Integer size;

    @Property(name = "updatedTime")
    public ZonedDateTime updatedTime;

    @NotNull
    @Property(name = "tags")
    public List<String> tags = new ArrayList<>();

    @NotNull
    @Property(name = "pageRank")
    public Double pageRank = 1.0;

    @NotNull
    @Property(name = "shop")
    public String shop;

    public static class Body {
        @NotNull
        @Property(name = "h1")
        public List<String> h1 = new ArrayList<>();

        @NotNull
        @Property(name = "h2")
        public List<String> h2 = new ArrayList<>();

        @NotNull
        @Property(name = "content")
        public String content;
    }
}

package app;

import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.module.App;

/**
 * @author Jordan
 */
public class SearchingServiceApp extends App {
    @Override
    protected void initialize() {
        bind(HTTPClient.class, HTTPClient.builder().build());

        http().route(HTTPMethod.POST, "/:path(*)", bind(SearchController.class));
    }
}

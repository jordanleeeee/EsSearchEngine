package app;

import app.web.HomeController;
import app.web.SearchController;
import core.framework.api.http.HTTPStatus;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.module.App;
import core.framework.module.SystemModule;
import core.framework.web.Response;

/**
 * @author Jordan
 */
public class SearchingServiceApp extends App {
    @Override
    protected void initialize() {
        load(new SystemModule("sys.properties"));
        loadProperties("app.properties");

        bind(HTTPClient.class, HTTPClient.builder().build());

        http().route(HTTPMethod.GET, "/", bind(HomeController.class));
        http().route(HTTPMethod.GET, "/:path(*)", bean(HomeController.class));
        http().route(HTTPMethod.POST, "/ajax/:path(*)", bind(new SearchController(requiredProperty("app.elasticSearchURL"))));

        // for pre-flight in CORS
        http().route(HTTPMethod.OPTIONS, "/ajax/:path(*)",
                request -> Response.empty()
                                   .header("Access-Control-Allow-Origin", "*")
                                   .header("Access-Control-Allow-Methods", "POST")
                                   .header("Access-Control-Allow-Headers", "content-type")
                                   .status(HTTPStatus.OK)
        );
    }
}

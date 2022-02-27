package app;

import core.framework.api.http.HTTPStatus;
import core.framework.http.ContentType;
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

        bind(HTTPClient.class, HTTPClient.builder().build());

        http().route(HTTPMethod.POST, "/:path(*)", bind(SearchController.class));

        http().route(HTTPMethod.OPTIONS, "/:path(*)", request -> Response.empty()
                           .header("Access-Control-Allow-Origin", "*")
                           .header("Access-Control-Allow-Methods", "POST")
                           .header("Access-Control-Allow-Headers", "content-type")
                           .contentType(ContentType.APPLICATION_JSON)
                           .status(HTTPStatus.OK)
        );
    }
}

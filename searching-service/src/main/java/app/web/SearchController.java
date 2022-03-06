package app.web;

import app.util.Formatter;
import core.framework.api.http.HTTPStatus;
import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.http.HTTPResponse;
import core.framework.inject.Inject;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jordan
 */
public class SearchController implements Controller {
    private static final Map<Integer, HTTPStatus> HTTP_STATUS_MAP =
            Arrays.stream(HTTPStatus.class.getEnumConstants())
                  .map(enumConst -> Map.entry(enumConst.code, enumConst))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final String esUrl;

    @Inject
    HTTPClient client;

    public SearchController(String esUrl) {
        this.esUrl = esUrl;
    }

    @Override
    public Response execute(Request request) {
        String path = request.path().substring(5);

        if (isValidSearchQuery(request)) {
            return Response.text("invalid search query").status(HTTPStatus.BAD_REQUEST);
        }

        HTTPRequest httpRequest = new HTTPRequest(HTTPMethod.POST, esUrl + path);
        httpRequest.body(request.body().orElseThrow(), ContentType.APPLICATION_JSON);
        HTTPResponse response = client.execute(httpRequest);
        logger.info("es response= " + Formatter.parseJson(response.text()));

        return Response.bytes(response.body)
                       .contentType(ContentType.APPLICATION_JSON)
                       .header("Access-Control-Allow-Origin", "*")
                       .status(HTTP_STATUS_MAP.getOrDefault(response.statusCode, HTTPStatus.OK));
    }

    public boolean isValidSearchQuery(Request request) {
        String[] parts = request.path().substring(5).split("/");
        if ((parts.length != 2 || !parts[0].isEmpty() || !"_search".equals(parts[1]))
                && (parts.length != 3 || !parts[0].isEmpty() || parts[1].isEmpty() || !"_search".equals(parts[2]))) {
            return false;
        }
        return request.body().isEmpty();
    }
}

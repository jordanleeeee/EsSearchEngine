package app;

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
    @Inject
    HTTPClient client;

    @Override
    public Response execute(Request request) {
        String[] parts = request.path().split("/");
        if ((parts.length != 2 || !parts[0].isEmpty() || !"_search".equals(parts[1]))
                && (parts.length != 3 || !parts[0].isEmpty() || parts[1].isEmpty() || !"_search".equals(parts[2]))) {
            return Response.text("query not allow").status(HTTPStatus.BAD_REQUEST);
        }
        if (request.body().isEmpty()) {
            return Response.text("empty request body").status(HTTPStatus.BAD_REQUEST);
        }
        HTTPRequest httpRequest = new HTTPRequest(HTTPMethod.POST, "http://127.0.0.1:9200" + request.path());
        httpRequest.body(request.body().orElseThrow(), ContentType.APPLICATION_JSON);
        HTTPResponse response = client.execute(httpRequest);
        logger.info("es response= " + Formatter.parseJson(response.text()));

        return Response
                .text(response.text())
                .contentType(ContentType.APPLICATION_JSON)
                .header("Access-Control-Allow-Origin", "*")
                .status(HTTP_STATUS_MAP.getOrDefault(response.statusCode, HTTPStatus.OK));
    }
}

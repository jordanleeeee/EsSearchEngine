package app;

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

import java.util.Arrays;

/**
 * @author Jordan
 */
public class SearchController implements Controller {
    @Inject
    HTTPClient client;

    @Override
    public Response execute(Request request) {
        String[] parts = request.path().split("/");
        if ((!parts[0].isEmpty() || !parts[1].equals("_search") || parts.length != 2) &&
                (!parts[0].isEmpty() || parts[1].isEmpty() || !parts[2].equals("_search") || parts.length != 3)) {
            return Response.text("query not allow").status(HTTPStatus.BAD_REQUEST);
        }
        if (request.body().isEmpty()) {
            return Response.text("empty request body").status(HTTPStatus.BAD_REQUEST);
        }
        HTTPRequest httpRequest = new HTTPRequest(HTTPMethod.POST, "http://127.0.0.1:9200" + request.path());
        httpRequest.body(request.body().orElseThrow(), ContentType.APPLICATION_JSON);
        HTTPResponse response = client.execute(httpRequest);
        return Response.text(response.text()).contentType(ContentType.APPLICATION_JSON).status(toStatus(response.statusCode));
    }

    private HTTPStatus toStatus(int statusCode) {
        return Arrays.stream(HTTPStatus.class.getEnumConstants()).filter(status -> status.code == statusCode).findAny().orElse(HTTPStatus.OK);
    }
}

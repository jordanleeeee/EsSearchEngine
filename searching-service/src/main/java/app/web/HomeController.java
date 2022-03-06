package app.web;

import core.framework.api.http.HTTPStatus;
import core.framework.http.ContentType;
import core.framework.log.Markers;
import core.framework.util.ClasspathResources;
import core.framework.web.Request;
import core.framework.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jordan
 */
public class HomeController {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public Response index(Request request) {
        return Response.bytes(ClasspathResources.bytes("web/index.html"))
                       .header("Access-Control-Allow-Origin", "*")
                       .contentType(ContentType.TEXT_HTML)
                       .status(HTTPStatus.OK);
    }

    public Response icon(Request request) {
        return Response.bytes(ClasspathResources.bytes("web/favicon.ico"))
                       .header("Access-Control-Allow-Origin", "*")
                       .contentType(ContentType.create("image/x-icon", null))
                       .status(HTTPStatus.OK);
    }

    public Response staticResources(Request request) {
        String resourcesPath = request.path();
        try {
            return Response.bytes(ClasspathResources.bytes("web" + resourcesPath))
                           .header("Access-Control-Allow-Origin", "*")
                           .contentType(getContentType(resourcesPath))
                           .status(HTTPStatus.OK);
        } catch (Error error) {
            logger.warn(Markers.errorCode("RESOURCES_NOT_FOUND"), error.getMessage());
            return Response.bytes(ClasspathResources.bytes("web/notFound.html"))
                           .contentType(ContentType.TEXT_HTML)
                           .status(HTTPStatus.NOT_FOUND);
        }
    }

    private ContentType getContentType(String path) {
        if (path.endsWith(".css")) {
            return ContentType.TEXT_CSS;
        } else if (path.endsWith(".js")) {
            return ContentType.APPLICATION_JAVASCRIPT;
        } else {
            return ContentType.TEXT_HTML;
        }
    }
}

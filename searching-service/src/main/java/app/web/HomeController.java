package app.web;

import core.framework.api.http.HTTPStatus;
import core.framework.http.ContentType;
import core.framework.log.Markers;
import core.framework.util.ClasspathResources;
import core.framework.web.Controller;
import core.framework.web.Request;
import core.framework.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jordan
 */
public class HomeController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Override
    public Response execute(Request request) {
        String resourcesPath = request.path();
        if ("/".equals(resourcesPath)) resourcesPath = "/index.html";
        try {
            return Response.bytes(ClasspathResources.bytes("web" + resourcesPath))
                           .header("Access-Control-Allow-Origin", "*")
                           .contentType(ContentType.parse(resourcesPath))
                           .contentType(getContentType(resourcesPath))
                           .status(HTTPStatus.OK);
        } catch (Error error) {
            logger.warn(Markers.errorCode("RESOURCES_NOT_FOUND"), error.getMessage());
            return Response.text(ClasspathResources.text("web/notFound.html"))
                           .contentType(ContentType.TEXT_HTML)
                           .status(HTTPStatus.NOT_FOUND);
        }
    }

    private ContentType getContentType(String path) {
        if (path.endsWith(".css")) {
            return ContentType.TEXT_CSS;
        } else if (path.endsWith(".js")) {
            return ContentType.APPLICATION_JAVASCRIPT;
        } else if (path.endsWith(".ioc")) {
            return ContentType.IMAGE_PNG;
        } else {
            return ContentType.TEXT_HTML;
        }
    }
}

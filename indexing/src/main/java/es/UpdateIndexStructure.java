package es;

import config.Configuration;
import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.util.ClasspathResources;

/**
 * @author Jordan
 */
public class UpdateIndexStructure {
    public static void main(String[] args) {
        HTTPClient client = HTTPClient.builder().build();

        HTTPRequest deleteIndexRequest = new HTTPRequest(HTTPMethod.DELETE, Configuration.esPath + "/restaurant");
        System.out.println(client.execute(deleteIndexRequest).text());

        HTTPRequest createIndexRequest = new HTTPRequest(HTTPMethod.PUT, Configuration.esPath + "/restaurant");
        createIndexRequest.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest).text());

    }
}

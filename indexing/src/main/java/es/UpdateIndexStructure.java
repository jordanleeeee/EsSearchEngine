package es;

import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.util.ClasspathResources;

/**
 * @author Jordan
 */
public class UpdateIndexStructure {
    private static final HTTPClient client = HTTPClient.builder().build();

    public static void main(String[] args) {
        HTTPRequest deleteIndexRequest = new HTTPRequest(HTTPMethod.DELETE, "http://127.0.0.1:9200/mcdonalds");
        System.out.println(client.execute(deleteIndexRequest).text());

        HTTPRequest createIndexRequest = new HTTPRequest(HTTPMethod.PUT, "http://127.0.0.1:9200/mcdonalds");
        createIndexRequest.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest).text());

        HTTPRequest deleteIndexRequest2 = new HTTPRequest(HTTPMethod.DELETE, "http://127.0.0.1:9200/starbucks");
        System.out.println(client.execute(deleteIndexRequest2).text());

        HTTPRequest createIndexRequest2 = new HTTPRequest(HTTPMethod.PUT, "http://127.0.0.1:9200/starbucks");
        createIndexRequest2.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest2).text());

        HTTPRequest deleteIndexRequest3 = new HTTPRequest(HTTPMethod.DELETE, "http://127.0.0.1:9200/subway");
        System.out.println(client.execute(deleteIndexRequest3).text());

        HTTPRequest createIndexRequest3 = new HTTPRequest(HTTPMethod.PUT, "http://127.0.0.1:9200/subway");
        createIndexRequest3.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest3).text());
    }
}

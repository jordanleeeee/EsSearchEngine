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

        HTTPRequest deleteIndexRequest = new HTTPRequest(HTTPMethod.DELETE, Configuration.esPath + "/mcdonalds");
        System.out.println(client.execute(deleteIndexRequest).text());

        HTTPRequest createIndexRequest = new HTTPRequest(HTTPMethod.PUT, Configuration.esPath + "/mcdonalds");
        createIndexRequest.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest).text());

        HTTPRequest deleteIndexRequest2 = new HTTPRequest(HTTPMethod.DELETE, Configuration.esPath + "/starbucks");
        System.out.println(client.execute(deleteIndexRequest2).text());

        HTTPRequest createIndexRequest2 = new HTTPRequest(HTTPMethod.PUT, Configuration.esPath + "/starbucks");
        createIndexRequest2.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest2).text());

        HTTPRequest deleteIndexRequest3 = new HTTPRequest(HTTPMethod.DELETE, Configuration.esPath + "/subway");
        System.out.println(client.execute(deleteIndexRequest3).text());

        HTTPRequest createIndexRequest3 = new HTTPRequest(HTTPMethod.PUT, Configuration.esPath + "/subway");
        createIndexRequest3.body(ClasspathResources.text("index.json"), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(createIndexRequest3).text());
    }
}

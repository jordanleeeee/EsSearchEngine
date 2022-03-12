package es;

import config.Configuration;
import core.framework.crypto.Hash;
import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.json.Bean;
import core.framework.util.Strings;
import es.domain.WebContent;
import util.WebsiteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan
 */
public final class IndexingManager {
    private static final IndexingManager MANAGER = new IndexingManager();
    private static final int BULK_SIZE = 50;

    public static IndexingManager getInstance() {
        return MANAGER;
    }

    private final HTTPClient client;
    private final List<String> bulkIndexRequestBodyLine;

    private IndexingManager() {
        Bean.register(WebContent.class);
        client = HTTPClient.builder().build();
        bulkIndexRequestBodyLine = new ArrayList<>();
    }

    @Deprecated
    public void indexOne(String siteName, WebContent webContent) {
        String documentId = Hash.sha1Hex(webContent.url).substring(20);
        String url = Strings.format(Configuration.esPath + "/{}/_doc/{}", siteName, documentId);
        HTTPRequest postRequest = new HTTPRequest(HTTPMethod.POST, url);
        postRequest.body(Bean.toJSON(webContent), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(postRequest).text());
    }

    public void index(String siteName, WebContent webContent) {
        String docId = WebsiteUtils.getUrlId(webContent.url);
        String headerPart = "{\"index\":{\"_index\":\"" + siteName + "\",\"_id\":\"" + docId + "\"}}";
        bulkIndexRequestBodyLine.add(headerPart);
        bulkIndexRequestBodyLine.add(Bean.toJSON(webContent));

        if (bulkIndexRequestBodyLine.size() > BULK_SIZE * 2) sendBulkRequest();
    }

    public void update(String siteName, String docId, String updatedFieldAndValue) {
        String headerPart = "{\"update\":{\"_index\":\"" + siteName + "\",\"_id\":\"" + docId + "\"}}";
        bulkIndexRequestBodyLine.add(headerPart);
        String updatePart = "{\"doc\": " + updatedFieldAndValue + "}";
        bulkIndexRequestBodyLine.add(updatePart);

        if (bulkIndexRequestBodyLine.size() > BULK_SIZE * 2) sendBulkRequest();
    }

    public void flushBulkRequest() {
        sendBulkRequest();
    }

    private void sendBulkRequest() {
        if (bulkIndexRequestBodyLine.isEmpty()) return;
        System.out.println("sent bulk index request");
        HTTPRequest postRequest = new HTTPRequest(HTTPMethod.POST, Configuration.esPath + "/_bulk");
        String body = String.join("\n", bulkIndexRequestBodyLine) + '\n';
        postRequest.body(body, ContentType.APPLICATION_JSON);
        System.out.println(client.execute(postRequest).text());
        bulkIndexRequestBodyLine.clear();
    }
}

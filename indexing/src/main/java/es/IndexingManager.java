package es;

import core.framework.crypto.Hash;
import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.json.Bean;
import core.framework.util.Strings;
import es.domain.BulkHeaderContent;
import es.domain.BulkIndexHeader;
import es.domain.BulkUpdateHeader;
import es.domain.WebContent;
import util.WebsiteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan
 */
public class IndexingManager {
    private static final IndexingManager manager = new IndexingManager();
    private final HTTPClient client;
    private final List<String> bulkIndexRequestBodyLine;
    private static final int bulkSize = 100;

    private IndexingManager() {
        Bean.register(WebContent.class);
        Bean.register(BulkIndexHeader.class);
        Bean.register(BulkUpdateHeader.class);
        client = HTTPClient.builder().build();
        bulkIndexRequestBodyLine = new ArrayList<>();
    }

    public static IndexingManager getInstance() {
        return manager;
    }

    @Deprecated
    public void indexOne(String siteName, WebContent webContent) {
        String documentId = Hash.sha1Hex(webContent.url).substring(20);
        String url = Strings.format("http://127.0.0.1:9200/{}/_doc/{}", siteName, documentId);
        HTTPRequest postRequest = new HTTPRequest(HTTPMethod.POST, url);
        postRequest.body(Bean.toJSON(webContent), ContentType.APPLICATION_JSON);
        System.out.println(client.execute(postRequest).text());
    }

    public void index(String siteName, WebContent webContent) {
        var req = new BulkIndexHeader();
        var body = new BulkHeaderContent();
        body.index = siteName;
        body.id = WebsiteUtils.getUrlId(webContent.url);
        req.body = body;
        bulkIndexRequestBodyLine.add(Bean.toJSON(req));
        bulkIndexRequestBodyLine.add(Bean.toJSON(webContent));

        if (bulkIndexRequestBodyLine.size() / 2 > bulkSize) sendBulkRequest();
    }

    public void update(String siteName, String id, String updatedFieldAndValue) {
        var req = new BulkUpdateHeader();
        var body = new BulkHeaderContent();
        body.index = siteName;
        body.id = id;
        req.body = body;
        bulkIndexRequestBodyLine.add(Bean.toJSON(req));
        String updatePart = "{\"doc\": " + updatedFieldAndValue + "}";
        bulkIndexRequestBodyLine.add(updatePart);

        if (bulkIndexRequestBodyLine.size() / 2 > bulkSize) sendBulkRequest();
    }

    public void flushBulkRequest() {
        sendBulkRequest();
    }

    private void sendBulkRequest() {
        if (bulkIndexRequestBodyLine.isEmpty()) return;
        System.out.println("sent bulk index request");
        HTTPRequest postRequest = new HTTPRequest(HTTPMethod.POST, "http://127.0.0.1:9200/_bulk");
        String body = String.join("\n", bulkIndexRequestBodyLine) + '\n';
        postRequest.body(body, ContentType.APPLICATION_JSON);
        System.out.println(client.execute(postRequest).text());
        bulkIndexRequestBodyLine.clear();
    }
}

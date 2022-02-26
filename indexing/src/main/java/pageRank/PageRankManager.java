package pageRank;

import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.http.HTTPResponse;
import core.framework.json.Bean;
import es.IndexingManager;
import es.searchResponse.SearchResponse;

import java.util.Map;

/**
 * @author Jordan
 */
public class PageRankManager {
    private static final String REQUEST_JSON = """
            {"query":{"match_all": {}},"_source": ["url","links"],"size":1000}
            """;

    private final HTTPClient client;
    private final IndexingManager indexingManager;

    public PageRankManager() {
        Bean.register(SearchResponse.class);
        client = HTTPClient.builder().build();
        indexingManager = IndexingManager.getInstance();
    }

    public void calculateAndUpdate(String siteName) {
        HTTPRequest searchRequest = new HTTPRequest(HTTPMethod.POST, "http://127.0.0.1:9200/" + siteName + "/_search");
        searchRequest.body(REQUEST_JSON, ContentType.APPLICATION_JSON);
        HTTPResponse response = client.execute(searchRequest);

        SearchResponse searchResponse = Bean.fromJSON(SearchResponse.class, response.text());
        var pageRankCalculator = new PageRankCalculator(searchResponse, 50, 0.85, 2);
        pageRankCalculator.calculatePageRank();
        updatePageRank(siteName, pageRankCalculator.getPageRankMap());
    }

    private void updatePageRank(String site, Map<String, Double> pageRankMap) {
        for (Map.Entry<String, Double> pageRankInfo : pageRankMap.entrySet()) {
            String updatedField = "{\"pageRank\":" + pageRankInfo.getValue() + "}";
            indexingManager.update(site, pageRankInfo.getKey(), updatedField);
        }
        indexingManager.flushBulkRequest();
    }
}

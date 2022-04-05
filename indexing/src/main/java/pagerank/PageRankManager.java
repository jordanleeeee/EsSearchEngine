package pagerank;

import config.Configuration;
import core.framework.http.ContentType;
import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.http.HTTPResponse;
import core.framework.json.Bean;
import core.framework.util.Strings;
import es.IndexingManager;
import es.searchresponse.SearchResponse;

import java.util.Map;

/**
 * @author Jordan
 */
public class PageRankManager {
    private static final String REQUEST_JSON = """
            {
                "query": {
                    "bool": {
                        "filter": [
                            {
                                "term": {"shop": {"value": "{}"}}
                            }
                        ]
                    }
                },
                "_source": ["url", "links"],
                "size": 1000
            }
            """;

    private final HTTPClient client;
    private final IndexingManager indexingManager;

    public PageRankManager() {
        Bean.register(SearchResponse.class);
        client = HTTPClient.builder().build();
        indexingManager = IndexingManager.getInstance();
    }

    public void calculateAndUpdate(String siteName) {
        HTTPRequest searchRequest = new HTTPRequest(HTTPMethod.POST, Configuration.esPath + "/restaurant/_search");
        searchRequest.body(Strings.format(REQUEST_JSON, siteName), ContentType.APPLICATION_JSON);
        HTTPResponse response = client.execute(searchRequest);

        SearchResponse searchResponse = Bean.fromJSON(SearchResponse.class, response.text());
        var pageRankCalculator = new PageRankCalculator(searchResponse, 50, 0.85, 2);
        pageRankCalculator.calculatePageRank();
        updatePageRank(pageRankCalculator.getPageRankMap());
    }

    private void updatePageRank(Map<String, Double> pageRankMap) {
        for (Map.Entry<String, Double> pageRankInfo : pageRankMap.entrySet()) {
            String updatedField = "{\"pageRank\":" + pageRankInfo.getValue() + "}";
            indexingManager.update(pageRankInfo.getKey(), updatedField);
        }
        indexingManager.flushBulkRequest();
    }
}

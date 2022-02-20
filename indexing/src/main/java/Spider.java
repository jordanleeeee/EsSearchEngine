import es.IndexingManager;
import es.domain.WebContent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * @author Jordan
 */
public class Spider {
    WebSiteParser parser = new WebSiteParser();
    IndexingManager indexingService = IndexingManager.getInstance();

    private boolean isTargetedSite(String siteName, String url, String targetedSite) {
        if (siteName.equals("subway")) {
            return url.contains("www.subway.com.hk") &&
                    !url.contains("www.subway.com.hk/zh-hant") &&
                    !url.contains("www.subway.com.hk/zh-hans");
        }
        return url.startsWith(targetedSite);
    }

    public void BFS(String siteName, String url, int limit) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedSite = new HashSet<>();
        queue.add(url);

        while (!queue.isEmpty()) {
            String nextUrl = queue.poll();
            if (visitedSite.size() > limit) break;
            if (nextUrl.endsWith("/") || nextUrl.endsWith("?")) {
                nextUrl = nextUrl.substring(0, nextUrl.length() - 1);
            }
            if (visitedSite.contains(nextUrl)) continue;
            visitedSite.add(nextUrl);
            if (!isTargetedSite(siteName, nextUrl, url)) continue;
            Optional<WebContent> webContentOptional = parser.getContentOfPage(siteName, nextUrl, url);
            if (webContentOptional.isEmpty()) continue;
            WebContent webContent = webContentOptional.get();
            indexingService.index(siteName, webContent);
            queue.addAll(webContent.links);
        }
        indexingService.flushBulkRequest();
    }

    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.BFS("mcdonalds", "https://www.mcdonalds.com.hk/en", 1000);
        spider.BFS("starbucks", "https://www.starbucks.com.hk", 1000);
        spider.BFS("subway", "https://www.subway.com.hk", 1000);
    }
}
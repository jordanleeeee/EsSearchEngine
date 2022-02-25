import es.IndexingManager;
import es.domain.WebContent;
import util.SitemapManager;

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
    SitemapManager sitemapManager = SitemapManager.getInstance();

    private boolean isTargetedSite(String siteName, String url, String targetedSite) {
        if (siteName.equals("subway")) {
            return url.contains("www.subway.com.hk") &&
                    !url.contains("www.subway.com.hk/zh-hant") &&
                    !url.contains("www.subway.com.hk/zh-hans");
        }
        return url.startsWith(targetedSite);
    }

    public void bfs(String url, int limit, boolean jsRenderingEnabled) {
        String siteName = url.substring(url.indexOf("www.") + 4, url.indexOf(".com"));
        sitemapManager.loadSitemap(siteName);
        Queue<String> queue = new LinkedList<>();
        Set<String> visitedSite = new HashSet<>();
        queue.add(url);

        while (!queue.isEmpty()) {
            String nextUrl = queue.poll();
            if (visitedSite.size() > limit) break;
            nextUrl = simplifyUrl(nextUrl);
            if (visitedSite.contains(nextUrl)) continue;
            visitedSite.add(nextUrl);
            if (!isTargetedSite(siteName, nextUrl, url)) continue;
            Optional<WebContent> webContentOptional = parser.getContentOfPage(siteName, nextUrl, url, jsRenderingEnabled);
            if (webContentOptional.isEmpty()) continue;
            WebContent webContent = webContentOptional.get();
            indexingService.index(siteName, webContent);
            queue.addAll(webContent.links);
        }
        indexingService.flushBulkRequest();
        sitemapManager.clearSiteMap();
    }

    private String simplifyUrl(String nextUrl) {
        if (nextUrl.endsWith("/") || nextUrl.endsWith("?")) {
            nextUrl = nextUrl.substring(0, nextUrl.length() - 1);
        }
        nextUrl = nextUrl.replace(":443", "");
        return nextUrl;
    }
}

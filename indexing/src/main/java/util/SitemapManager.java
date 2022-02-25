package util;

import core.framework.http.HTTPClient;
import core.framework.http.HTTPMethod;
import core.framework.http.HTTPRequest;
import core.framework.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jordan
 */
public class SitemapManager {
    private static SitemapManager instance;
    private final Map<String, ZonedDateTime> sitemap;
    private final HTTPClient client;

    public static SitemapManager getInstance() {
        if (instance == null) {
            instance = new SitemapManager();
        }
        return instance;
    }

    private SitemapManager() {
        sitemap = new HashMap<>();
        client = HTTPClient.builder().build();
    }

    private void loadSitemap(String site, String xml) {
        for (Element urlElement : Jsoup.parse(xml).getElementsByTag("url")) {
            String url = urlElement.getElementsByTag("loc").text();
            String time = urlElement.getElementsByTag("lastmod").text();

            if (site.equals("starbucks")) {
                url = "https://www.starbucks.com.hk" + url;
            } else {
                url = url.substring(0, url.length() - 1);
            }
            sitemap.put(url, ZonedDateTime.parse(time).withZoneSameInstant(ZoneId.of("UTC")));
        }
    }

    public void loadSitemap(String site) {
        String sitemapPath = switch (site) {
            case "mcdonalds" -> "https://www.mcdonalds.com.hk/page-sitemap.xml";
            case "subway" -> "https://subway.com.hk/page-sitemap.xml";
            case "starbucks" -> "https://www.starbucks.com.hk/sitemap.xml";
            default -> throw new IllegalArgumentException("no sitemap path configuration for " + site);
        };

        String sitemapFile;
        String possibleCacheLocation = Strings.format("sitemap/{}.xml", site);
        Optional<String> sitemapFileOptional = FileUtils.read(possibleCacheLocation);
        if (sitemapFileOptional.isPresent()) {
            sitemapFile = sitemapFileOptional.get();
        } else {
            sitemapFile = client.execute(new HTTPRequest(HTTPMethod.GET, sitemapPath)).text();
            FileUtils.save(possibleCacheLocation, sitemapFile);
        }
        loadSitemap(site, sitemapFile);
    }

    public void clearSiteMap() {
        sitemap.clear();
    }

    @Nullable
    public ZonedDateTime getSiteModificationTime(String site) {
        return sitemap.get(site);
    }
}

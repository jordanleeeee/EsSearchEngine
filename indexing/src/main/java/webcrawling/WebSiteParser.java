package webcrawling;

import core.framework.util.Strings;
import es.domain.WebContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.FileUtils;
import util.StringUtils;
import util.WebsiteUtils;

import java.io.File;
import java.util.Optional;

public class WebSiteParser {
    private Optional<String> getHtmlDoc(String url, String possibleCacheLocation, boolean jsRenderingEnabled) {
        try {
            Optional<String> cachedHtml = FileUtils.read(possibleCacheLocation);
            if (cachedHtml.isPresent()) {
                System.out.println("get from cache");
                return cachedHtml;
            }
            return Optional.empty();    // only fetch from cache
//            String html;
//            if (jsRenderingEnabled) {
//                html = WebBrowser.getInstance().getPageContent(url);
//            } else {
//                html = Jsoup.connect(url).get().html();
//            }
//
//            if (!html.substring(0, 15).equalsIgnoreCase("<!doctype html>")) return Optional.empty();
//            return Optional.of(html);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<WebContent> getContentOfPage(String siteName, String url, String baseUrl, boolean jsRenderingEnabled) {
        System.out.println("extract content from: " + url);

        String cachePath = Strings.format("site/{}/{}", siteName, WebsiteUtils.getUrlId(url));
        return getHtmlDoc(url, cachePath, jsRenderingEnabled).flatMap(html -> {
            Document htmlDoc = Jsoup.parse(html);

            var content = new WebContent();
            content.url = url;
            content.shop = siteName;
            content.title = htmlDoc.title();
            content.size = htmlDoc.html().getBytes().length;
            content.body = WebsiteUtils.getPageBody(htmlDoc);
            content.links = WebsiteUtils.getLinkOfPage(baseUrl, htmlDoc);
            content.updatedTime = WebsiteUtils.getLastModificationTime(url, htmlDoc);
            content.tags.addAll(StringUtils.getTags(content.title));
            content.tags.addAll(StringUtils.getTags(content.body.h1));
            content.tags.addAll(StringUtils.getTags(content.body.h2));

            saveHtmlToCacheIfNeeded(cachePath, html);
            return Optional.of(content);
        });
    }

    public void saveHtmlToCacheIfNeeded(String cachePath, String html) {
        if (!new File(cachePath).exists()) {
            FileUtils.save(cachePath, html);
        }
    }
}

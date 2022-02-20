import core.framework.crypto.Hash;
import core.framework.util.Strings;
import es.domain.WebContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import util.StringUtils;
import util.WebBrowser;
import util.WebSiteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class WebSiteParser {
    private Optional<String> getHtmlDoc(String url, String possibleCacheLocation, boolean jsRenderingEnabled) {
        try {
            File file = new File(possibleCacheLocation);
            String html;
            if (file.exists()) {
                System.out.println("get from cache");
                return Optional.of(new String(new FileInputStream(file).readAllBytes()));
            }
//            return Optional.empty();    // only fetch from cache
            if (jsRenderingEnabled) {
                html = WebBrowser.getInstance().getPageContent(url);
            } else {
                html = Jsoup.connect(url).get().html();
            }

            if (!html.substring(0, 15).equalsIgnoreCase("<!doctype html>")) return Optional.empty();
            return Optional.of(html);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<WebContent> getContentOfPage(String siteName, String url, String baseUrl, boolean jsRenderingEnabled) {
        System.out.println("extract content from: " + url);

        String cachePath = Strings.format("site/{}/{}", siteName, Hash.sha1Hex(url).substring(20));
        return getHtmlDoc(url, cachePath, jsRenderingEnabled).flatMap(html -> {
            Document htmlDoc = Jsoup.parse(html);

            WebContent content = new WebContent();
            content.url = url;
            content.title = htmlDoc.title();
            content.size = htmlDoc.body().text().getBytes().length;
            content.body = WebSiteUtils.getPageBody(htmlDoc);
            content.links = WebSiteUtils.getLinkOfPage(baseUrl, htmlDoc);
            content.tags.addAll(StringUtils.getTags(content.title));
            content.tags.addAll(StringUtils.getTags(content.body.h1));
            content.tags.addAll(StringUtils.getTags(content.body.h2));

            saveHtmlToCacheIfNeeded(cachePath, html);
            return Optional.of(content);
        });
    }

    private void saveHtmlToCacheIfNeeded(String cachePath, String html) {
        if (!new File(cachePath).exists()) {
            try (FileWriter fileWriter = new FileWriter(cachePath)) {
                fileWriter.write(html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

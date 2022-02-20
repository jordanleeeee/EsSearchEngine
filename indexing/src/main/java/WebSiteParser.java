import core.framework.crypto.Hash;
import core.framework.util.Strings;
import es.domain.WebContent;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebSiteParser {

    public List<String> getLinkOfPage(String baseUrl, Document htmlDoc) {
        List<String> result = new ArrayList<>();
        for (Element linkElement : htmlDoc.select("a")) {
            String hrefValue = linkElement.attr("href");
            if (hrefValue.startsWith("http")) {
                result.add(hrefValue);
            } else if (hrefValue.startsWith("/")) {
                result.add(baseUrl + hrefValue);
            }
        }
        return result;
    }

    private Optional<String> getHtmlDoc(String siteName, String url, String possibleCacheLocation) {
        try {
            Connection web = Jsoup.connect(url);
            File file = new File(possibleCacheLocation);
            String html;
            if (file.exists()) {
                System.out.println("get from cache");
                html = new String(new FileInputStream(file).readAllBytes());
            } else {
                return Optional.empty();    // only fetch from cache
//                html = web.get().html();
            }
            if (!html.substring(0, 15).equalsIgnoreCase("<!doctype html>")) return Optional.empty();
            return Optional.of(html);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<WebContent> getContentOfPage(String siteName, String url, String baseUrl) {
        System.out.println("extract content from: " + url);

        String cachePath = Strings.format("site/{}/{}", siteName, Hash.sha1Hex(url).substring(20));
        return getHtmlDoc(siteName, url, cachePath).flatMap(html -> {
            Document htmlDoc = Jsoup.parse(html);

            WebContent content = new WebContent();
            content.url = url;
            content.size = htmlDoc.body().text().getBytes().length;
            content.links = getLinkOfPage(baseUrl, htmlDoc);
            content.title = htmlDoc.title();
            WebContent.Body body = new WebContent.Body();
            for (Element h1 : htmlDoc.select("h1")) {
                body.h1.add(h1.text());
                h1.remove();
            }
            for (Element h2 : htmlDoc.select("h2")) {
                body.h2.add(h2.text());
                h2.remove();
            }
            body.content = htmlDoc.body().text();
            content.body = body;
            content.tags.addAll(StringUtils.getTag(content.title));
            content.tags.addAll(StringUtils.getTag(content.body.h1));
            content.tags.addAll(StringUtils.getTag(content.body.h2));

            saveHtmlToCache(cachePath, html);
            return Optional.of(content);
        });
    }

    private void saveHtmlToCache(String cachePath, String html) {
        if (!new File(cachePath).exists()) {
            try (FileWriter fileWriter = new FileWriter(cachePath)) {
                fileWriter.write(html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        WebSiteParser webSiteParser = new WebSiteParser();
        System.out.println(webSiteParser.getLinkOfPage("https://www.starbucks.com.hk", Jsoup.parse(Jsoup.connect("https://www.starbucks.com.hk").get().html())));
    }

}

package util;

import es.domain.WebContent;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.annotation.Nullable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jordan
 */
public class WebsiteUtils {
    public static List<String> getLinkOfPage(String baseUrl, Document htmlDoc) {
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

    public static WebContent.Body getPageBody(Document htmlDoc) {
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
        return body;
    }

    @Nullable
    public static ZonedDateTime getLastModificationTime(String url, Document htmlDoc){
        ZonedDateTime time = SitemapManager.getInstance().getSiteModificationTime(url);
        if (time == null) {
            for (Element meta : htmlDoc.getElementsByTag("meta")) {
                if (meta.attr("property").equals("article:modified_time")) {
                    String timeString = meta.attr("content");
                    time = ZonedDateTime.parse(timeString).withZoneSameInstant(ZoneId.of("UTC"));
                }
            }
        }
        return time;
    }
}

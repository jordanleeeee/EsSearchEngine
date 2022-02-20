import core.framework.http.HTTPClient;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Jordan
 */
public class Any {
    private static HTTPClient client = HTTPClient.builder().build();

    public static void main(String[] args) throws Exception {
//        String url = "https://www.starbucks.com.hk/sitemap.xml";
//        String url = "https://www.mcdonalds.com.hk/page-sitemap.xml";
//        String url = "https://subway.com.hk/page-sitemap.xml";
//        FileUtils.save("sitemap/starbucks.xml", client.execute(new HTTPRequest(HTTPMethod.GET, url)).text());

//        String date = "2020-12-23T10:46:41+00:00";
        String date = "2017-07-11T08:32:08+00:00";
        System.out.println(ZonedDateTime.parse(date).withZoneSameInstant(ZoneId.of("UTC")));
    }
}

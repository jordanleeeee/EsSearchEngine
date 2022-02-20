package util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import java.io.IOException;

/**
 * @author Jordan
 */
public class WebBrowser {
    private static WebBrowser webBrowser;
    private final Playwright playwright;
    private final Page page;

    public static WebBrowser getInstance() {
        if (webBrowser == null) {
            webBrowser = new WebBrowser();
        }
        return webBrowser;
    }

    private WebBrowser() {
        playwright = Playwright.create();
        page = playwright.chromium().launch().newPage();
    }

    public void closeBrowser() {
        playwright.close();
    }

    public String getPageContent(String url) throws IOException {
        try {
            Response response = page.navigate(url);
            if (response.status() >= 400) throw new IOException("invalid status code");
            return page.content();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

}

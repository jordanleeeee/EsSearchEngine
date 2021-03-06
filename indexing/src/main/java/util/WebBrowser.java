package util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import java.io.IOException;

/**
 * @author Jordan
 */
public final class WebBrowser {
    private static WebBrowser webBrowser;
    public static WebBrowser getInstance() {
        if (webBrowser == null) {
            webBrowser = new WebBrowser();
        }
        return webBrowser;
    }

    private final Playwright playwright;
    private final Page page;

    private WebBrowser() {
        playwright = Playwright.create();
        page = playwright.chromium().launch().newPage();
    }

    public static void closeBrowser() {
        if (webBrowser != null) {
            webBrowser.playwright.close();
        }
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

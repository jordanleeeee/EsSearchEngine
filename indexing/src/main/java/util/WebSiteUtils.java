package util;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;

import java.util.Optional;

/**
 * @author Jordan
 */
public class WebSiteUtils {
    public static Optional<String> getPageContent(String url) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            Response response = page.navigate(url);
            if (response.status() >= 400) return Optional.empty();
            return Optional.of(page.content());
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

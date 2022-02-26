package webcrawling;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * @author Jordan
 */
public class HeadlessBrowserDemo {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
//            Browser browser = playwright.chromium().launch();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500));
            Page page = browser.newPage();
//            System.out.println(page.title());
            page.navigate("https://www.ubgame.dev/");
            page.querySelector("span:has-text(\"登录账号\")").click();
            page.fill(":nth-match(.ant-input, 1)", "jordantest");
            page.fill(":nth-match(.ant-input, 2)", "helloworld1");
            page.querySelector("button:has-text(\"登录\")").click();
            System.out.println(page.content());     // js rendered content
        }
    }
}

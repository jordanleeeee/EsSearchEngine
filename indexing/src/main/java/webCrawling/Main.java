package webCrawling;

import util.WebBrowser;

/**
 * @author Jordan
 */
public class Main {
    public static void main(String[] args) {
        var spider = new Spider();

        spider.bfs("https://www.mcdonalds.com.hk/en", 1000, false);
        spider.bfs("https://www.starbucks.com.hk", 1000, false);
        spider.bfs("https://www.subway.com.hk", 1000, false);
        WebBrowser.getInstance().closeBrowser();
    }
}

package pageRank;

/**
 * @author Jordan
 */
public class Main {
    public static void main(String[] args) {
        var pageRankManager = new PageRankManager();

        pageRankManager.calculateAndUpdate("mcdonalds");
        pageRankManager.calculateAndUpdate("starbucks");
        pageRankManager.calculateAndUpdate("subway");
    }
}

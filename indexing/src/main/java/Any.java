/**
 * @author Jordan
 */
public class Any {
    public static void main(String[] args) throws Exception {
        String url = "https://www.mcdonalds.com.hk/en";
        System.out.println(url.substring(url.indexOf("www.") + 4, url.indexOf(".com")));
    }
}

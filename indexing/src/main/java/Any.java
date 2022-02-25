import core.framework.http.HTTPClient;

/**
 * @author Jordan
 */
public class Any {
    private static HTTPClient client = HTTPClient.builder().build();

    public static void main(String[] args) throws Exception {
        System.out.println("hello world");
    }
}

import java.io.FileInputStream;

/**
 * @author Jordan
 */
public class Any {
    public static void main(String[] args) throws Exception {
        System.out.println(new String(new FileInputStream("site/mcdonalds/0b6d984d181515f4b193").readAllBytes()));
    }
}

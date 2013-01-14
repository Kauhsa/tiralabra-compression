package kauhsa;

import java.io.IOException;
import kauhsa.compression.lzw.LZW;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) throws IOException {
        byte[] bytes = "hoehoehoehoehoi".getBytes();
        byte[] encoded = LZW.encode(bytes);
        byte[] decoded = LZW.decode(encoded);
        System.out.println(new String(decoded));
    }
}

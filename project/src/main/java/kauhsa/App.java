package kauhsa;

import java.io.IOException;
import java.io.InputStream;
import kauhsa.compression.lzw.LZW;

/**
 * Hello world!
 *
 */
public class App {
    public static final String FILENAME = "/randomfile";

    public static void main(String[] args) throws IOException {
        // sighface
        InputStream is = App.class.getResourceAsStream(FILENAME);
        int size = 0;
        while (is.read() != -1) {
            size++;
        }
        
        byte[] bytes = new byte[size];
        App.class.getResourceAsStream(FILENAME).read(bytes);

        byte[] encoded = LZW.encode(bytes);
        byte[] decoded = LZW.decode(encoded);
        //System.out.println(new String(decoded));
    }
}

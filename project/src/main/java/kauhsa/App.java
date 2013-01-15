package kauhsa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kauhsa.compression.lzw.LZW;

/**
 * Hello world!
 *
 */
public class App {
    public static final String FILENAME = "/seitseman_veljesta.txt";

    public static void main(String[] args) throws IOException {
        InputStream is = App.class.getResourceAsStream(FILENAME);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZW.encode(is, out);
        byte[] decoded = LZW.decode(out.toByteArray());
        System.out.println(new String(decoded));
        //System.out.write(out.toByteArray());
    }
}

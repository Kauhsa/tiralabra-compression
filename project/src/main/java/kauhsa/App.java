package kauhsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import kauhsa.compression.lzw.LZWEncode;
import kauhsa.compression.lzw.LZWDecode;

/**
 * Hello world!
 *
 */
public class App {
    public static final String FILENAME = "/seitseman_veljesta.txt";

    public static void main(String[] args) throws IOException {
        /* InputStream is = App.class.getResourceAsStream(FILENAME);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZWEncode.encode(is, out);
        byte[] decoded = LZWDecode.decode(out.toByteArray());  
        System.out.write(decoded); */
    }
}

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
        /*InputStream in = App.class.getResourceAsStream(FILENAME);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZW.encode(in, out);
        //byte[] decoded = LZWDecode.decode(out.toByteArray());  
        //System.out.write(decoded);*/
    }
}

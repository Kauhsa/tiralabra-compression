package kauhsa.compression.lzw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LZWTest {

    private void testEncodingAndDecoding(byte[] data) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(Arrays.copyOf(data, data.length));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LZWEncode.encode(in, out);
        assertArrayEquals(data, LZWDecode.decode(out.toByteArray()));
    }

    private byte[] loadResourceToByteArray(String s) throws IOException {
        InputStream res = LZWTest.class.getResourceAsStream(s);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        while (true) {
            int b = res.read();
            if (b == -1) {
                break;
            }
            out.write(b);
        }
        
        return out.toByteArray();
    }
    
    private byte[] getRandomData(int length) {
        Random random = new Random();
        byte[] data = new byte[length];        
        random.nextBytes(data);
        return data;
    }
    
    @Test
    public void testEmpty() throws IOException {
        testEncodingAndDecoding(new byte[0]);
    }
    
    @Test
    public void testSimple() throws IOException {
        testEncodingAndDecoding("hå hå, fiske!".getBytes());        
        testEncodingAndDecoding("ainahan voit pelata pokeria taikka kenoa".getBytes());
    }
    
    @Test
    public void testRandomData() throws IOException {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int len = random.nextInt(1000) + 100;
            testEncodingAndDecoding(getRandomData(len));
        }
    }
    
    @Test
    public void testBook() throws IOException {
        byte[] data = loadResourceToByteArray("seitseman_veljesta.txt");
        testEncodingAndDecoding(data);
    }
    
    @Test
    public void testBookCompressedSize() throws IOException {
        byte[] data = loadResourceToByteArray("seitseman_veljesta.txt");
        int originalLength = data.length;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        LZWEncode.encode(in, out);
        int compressedLength = out.toByteArray().length;
        
        assertTrue((float) compressedLength / originalLength < 0.5);
    }
}

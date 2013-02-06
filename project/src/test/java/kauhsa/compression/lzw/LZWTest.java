package kauhsa.compression.lzw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Test;

public class LZWTest {

    private void testEncodingAndDecoding(byte[] data) throws IOException {
        ByteArrayInputStream originalIn = new ByteArrayInputStream(data);
        ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
        LZW.encode(originalIn, encodedOut);

        ByteArrayInputStream encodedIn = new ByteArrayInputStream(encodedOut.toByteArray());
        ByteArrayOutputStream originalOut = new ByteArrayOutputStream();
        LZW.decode(encodedIn, originalOut);

        assertArrayEquals(data, originalOut.toByteArray());
    }

    private void testChunkEncodingAndDecoding(byte[] data, int dictSize) throws IOException {
        ByteArrayInputStream originalIn = new ByteArrayInputStream(data);
        ByteArrayOutputStream encodedOut = new ByteArrayOutputStream();
        LZW.encodeInChunks(originalIn, encodedOut, dictSize);

        ByteArrayInputStream encodedIn = new ByteArrayInputStream(encodedOut.toByteArray());
        ByteArrayOutputStream originalOut = new ByteArrayOutputStream();
        LZW.decodeChunks(encodedIn, originalOut);

        assertArrayEquals(data, originalOut.toByteArray());
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
        testEncodingAndDecoding("They will all be mine in the end, for I am the Queen of Blades".getBytes());
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
    public void testRandomDataInChunks() throws IOException {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int len = random.nextInt(1000) + 10000;
            testChunkEncodingAndDecoding(getRandomData(len), 1000);
        }
    }
    
    @Test
    public void test256BytesOfRandomData() throws IOException {
        for (int i = 0; i < 100; i++) {
            testEncodingAndDecoding(getRandomData(256));
        }
    }

    @Test
    public void testBook() throws IOException {
        byte[] data = loadResourceToByteArray("seitseman_veljesta.txt");
        testEncodingAndDecoding(data);
    }

    @Test
    public void testBookInChunks() throws IOException {
        byte[] data = loadResourceToByteArray("seitseman_veljesta.txt");
        testChunkEncodingAndDecoding(data, 10000);
    }
    
    @Test
    public void testBookCompressedSize() throws IOException {
        byte[] data = loadResourceToByteArray("seitseman_veljesta.txt");
        float originalLength = data.length;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        LZW.encode(in, out);
        float compressedLength = out.toByteArray().length;

        assertTrue(compressedLength / originalLength < 0.5);
        assertTrue(compressedLength / originalLength > 0.1);
    }
}

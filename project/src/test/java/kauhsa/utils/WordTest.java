package kauhsa.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class WordTest {

    @Test
    public void createWordFromByteArrayTest() {
        byte[] byteArray = "HEHEZ".getBytes();
        Word word = new Word(byteArray);
        assertArrayEquals(byteArray, word.getData());
    }

    @Test
    public void createWordFromSingleByteTest() {
        byte b = 122;
        Word word = new Word(b);
        assertArrayEquals(new byte[]{b}, word.getData());
    }

    @Test
    public void appendTest() {
        Word word = new Word((byte) 1);
        assertArrayEquals(new byte[]{1}, word.getData());

        word = word.append((byte) 2);
        assertArrayEquals(new byte[]{1, 2}, word.getData());

        word = word.append((byte) 3);
        assertArrayEquals(new byte[]{1, 2, 3}, word.getData());
    }
}

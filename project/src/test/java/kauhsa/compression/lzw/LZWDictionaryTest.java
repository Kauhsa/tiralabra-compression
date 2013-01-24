package kauhsa.compression.lzw;

import kauhsa.utils.word.Word;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mika
 */
public class LZWDictionaryTest {
    private LZWDictionary decodeDict;
    private LZWDictionary encodeDict;
           
    @Before
    public void setUp() {
        this.encodeDict = new LZWDictionary(LZWDictionaryType.ENCODE);
        this.decodeDict = new LZWDictionary(LZWDictionaryType.DECODE);
    }

    @Test
    public void testInitalizedCorrectly() {
        assertEquals(9, this.encodeDict.getCurrentBitSize());
        assertEquals(9, this.decodeDict.getCurrentBitSize());
        
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            assertTrue(this.encodeDict.containsWord(new Word((byte) i)));
        }
        assertTrue(this.encodeDict.containsWord(LZWDictionary.EOF_WORD));
    }
    
    @Test
    public void containsWordWhenAdded() {
        Word word = new Word("HAHA".getBytes());
        assertFalse(this.encodeDict.containsWord(word));      
        this.encodeDict.addWord(word);        
        assertTrue(this.encodeDict.containsWord(word));
        assertNotNull(this.encodeDict.getCode(word));
    }
    
    @Test
    public void sizeIncrementsCorrectly() {
        final int INITIAL_SIZE = 257;
        for (long i = INITIAL_SIZE; i < 1000; i++) {       
            int currentBitSize = encodeDict.getCurrentBitSize();
            assertTrue((1L << currentBitSize) >= i);
            assertTrue((1L << currentBitSize - 1) < i);                 
            encodeDict.incrementSizeCounter();
        }
    }

}

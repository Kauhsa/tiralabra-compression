package kauhsa.compression.lzw;

import java.util.HashMap;

/**
 *
 * @author mcviinam
 */
public class LZWDictionary {
    private HashMap<byte[], Long> dictionary;
    private int currentBitSize;
    private long nextValue;
    
    public LZWDictionary() {
        // fill dictionary with all one-byte arrays
        dictionary = new HashMap<byte[], Long>();
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            byte[] oneByteArray = {(byte) i};
            dictionary.put(oneByteArray, (long) i);
        }
        
        // initialized dictionary fits with 8-bit size
        currentBitSize = 8;
        nextValue = Byte.MAX_VALUE + 1;        
    }
    
    private void updateCurrentBitSize() {
        // 2 ** currentBitSize
        int maxSizeWithCurrentBitSize = 1 << currentBitSize;
        if (maxSizeWithCurrentBitSize < dictionary.size()) {
            currentBitSize++;
        }
    }
    
    public boolean contains(byte[] word) {
        return dictionary.containsKey(word);
    }
    
    public void add(byte[] word) {
        assert (!contains(word));
        dictionary.put(word, nextValue);
        nextValue++;
        updateCurrentBitSize();
    }
    
    public LZWEntry get(byte[] word) {
        LZWEntry entry = new LZWEntry(dictionary.get(word), currentBitSize);
        return entry;
    }
    
}

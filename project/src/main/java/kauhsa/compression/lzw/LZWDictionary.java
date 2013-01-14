package kauhsa.compression.lzw;

import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.word.Word;
import java.util.HashMap;

/**
 *
 * @author mcviinam
 */
public class LZWDictionary {

    private HashMap<Word, Long> dictionary;
    private int currentBitSize;
    private long nextValue;

    public LZWDictionary() {
        nextValue = 0;
        currentBitSize = 1;

        /* 
         * Fill dictionary with all single-byte words. Use short for iteration
         * because byte would overflow and there would be an infinite loop.
         */
        dictionary = new HashMap<Word, Long>();
        for (short i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            add(new Word((byte) i));
        }
    }

    private void updateCurrentBitSize() {
        // 2 ** currentBitSize
        int maxSizeWithCurrentBitSize = 1 << currentBitSize;
        if (dictionary.size() > maxSizeWithCurrentBitSize) {
            currentBitSize++;
        }
    }

    public boolean contains(Word word) {
        return dictionary.containsKey(word);
    }

    public void add(Word word) {
        dictionary.put(word, nextValue);
        nextValue++;
        updateCurrentBitSize();
    }

    public BitGroup get(Word word) {
        BitGroup entry = new BitGroup(dictionary.get(word), currentBitSize);
        return entry;
    }
}

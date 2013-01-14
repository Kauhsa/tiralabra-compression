package kauhsa.compression.lzw;

import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.word.Word;
import java.util.HashMap;

/**
 *
 * @author mcviinam
 */
public class LZWDictionary {
    public static final Word EOF_WORD = new Word(null);
    
    private HashMap<Word, Long> wordLongDict;
    private HashMap<Long, Word> longWordDict;
    private int currentBitSize;
    private long nextValue;

    public LZWDictionary() {
        nextValue = 0;
        currentBitSize = 1;

        /* 
         * Fill dictionary with all single-byte words. Use short for iteration
         * because byte would overflow and there would be an infinite loop.
         */
        wordLongDict = new HashMap<Word, Long>();
        longWordDict = new HashMap<Long, Word>();
        for (short i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            add(new Word((byte) i));
        }
        
        // Add EOF word to dictionary too
        add(EOF_WORD);
    }

    private void updateCurrentBitSize() {
        // 2 ** currentBitSize
        int maxSizeWithCurrentBitSize = 1 << currentBitSize;
        if (wordLongDict.size() > maxSizeWithCurrentBitSize) {
            currentBitSize++;
        }
    }

    public boolean containsWord(Word word) {
        return wordLongDict.containsKey(word);
    }
    
    public boolean containsCode(BitGroup code) {
        return longWordDict.containsKey(code.getData());
    }

    public void add(Word word) {
        wordLongDict.put(word, nextValue);
        longWordDict.put(nextValue, word);
        nextValue++;
        updateCurrentBitSize();
    }

    public BitGroup getCode(Word word) {
        return new BitGroup(wordLongDict.get(word), currentBitSize);
    }
    
    public Word getWord(BitGroup bitGroup) {
        return longWordDict.get(bitGroup.getData());        
    }

    public int getCurrentBitSize() {
        return currentBitSize;
    }
    
}

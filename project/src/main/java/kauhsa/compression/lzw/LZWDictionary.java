package kauhsa.compression.lzw;

import java.util.HashMap;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.word.Word;

/**
 * Dictionary implementation that is in the core of LZW algorithm.
 *
 * TODO: Split this dictionary to encoding and decoding version or add parameter
 * to constructor or something
 */
public class LZWDictionary {

    public static final Word EOF_WORD = new Word(null);
    private HashMap<Word, Long> wordLongDict;
    private HashMap<Long, Word> longWordDict;
    private int currentBitSize;
    private long nextValue;
    private int sizeOffset;

    /**
     * Create new a LZWDictionary. This also fills the dictionary with default
     * values - all single bytes 0-255 and EOF word.
     */
    public LZWDictionary() {
        nextValue = 0;
        currentBitSize = 1;
        initalizeDictionary();
    }

    /**
     * Fill dictionary with single bytes 0-255 and EOF word.
     */
    private void initalizeDictionary() {
        // Use short for iteration because byte would overflow and would cause 
        // an an infinite loop. 
        wordLongDict = new HashMap<>();
        longWordDict = new HashMap<>();
        for (short i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            addWord(new Word((byte) i));
        }

        addWord(EOF_WORD);
    }

    /**
     * If all codes in dictionary can not be represented with current bit size,
     * increase bit size.
     */
    private void updateCurrentBitSize() {
        int maxSizeWithCurrentBitSize = 1 << currentBitSize;
        if (getDictionarySize() > maxSizeWithCurrentBitSize) {
            currentBitSize++;
        }
    }

    public boolean containsWord(Word word) {
        return wordLongDict.containsKey(word);
    }

    public boolean containsCode(BitGroup code) {
        return longWordDict.containsKey(code.getData());
    }

    /**
     * Add new Word to dictionary. Will increase bit size of all future codes
     * returned by getCode if necessary.
     *
     * @param word
     */
    public void addWord(Word word) {
        wordLongDict.put(word, nextValue);
        longWordDict.put(nextValue, word);
        nextValue++;
        updateCurrentBitSize();
    }

    /**
     * Return code for the current word. Bit size of the BitGroup depends on how
     * many words there are currently on dictionary.
     *
     * @param word
     * @return
     */
    public BitGroup getCode(Word word) {
        return new BitGroup(wordLongDict.get(word), currentBitSize);
    }

    public Word getWord(BitGroup bitGroup) {
        return longWordDict.get(bitGroup.getData());
    }

    public int getCurrentBitSize() {
        return currentBitSize;
    }

    public int getDictionarySize() {
        return wordLongDict.size() + sizeOffset;
    }

    /**
     * TODO: Part of ugly hack, get rid of this eventually.
     */
    public void incrementSizeOffset() {
        sizeOffset++;
    }
}

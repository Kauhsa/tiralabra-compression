package kauhsa.compression.lzw;

import kauhsa.utils.Word;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.hashmap.KauhsaHashMap;

/**
 * Dictionary implementation that is in the core of LZW algorithm.
 */
public class LZWDictionary {

    public static final Word EOF_WORD = new Word(null);
    private KauhsaHashMap<Word, Long> wordLongDict;
    private KauhsaHashMap<Long, Word> longWordDict;
    private int currentBitSize;
    private long nextValue;
    private int size;
    private final LZWDictionaryType type;

    /**
     * Create new a LZWDictionary. This also fills the dictionary with default
     * values - all single bytes 0-255 and EOF word.
     *
     * @param type Type of LZW Dictionary - use ENCODE if this dictionary is
     * used for encoding, DECODE for decoding
     */
    public LZWDictionary(LZWDictionaryType type) {
        nextValue = 0;
        currentBitSize = 1;
        this.type = type;
        initalizeDictionary();
    }

    /**
     * Fill dictionary with single bytes 0-255 and EOF word.
     */
    private void initalizeDictionary() {
        wordLongDict = new KauhsaHashMap<>();
        longWordDict = new KauhsaHashMap<>();
        for (short i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            addWord(new Word((byte) i));
            incrementSizeCounter();
        }

        addWord(EOF_WORD);
        incrementSizeCounter();
    }

    /**
     * Tick the size counter of dictionary by one - if the size goes over
     * certain boundary, will affect getCurrentBitSize and getCode.
     */
    public void incrementSizeCounter() {
        size++;
        int maxSizeWithCurrentBitSize = 1 << currentBitSize;
        if (size > maxSizeWithCurrentBitSize) {
            currentBitSize++;
        }
    }

    /**
     * Check if this dictionary contains a code for specific word.
     *
     * @param word word to check
     * @return true if contains, otherwise false
     * @throws IllegalStateException if dictionary is not encoding dictionary
     */
    public boolean containsWord(Word word) throws IllegalStateException {
        if (type != LZWDictionaryType.ENCODE) {
            throw new IllegalStateException("Can not be used in decoding dictionary");
        }

        return wordLongDict.containsKey(word);
    }

    /**
     * Check if this dictionary contains a word for specific code.
     *
     * @param code code to check
     * @return true if contains, otherwise false
     * @throws IllegalStateException if dictionary is not decoding dictionary
     */
    public boolean containsCode(BitGroup code) throws IllegalStateException {
        if (type != LZWDictionaryType.DECODE) {
            throw new IllegalStateException("Can not be used in encoding dictionary");
        }

        return longWordDict.containsKey(code.getData());
    }

    /**
     * Add new Word to dictionary.
     *
     * @param word Word to be added
     */
    public void addWord(Word word) {
        if (type == LZWDictionaryType.ENCODE) {
            wordLongDict.put(word, nextValue);
        } else {
            longWordDict.put(nextValue, word);
        }

        nextValue++;
    }

    /**
     * Return code for specific word. Bit size of returned BitGroup depends on
     * how many times incrementSizeCounter() is called.
     *
     * @param word matching word for returned code
     * @return code for Word given as parameter
     * @throws IllegalStateException if dictionary is not encoding dictionary
     */
    public BitGroup getCode(Word word) throws IllegalStateException {
        if (type != LZWDictionaryType.ENCODE) {
            throw new IllegalStateException("Can not be used in decoding dictionary");
        }

        return new BitGroup(wordLongDict.get(word), currentBitSize);
    }

    /**
     * Return word for specific code.
     *
     * @param code matching code for returned word
     * @return Word for code given as parameter
     * @throws IllegalStateException if dictionary is not decoding dictionary
     */
    public Word getWord(BitGroup code) throws IllegalStateException {
        if (type != LZWDictionaryType.DECODE) {
            throw new IllegalStateException("Can not be used in encoding dictionary");
        }

        return longWordDict.get(code.getData());
    }

    /**
     * Get current bit size of dictionary. Depends on how many times incrementSizeCounter() is
     * called.
     *
     * @return bitSize of BitGroups returned by getCode()
     */
    public int getCurrentBitSize() {
        return currentBitSize;
    }
    
    /**
     * Get size counter.
     * 
     * @return size counter.
     */
    public int getSizeCounter() {
        return size;
    }
}

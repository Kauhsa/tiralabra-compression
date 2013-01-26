package kauhsa.compression.lzw;

import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.hashmap.KauhsaHashMap;
import kauhsa.utils.word.Word;

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

    public boolean containsWord(Word word) {
        if (type != LZWDictionaryType.ENCODE) {
            throw new IllegalStateException("Can not be used in decoding dictionary");
        }

        return wordLongDict.containsKey(word);
    }

    public boolean containsCode(BitGroup code) {
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
     * Return code for the current word. Bit size of the BitGroup depends on how
     * many words there are currently on dictionary.
     *
     * @param word word whose code will be returned
     * @return code for Word given as parameter
     */
    public BitGroup getCode(Word word) {
        if (type != LZWDictionaryType.ENCODE) {
            throw new IllegalStateException("Can not be used in decoding dictionary");
        }

        return new BitGroup(wordLongDict.get(word), currentBitSize);
    }

    public Word getWord(BitGroup bitGroup) {
        if (type != LZWDictionaryType.DECODE) {
            throw new IllegalStateException("Can not be used in encoding dictionary");
        }

        return longWordDict.get(bitGroup.getData());
    }

    public int getCurrentBitSize() {
        return currentBitSize;
    }
}

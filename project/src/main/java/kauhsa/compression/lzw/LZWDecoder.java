package kauhsa.compression.lzw;

import java.io.IOException;
import java.io.OutputStream;
import kauhsa.utils.Word;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.bitgroup.BitGroupInputStream;

/**
 * Class that implements LZW decoding.
 */
public class LZWDecoder {

    private final BitGroupInputStream in;
    private final OutputStream out;
    private LZWDictionary decodeDict;
    private Word currentWord;
    private Word previousWord;
    private byte lastCharacter;

    /**
     * Create a new LZWDecoder instance.
     *
     * @param in where the codes are read from
     * @param out where the decoded data is written to
     */
    public LZWDecoder(BitGroupInputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    /**
     * Decode LZW data.
     *
     * @throws IOException if BitGroupInputStream or OutputStream throw an
     * IOException
     */
    @SuppressWarnings("empty-statement")
    public void decode() throws IOException {
        resetState();
        while (decodeSingleCode());
    }

    /**
     * Reset internal variables of decoder.
     */
    private void resetState() {
        decodeDict = new LZWDictionary(LZWDictionaryType.DECODE);
        currentWord = null;
        previousWord = null;
        lastCharacter = 0;
    }

    /**
     * Decode a single code from BitGroupInputStream.
     *
     * @throws IOException if BitGroupInputStream or OutputStream throw an
     * IOException
     */
    private boolean decodeSingleCode() throws IOException {
        decodeDict.incrementSizeCounter();

        if (!readCurrentWord()) {
            return false;
        }

        deduceLastCharacter();
        addNewWordToDictionary();
        out.write(currentWord.getData());
        previousWord = currentWord;
        return true;
    }

    /**
     * Read the word of next code in BitGroupInputStream.
     *
     * @throws IOException if BitGroupInputStream throws an IOException
     * @throws IllegalStateException if unable to read BitGroup or necessary
     * length
     * @return false if encoding should be stopped - as in, we've reached the
     * end already
     */
    private boolean readCurrentWord() throws IOException, IllegalStateException {
        BitGroup currentCode = in.read(decodeDict.getCurrentBitSize());
        currentWord = decodeDict.getWord(currentCode);

        if (currentWord != null && currentWord.equals(LZWDictionary.EOF_WORD)) {
            return false;
        }

        return true;
    }

    /**
     * Deduce the last character of word that should be added to dictionary.
     * Normally it is the first character of current word, but in special case
     * where the current word is not found from dictionary, it is the last
     * character of word in previous encoding round.
     */
    private void deduceLastCharacter() {
        if (currentWord != null) {
            lastCharacter = currentWord.getData()[0];
        } else {
            lastCharacter = previousWord.getData()[0];
            currentWord = previousWord.append(lastCharacter);
        }
    }

    /**
     * Add new word to decoding dictionary. It is composed from previously
     * deduced last character and previous word.
     */
    private void addNewWordToDictionary() {
        if (previousWord != null) {
            Word newWord = previousWord.append(lastCharacter);
            decodeDict.addWord(newWord);
        }
    }
}

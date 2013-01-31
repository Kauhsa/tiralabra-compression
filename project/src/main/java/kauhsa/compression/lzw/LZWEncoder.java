package kauhsa.compression.lzw;

import java.io.IOException;
import java.io.InputStream;
import kauhsa.utils.bitgroup.BitGroupOutputStream;
import kauhsa.utils.word.Word;

/**
 * Class that implements LZW encoding.
 */
public class LZWEncoder {

    private final InputStream in;
    private final BitGroupOutputStream out;
    private LZWDictionary dict;
    private Word previousWord;
    private Word currentWord;
    private byte currentByte;
    private int maxDictionarySize;
    private boolean inputDepleted;
    
    /**
     * Create new LZWEncoder instance.
     *
     * @param in input data
     * @param out decoded data
     */
    public LZWEncoder(InputStream in, BitGroupOutputStream out) {
        this.in = in;
        this.out = out;
        inputDepleted = false;
    }

    /**
     * Encode data using LZW algorithm.
     *
     * @throws IOException if in or out throws IOException
     */
    public void encode() throws IOException {
        encodeUntilDictionarySize(-1);
    }

    /**
     * Encode data using LZW algorithm until dictionary size is larger than
     * maxSize or InputStream is depleted.
     *
     * @param maxSize maximum size of dictionary. -1 disables the limit.
     * @throws IOException if in or out throws IOException
     */
    @SuppressWarnings("empty-statement")
    public void encodeUntilDictionarySize(int maxSize) throws IOException {
        this.maxDictionarySize = maxSize;
        resetState();
        while (encodeSingleByte());
        finalizeEncoding();
    }

    /**
     * Reset internal variables of encoder.
     */
    private void resetState() {
        dict = new LZWDictionary(LZWDictionaryType.ENCODE);
        previousWord = null;
        currentWord = null;
        currentByte = 0;
    }

    /**
     * Encode a single byte from InputStream.
     *
     * @return false if encoding should stop, otherwise true
     * @throws IOException
     */
    private boolean encodeSingleByte() throws IOException {
        if (maxDictionarySize != -1 && dict.getSizeCounter() > maxDictionarySize) {
            return false;
        }

        if (!readNextByte()) {
            inputDepleted = true;
            return false;
        }

        setCurrentWord();
        handleCurrentByte();
        return true;
    }

    /**
     * Set current word. If the previousWord is null (meaning this is first byte
     * to encode), it contains only currentByte, otherwise it contains
     * previousWord + currentByte.
     */
    private void setCurrentWord() {
        if (previousWord == null) {
            currentWord = new Word(currentByte);
        } else {
            currentWord = previousWord.append(currentByte);
        }
    }

    /**
     * Read next byte from InputStream.
     *
     * @return false if no more bytes to read
     * @throws IOException if InputStream throws IOException
     */
    private boolean readNextByte() throws IOException {
        int currentByteInt = in.read();
        if (currentByteInt == -1) {
            return false;
        }
        currentByte = (byte) currentByteInt;
        return true;
    }

    /**
     * Handle current byte. If currentByte is in dictionary, just advance to the
     * next round of encoding to find out if a longer word can be decoded with
     * single byte. If currentByte is not in dictionary, add it, write the code
     * of previousByte to output and reset word.
     *
     * @throws IOException if OutputStream throws IOException
     */
    private void handleCurrentByte() throws IOException {
        if (dict.containsWord(currentWord)) {
            previousWord = currentWord;
        } else {
            dict.addWord(currentWord);
            dict.incrementSizeCounter();
            out.write(dict.getCode(previousWord));
            previousWord = new Word(currentByte);
        }
    }

    /**
     * Finalize encoding. Write leftover byte and EOF marker to OutputStream and
     * flush buffer.
     *
     * @throws IOException if OutputStream throws IOException
     */
    private void finalizeEncoding() throws IOException {
        if (previousWord != null) {
            dict.incrementSizeCounter();
            out.write(dict.getCode(previousWord));
        }

        dict.incrementSizeCounter();
        out.write(dict.getCode(LZWDictionary.EOF_WORD));
    }
    
    /**
     * Return true if InputStream was depleted while using encoding methods in this class.
     * 
     * @return true if InputStream depleted, otherwise false
     */
    public boolean inputDepleted() {
        return inputDepleted;
    }
}

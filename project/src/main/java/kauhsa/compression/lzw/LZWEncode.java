package kauhsa.compression.lzw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.utils.bitgroup.BitGroupOutputStream;
import kauhsa.utils.word.Word;

/**
 * Class that implements LZW encoding.
 */
public class LZWEncode {

    /**
     * Encode data using LZW algorithm.
     *
     * @param in Data to encode
     * @param out Encoded data
     * @throws IOException If using in or out results error
     */
    public static void encode(InputStream in, OutputStream out) throws IOException {
        BitGroupOutputStream bitGroupOut = new BitGroupOutputStream(out);
        LZWDictionary dict = new LZWDictionary(LZWDictionaryType.ENCODE);
        Word previousWord = null;
        Word currentWord;

        while (true) {
            /* Read next byte, but stop encoding if there is no more bytes left
             * to encode */
            int currentByteInt = in.read();
            if (currentByteInt == -1) {
                break;
            }
            byte currentByte = (byte) currentByteInt;

            currentWord = getCurrentWord(previousWord, currentByte);

            if (dict.containsWord(currentWord)) {
                /* Current word is already in dictionary - let's find out if we
                 * can encode even longer word with single code */
                previousWord = currentWord;
            } else {
                /* Previous word was longest we could encode with current
                 * dictionary - add current word to dictionary, write the code
                 * of previous word to OutputStream and reset word */
                dict.addWord(currentWord);
                dict.incrementSizeCounter();
                bitGroupOut.write(dict.getCode(previousWord));
                previousWord = new Word(currentByte);
            }
        }

        /* Write the last word that didn't get written to OutputStream, write
         * code that means end of file and flush the possible buffer byte left
         * in BitGroupOutputStream */
        if (previousWord != null) {
            dict.incrementSizeCounter();
            bitGroupOut.write(dict.getCode(previousWord));
        }
        
        dict.incrementSizeCounter();
        bitGroupOut.write(dict.getCode(LZWDictionary.EOF_WORD));
        bitGroupOut.flush();
    }

    /**
     * Get current word. If the previousWord is null (meaning this is first byte
     * to encode), it contains only currentByte, otherwise it contains
     * previousWord + currentByte.
     *
     * @param previousWord word of last iteration of LZW encoding loop
     * @param currentByte current byte to encode
     * @return currentWord for LZW encoding loop.
     */
    private static Word getCurrentWord(Word previousWord, byte currentByte) {
        Word currentWord;

        if (previousWord == null) {
            currentWord = new Word(currentByte);
        } else {
            currentWord = previousWord.append(currentByte);
        }

        return currentWord;
    }
}

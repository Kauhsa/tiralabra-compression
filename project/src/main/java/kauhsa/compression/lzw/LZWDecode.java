package kauhsa.compression.lzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.bitgroup.ByteArrayIterator;
import kauhsa.utils.word.Word;

/**
 * Class that implements LZW decoding.
 */
public class LZWDecode {

    /**
     * Decode LZW data.
     * 
     * TODO: Change to use Input/OutputStream like encoder.
     * 
     * @param data data to decode
     * @return decoded data
     * @throws IOException 
     */
    public static byte[] decode(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayIterator bat = new ByteArrayIterator(data);
   
        /* Ugly hack for now. The reason for this is that dictionary size should
         * be increased whenever new word is read from dictionary rather than
         * whenever there is a word added there. Without this, the code length
         * will be desynced with encoder, resulting wrong codes to get read and
         * decoding turns to carbage after a certain point. */
        LZWDictionary dict = new LZWDictionary();
        dict.incrementSizeOffset();
        dict.incrementSizeOffset();

        BitGroup currentCode;
        Word currentWord;
        Word previousWord = null;
        byte lastCharacter;
        
        while (true) {
            /* Get next code in binary stream */
            currentCode = bat.next(dict.getCurrentBitSize());
            if (currentCode == null) {
                break;
            }
        
            currentWord = dict.getWord(currentCode);
            if (currentWord != null && currentWord.equals(LZWDictionary.EOF_WORD)) {
                break;
            }
            
            if (currentWord != null) {
                /* Normally the unknown character is the first character of
                 * current word */
                lastCharacter = currentWord.getData()[0];
            } else {
                /* Handle the special case when the code is not found in
                 * dictionary - only possible character is first of previous
                 * word */
                lastCharacter = previousWord.getData()[0];
                currentWord = previousWord.append(lastCharacter);
            }
            
            /* previousWord is only null in the first round of decoding loop */
            if (previousWord != null) {
                Word newWord = new Word(previousWord.getData()).append(lastCharacter);
                dict.addWord(newWord);
            }
            
            out.write(currentWord.getData());
            previousWord = currentWord;
        }
        
        return out.toByteArray();
    }
    
}

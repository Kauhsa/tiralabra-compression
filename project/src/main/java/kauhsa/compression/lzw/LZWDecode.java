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

        LZWDictionary dict = new LZWDictionary(LZWDictionaryType.DECODE);

        BitGroup currentCode;
        Word currentWord;
        Word previousWord = null;
        byte lastCharacter;
        
        while (true) {
            dict.incrementSizeCounter();
            
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

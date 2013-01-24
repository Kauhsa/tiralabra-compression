package kauhsa.compression.lzw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.utils.bitgroup.BitGroupInputStream;
import kauhsa.utils.word.Word;

/**
 * Class that implements LZW decoding.
 */
public class LZWDecode {

    /**
     * Decode LZW data.
     * 
     * @param data data to decode
     * @return decoded data
     * @throws IOException 
     */
    public static void decode(InputStream in, OutputStream out) throws IOException {
        BitGroupInputStream bitGroupInputStream = new BitGroupInputStream(in);
        LZWDictionary dict = new LZWDictionary(LZWDictionaryType.DECODE);
        Word currentWord;
        Word previousWord = null;
        
        while (true) {
            dict.incrementSizeCounter();
            
            /* Get next word for code in binary stream */
            currentWord = dict.getWord(bitGroupInputStream.read(dict.getCurrentBitSize()));
            if (currentWord != null && currentWord.equals(LZWDictionary.EOF_WORD)) {
                break;
            }

            byte lastCharacter;
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
    }
    
}

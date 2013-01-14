package kauhsa.compression.lzw;

import kauhsa.utils.word.Word;
import java.util.logging.Level;
import java.util.logging.Logger;
import kauhsa.utils.bitgroup.BitGroupWriter;

/**
 *
 * @author mcviinam
 */
public class LZW {
    
    private final static Logger logger = Logger.getLogger("LZW");
    
    public static byte[] encode(byte[] data) {
        BitGroupWriter writer = new BitGroupWriter();
        LZWDictionary dict = new LZWDictionary();   
        Word currentWord = null;
        Word nextWord;
        
        for (byte k : data) {   
            if (currentWord == null) {
                nextWord = new Word(k);
            } else {
                nextWord = currentWord.append(k);
            }
            
            if (dict.contains(nextWord)) {                
                logger.log(Level.INFO, "Word {0} found from dictionary", nextWord);
                currentWord = nextWord;
            } else {                
                logger.log(Level.INFO, "Word {0} not found from dictionary, added", nextWord);                
                dict.add(nextWord);                
                logger.log(Level.INFO, "BitGroup {0} for word {1} written", new Object[]{dict.get(currentWord), currentWord});
                writer.writeBitGroup(dict.get(currentWord));
                currentWord = new Word(k);
            }
        }
        
        writer.writeBitGroup(dict.get(LZWDictionary.EOF_WORD));        
        return writer.getByteArray();
    }
    
    public static byte[] decode(byte[] data) {
        return null;
    }    
    
}

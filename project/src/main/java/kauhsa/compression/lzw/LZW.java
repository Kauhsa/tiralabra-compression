package kauhsa.compression.lzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.bitgroup.BitGroupWriter;
import kauhsa.utils.bitgroup.ByteArrayIterator;
import kauhsa.utils.word.Word;

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
            
            if (dict.containsWord(nextWord)) {                
                // logger.log(Level.INFO, "Word {0} found from dictionary", nextWord);
                currentWord = nextWord;
            } else {                
                // logger.log(Level.INFO, "Word {0} not found from dictionary, added", nextWord);                
                dict.add(nextWord);                
                // logger.log(Level.INFO, "BitGroup {0} for word {1} written. size {2}", new Object[]{dict.getCode(currentWord), currentWord, dict.getDictionarySize()});
                writer.writeBitGroup(dict.getCode(currentWord));
                currentWord = new Word(k);
            }
        }
        
        writer.writeBitGroup(dict.getCode(currentWord));     
        writer.writeBitGroup(dict.getCode(LZWDictionary.EOF_WORD));        
        return writer.getByteArray();
    }
    
    public static byte[] decode(byte[] data) throws IOException {        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayIterator bat = new ByteArrayIterator(data);
        
        LZWDictionary dict = new LZWDictionary();
        dict.incrementSizeOffset();        
        dict.incrementSizeOffset();
        
        BitGroup currentCode;
        Word currentEntry;
        Word previousEntry = null;
        byte lastCharacter;
        
        while (true) {
            currentCode = bat.next(dict.getCurrentBitSize());
            if (currentCode == null) {
                break;
            }
            
            // logger.log(Level.INFO, "Read code {0}, size {1}", new Object[] {currentCode, dict.getDictionarySize()});
            currentEntry = dict.getWord(currentCode);
                        
            if (currentEntry != null && currentEntry.equals(LZWDictionary.EOF_WORD)) {
                break;
            } 
            
            if (currentEntry == null) {
                lastCharacter = previousEntry.getData()[0];
                currentEntry = previousEntry.append(lastCharacter);
            } else {
                lastCharacter = currentEntry.getData()[0];
            }
            
            if (previousEntry != null) {
                Word newWord = new Word(previousEntry.getData()).append(lastCharacter);
                dict.add(newWord);
            }
            
            // logger.log(Level.INFO, "Print {0}", new String(currentEntry.getData()));
            out.write(currentEntry.getData());
            previousEntry = currentEntry;
        }
        
        return out.toByteArray();
    }    
    
}

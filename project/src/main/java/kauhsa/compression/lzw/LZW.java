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
                //logger.log(Level.INFO, "Word {0} found from dictionary", nextWord);
                currentWord = nextWord;
            } else {                
                //logger.log(Level.INFO, "Word {0} not found from dictionary, added", nextWord);                
                dict.add(nextWord);                
                //logger.log(Level.INFO, "BitGroup {0} for word {1} written", new Object[]{dict.getCode(currentWord), currentWord});
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
        BitGroup currentCode;
        Word currentEntry;
        Word previousEntry = null;
        
        while (true) {
            currentCode = bat.next(dict.getCurrentBitSize());
            //logger.log(Level.INFO, "Read code {0}", currentCode);
            currentEntry = dict.getWord(currentCode);
                        
            if (currentEntry != null && currentEntry.equals(LZWDictionary.EOF_WORD)) {
                break;
            } else if (currentEntry == null) {
                Word newWord = new Word(previousEntry.getData()).append(previousEntry.getData()[0]);
                dict.add(newWord);
                currentEntry = newWord;
            } 
            
            if (previousEntry != null) {
                dict.add(new Word(previousEntry.getData()).append(currentEntry.getData()[0]));
            }
            
            out.write(currentEntry.getData());
            previousEntry = currentEntry;
        }
        
        return out.toByteArray();
    }    
    
}

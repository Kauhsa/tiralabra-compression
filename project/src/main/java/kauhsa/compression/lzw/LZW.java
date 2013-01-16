package kauhsa.compression.lzw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.utils.bitgroup.BitGroup;
import kauhsa.utils.bitgroup.BitGroupOutputStream;
import kauhsa.utils.bitgroup.ByteArrayIterator;
import kauhsa.utils.word.Word;

/**
 *
 * @author mcviinam
 */
public class LZW {
        
    public static void encode(InputStream in, OutputStream out) throws IOException {
        BitGroupOutputStream bgOutputStream = new BitGroupOutputStream(out);
        LZWDictionary dict = new LZWDictionary();   
        Word currentWord = null;
        Word nextWord;
        
        while (true) {
            int readByte = in.read();
            if (readByte == -1) {
                break;
            }
            
            byte k = (byte) readByte;
                    
            if (currentWord == null) {
                nextWord = new Word(k);
            } else {
                nextWord = currentWord.append(k);
            }
            
            if (dict.containsWord(nextWord)) {                
                currentWord = nextWord;
            } else {                              
                dict.add(nextWord);
                bgOutputStream.write(dict.getCode(currentWord));
                currentWord = new Word(k);
            }
        }
        
        bgOutputStream.write(dict.getCode(currentWord));     
        bgOutputStream.write(dict.getCode(LZWDictionary.EOF_WORD));
        bgOutputStream.flush();
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
            
            out.write(currentEntry.getData());
            previousEntry = currentEntry;
        }
        
        return out.toByteArray();
    }    
    
}

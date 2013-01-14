package kauhsa.compression.lzw;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author mcviinam
 */
public class LZW {
    
    public static byte[] encode(byte[] data) {
        LZWDictionary dict = new LZWDictionary();
        
        byte[] currentWord = null;
        byte[] nextWord;
        
        for (byte k : data) {   
            if (currentWord == null) {
                nextWord = new byte[] {k};
            } else {
                nextWord = Arrays.copyOf(currentWord, currentWord.length + 1);
                nextWord[nextWord.length - 1] = k;
            }
            
            if (dict.contains(nextWord)) {
                currentWord = nextWord;
            } else {
                dict.add(nextWord);
                System.out.println(dict.get(currentWord));
                currentWord = new byte[] {k};
            }
        }
        
        return null;
    }
    
    public static byte[] decode(byte[] data) {
        return null;
    }    
    
}

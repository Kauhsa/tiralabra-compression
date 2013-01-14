/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

/**
 *
 * @author mika
 */
public class ByteArrayIterator {
    private final byte[] byteArray;
    private int currentBitIndex;
    private int currentArrayIndex;
    
    public ByteArrayIterator(byte[] byteArray) {
        this.byteArray = byteArray;
        this.currentArrayIndex = 0;
        this.currentBitIndex = 0;
    }
    
    public BitGroup next(int bitCount) {
        int bitsLeft = bitCount;
        long data = 0;
        
        while (bitsLeft > 0) {
            data = data << 1;
            byte a = getBitFromCurrentPosition();
            data = data | a;
            
            bitsLeft--;
            
            if (currentBitIndex == 8) {
                currentArrayIndex++;
                currentBitIndex = 0;
            }
        }
        
        return new BitGroup(data, bitCount);
    }

    private byte getBitFromCurrentPosition() {        
        int currentOffset = 7 - currentBitIndex;
        byte a = (byte) ((byteArray[currentArrayIndex] & (1 << currentOffset)) >> currentOffset);
        
        currentBitIndex++;
        return a;
    }
}

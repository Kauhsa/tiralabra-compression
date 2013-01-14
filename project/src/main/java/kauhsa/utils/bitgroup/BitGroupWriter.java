/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author mika
 */
public class BitGroupWriter {    
    private Deque<Byte> byteQueue;
    private byte lastByteOffset = 0;
    
    public BitGroupWriter() {
        byteQueue = new LinkedList<Byte>();
        byteQueue.add((byte) 0);
    }
    
    public void writeBitGroup(BitGroup bitGroup) {        
        for (byte currentBit : new BitGroupBitIterator(bitGroup)) {            
            byte currentByte = byteQueue.removeLast();         
            currentByte = (byte) (currentByte | (currentBit << (7 - lastByteOffset)));
            byteQueue.addLast(currentByte);
            lastByteOffset++;
            
            if (lastByteOffset == 8) {
                byteQueue.add((byte) 0);
                lastByteOffset = 0;
            }
        }
    }
    
    public byte[] getByteArray() {
        byte[] byteArray = new byte[byteQueue.size()];
        int i = 0;
        while (!byteQueue.isEmpty()) {
            byte newByte = byteQueue.removeFirst();
            byteArray[i] = newByte;
            i++;
        }
        return byteArray;
    }
}

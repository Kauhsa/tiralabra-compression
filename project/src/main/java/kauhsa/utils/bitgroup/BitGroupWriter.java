/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import java.util.LinkedList;

/**
 *
 * @author mika
 */
public class BitGroupWriter {    
    private LinkedList<Byte> dataArray;
    private byte lastByteOffset = 0;
    
    public BitGroupWriter() {
        dataArray = new LinkedList<Byte>();
        dataArray.add((byte) 0);
    }
    
    public void writeBitGroup(BitGroup bitGroup) {        
        for (byte currentBit : new BitIterator(bitGroup)) {            
            byte currentByte = dataArray.removeLast();            
            currentByte = (byte) (currentByte | (currentBit << (7 - lastByteOffset)));
            dataArray.addLast(currentByte);
            lastByteOffset++;
            
            if (lastByteOffset == 8) {
                dataArray.add((byte) 0);
                lastByteOffset = 0;
            }
        }
    }
    
    public byte[] getByteArray() {
        byte[] byteArray = new byte[dataArray.size()];
        for (int i = 0; i < dataArray.size(); i++) {
            byteArray[i] = dataArray.get(i);
        }
        return byteArray;
    }
}

package kauhsa.utils.bitgroup;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author mika
 */
public class BitGroupOutputStream {
    private final OutputStream out;
    
    private int lastByteOffset = 0;
    private byte bufferByte;
    
    private long bitGroupData;
    private int bitGroupBits;
    
    public BitGroupOutputStream(OutputStream out) {
        this.out = out;
    }
    
    private int bitsLeftInBufferByte() {
        return Byte.SIZE - lastByteOffset;
    }
    
    public void write(BitGroup bitGroup) throws IOException {        
        bitGroupData = bitGroup.getData();
        bitGroupBits = (int) bitGroup.getBits();
        shiftBitGroupDataToLeft();
        
        while (bitGroupBits != 0) {
            fillBufferByte();
        }
    }
 
    private void shiftBitGroupDataToLeft() {
        bitGroupData = bitGroupData << (Long.SIZE - bitGroupBits);
    }

    private void fillBufferByte() throws IOException {       
        int bitsToWrite = Math.min(bitsLeftInBufferByte(), bitGroupBits);
                        
        long byteData = bitGroupData;
        byteData = byteData >>> (Long.SIZE - bitsToWrite);
        byteData = byteData << (Byte.SIZE - bitsToWrite - lastByteOffset);
        
        bufferByte = (byte) (bufferByte | byteData);
                
        bitGroupBits = bitGroupBits - bitsToWrite;        
        bitGroupData = bitGroupData << bitsToWrite;
        lastByteOffset = lastByteOffset + bitsToWrite;
        
        if (lastByteOffset == Byte.SIZE) {
            out.write(bufferByte);
            lastByteOffset = 0;
            bufferByte = (byte) 0;
        }
    }
    
    public void flush() throws IOException {
        if (lastByteOffset != 0) {
            out.write(bufferByte);
        }
    }
}

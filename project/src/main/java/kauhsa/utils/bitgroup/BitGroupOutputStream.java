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
        return 8 - lastByteOffset;
    }
    
    public void writeBitGroup(BitGroup bitGroup) throws IOException {        
        bitGroupData = bitGroup.getData();
        bitGroupBits = (int) bitGroup.getBits();
        shiftBitGroupDataToLeft();
        
        while (bitGroupBits != 0) {
            fillBufferByte();
        }
    }
 
    private void shiftBitGroupDataToLeft() {
        bitGroupData = bitGroupData << (64 - bitGroupBits);
    }

    private void fillBufferByte() throws IOException {       
        int bitsToWrite = Math.min(bitsLeftInBufferByte(), bitGroupBits);
        
        long bitMask = (1 << bitsToWrite) - 1;
        bitMask = bitMask << (64 - bitsToWrite);
                
        long byteData = bitGroupData & bitMask;
        bitGroupData = bitGroupData << bitsToWrite;
        byteData = byteData >>> (64 - bitsToWrite);
        byteData = byteData << (8 - bitsToWrite - lastByteOffset);
        
        bufferByte = (byte) (bufferByte | byteData);
                
        bitGroupBits = bitGroupBits - bitsToWrite;
        lastByteOffset = lastByteOffset + bitsToWrite;
        if (lastByteOffset == 8) {
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

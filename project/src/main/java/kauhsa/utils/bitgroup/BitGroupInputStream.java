package kauhsa.utils.bitgroup;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class BitGroupInputStream {

    private final InputStream inputStream;
    private byte bufferByte;
    private int bitsLeftInBuffer;
    
    public BitGroupInputStream(InputStream in) {
        this.inputStream = in;
        bitsLeftInBuffer = 0;
        bufferByte = 0;
    }
    
    public BitGroup read(int bitCount) throws IOException {
        long bitGroupData = 0;
        int bitsLeft = bitCount;
        
        while (bitsLeft != 0) {
            if (bitsLeftInBuffer == 0) {
                int readByte = inputStream.read();
                if (readByte == -1) {
                    throw new IllegalStateException("Not enough bytes in InputStream to create BitGroup");
                }
                bufferByte = (byte) readByte;
                bitsLeftInBuffer = 8;
            }
            
            int bitsToRead = Math.min(bitsLeftInBuffer, bitsLeft);

            
            bitGroupData = bitGroupData << Byte.SIZE;
            bitGroupData = bitGroupData | (bufferByte & 0xFF);
            bitGroupData = bitGroupData >>> (Byte.SIZE - bitsToRead);
            
            bufferByte = (byte) (bufferByte << bitsToRead);
            bitsLeftInBuffer -= bitsToRead;            
            bitsLeft -= bitsToRead;
        }
        
        return new BitGroup(bitGroupData, bitCount);
    }
}

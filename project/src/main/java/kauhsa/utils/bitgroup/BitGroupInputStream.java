package kauhsa.utils.bitgroup;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class that reads BitGroups from OutputStream.
 */
public class BitGroupInputStream {

    private final InputStream inputStream;
    private byte bufferByte;
    private int bitsLeftInBuffer;
    
    /**
     * Create new BitGroupInputStream.
     * 
     * @param in InputStream used for reading.
     */
    public BitGroupInputStream(InputStream in) {
        this.inputStream = in;
        bitsLeftInBuffer = 0;
        bufferByte = 0;
    }
    
    /**
     * Read selected number of bits from InputStream.
     * 
     * @param bitCount Number of bits to be read.
     * @return BitGroup containing wanted amount of bits.
     * @throws IOException If InputStream fails.
     * @throws IllegalStateException If there is less data in InputStream than bitCount.
     */
    public BitGroup read(int bitCount) throws IllegalStateException, IOException {
        long bitGroupData = 0;
        int bitsLeft = bitCount;
        
        while (bitsLeft != 0) {
            fillBufferByteIfNecessary();            
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

    /**
     * If we've used all data in bufferByte, fill it again from InputStream.
     * 
     * @throws IllegalStateException InputStream is empty
     * @throws IOException InputStream is silly
     */
    private void fillBufferByteIfNecessary() throws IllegalStateException, IOException {
        if (bitsLeftInBuffer == 0) {
            int readByte = inputStream.read();     
            
            if (readByte == -1) {
                throw new IllegalStateException("Not enough bytes in InputStream to create BitGroup");
            }
            
            bufferByte = (byte) readByte;
            bitsLeftInBuffer = 8;
        }
    }
}

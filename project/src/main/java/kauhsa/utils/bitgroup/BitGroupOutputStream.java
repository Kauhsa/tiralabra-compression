package kauhsa.utils.bitgroup;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class that writes BitGroups (instead of bytes) to OutputStream.
 */
public class BitGroupOutputStream {

    private final OutputStream out;
    private int lastByteOffset = 0;
    private byte bufferByte;
    private long bitGroupData;
    private int bitGroupBits;

    /**
     * Create new BitGroupOutputStream.
     *
     * @param out
     */
    public BitGroupOutputStream(OutputStream out) {
        this.out = out;
    }

    /**
     * Return how many bits are "empty" in bufferByte.
     *
     * @return how many bits are "empty" in bufferByte.
     */
    private int bitsLeftInBufferByte() {
        return Byte.SIZE - lastByteOffset;
    }

    /**
     * Write BitGroup to OutputStream. Not guaranteed to write instantly, as
     * there is single-byte buffer.
     *
     * @param bitGroup bitGroup to write
     * @throws IOException if something goofy is going on with OutputStream
     */
    public void write(BitGroup bitGroup) throws IOException {
        bitGroupData = bitGroup.getData();
        bitGroupBits = bitGroup.getBitCount();
        shiftBitGroupDataToLeft();

        while (bitGroupBits != 0) {
            writeToBufferByte();
            writeToOutputStream();
        }
    }

    /**
     * Usually BitGroup data is shift to right, like:
     *
     * "000000011" (let's imagine this is a long)
     *
     * This will shift it to left:
     *
     * "110000000"
     */
    private void shiftBitGroupDataToLeft() {
        bitGroupData = bitGroupData << (Long.SIZE - bitGroupBits);
    }

    /**
     * Return a slice from bitGroupData, positioned correctly to be added to
     * bufferByte.
     *
     * @param bitCount size of the slice.
     * @return a slice of bitGroupData.
     */
    private long getByteToWrite(int bitCount) {
        long byteData = bitGroupData;
        byteData = byteData >>> (Long.SIZE - bitCount);
        byteData = byteData << (Byte.SIZE - bitCount - lastByteOffset);
        return byteData;
    }

    /**
     * Update bitGroupData, bitGroupBits and lastByteOffset to accommodate the
     * latest write to bufferByte.
     *
     * @param bitsWritten how many bits were written
     */
    private void updateInternalValues(int bitsWritten) {
        bitGroupBits = bitGroupBits - bitsWritten;
        bitGroupData = bitGroupData << bitsWritten;
        lastByteOffset = lastByteOffset + bitsWritten;
    }

    /**
     * Fill bufferByte with bitGroupData. Tries always to fill the current
     * buffer byte, but there are not always enough bits left in bitGroupData to
     * do that.
     */
    private void writeToBufferByte() {
        int bitsToWrite = Math.min(bitsLeftInBufferByte(), bitGroupBits);
        long byteData = getByteToWrite(bitsToWrite);
        bufferByte = (byte) (bufferByte | byteData);
        updateInternalValues(bitsToWrite);
    }

    /**
     * If bufferByte is filled with data, write it to outputStream.
     */
    private void writeToOutputStream() throws IOException {
        if (lastByteOffset == Byte.SIZE) {
            out.write(bufferByte);
            lastByteOffset = 0;
            bufferByte = (byte) 0;
        }
    }

    /**
     * Write buffer to OutputStream. You should always do this after you've
     * written all BitGroups, but not before that.
     *
     * @throws IOException if OutputStream is being difficult
     */
    public void flush() throws IOException {
        if (lastByteOffset != 0) {
            out.write(bufferByte);
        }
    }
}

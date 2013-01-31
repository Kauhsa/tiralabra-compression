package kauhsa.compression.lzw;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.utils.bitgroup.BitGroupInputStream;
import kauhsa.utils.bitgroup.BitGroupOutputStream;

/**
 * Class that contains collection of static methods for encoding and decoding
 * LZW data.
 */
public class LZW {

    /**
     * Encode data to LZW format.
     *
     * @param in input data
     * @param out encoded data
     * @throws IOException if InputStream or OutputStream throw IOException
     */
    public static void encode(InputStream in, OutputStream out) throws IOException {
        BitGroupOutputStream bitGroupOut = new BitGroupOutputStream(out);
        LZWEncoder lzwEncoder = new LZWEncoder(in, bitGroupOut);
        lzwEncoder.encode();
        bitGroupOut.flush();
    }

    /**
     * Decode LZW-encoded data.
     *
     * @param in encoded data
     * @param out decoded data
     * @throws IOException if InputStream or OutputStream throw IOException
     */
    public static void decode(InputStream in, OutputStream out) throws IOException {
        BitGroupInputStream bitGroupIn = new BitGroupInputStream(in);
        LZWDecoder lzwDecoder = new LZWDecoder(bitGroupIn, out);
        lzwDecoder.decode();
    }

    /**
     * Encode LZW-encoded data in chunks. Helps to keep encoding large amounts
     * of data fast.
     *
     * @param in input data
     * @param out encoded data
     * @param dictionarySize maximum dictionary size where chunk is reset
     * @throws IOException if InputStream or OutputStream throw IOException
     */
    public static void encodeInChunks(InputStream in, OutputStream out, int dictionarySize) throws IOException {
        BitGroupOutputStream bitGroupOut = new BitGroupOutputStream(out);
        LZWEncoder lzwEncoder;

        do {
            lzwEncoder = new LZWEncoder(in, bitGroupOut);
            lzwEncoder.encodeUntilDictionarySize(dictionarySize);
        } while (!lzwEncoder.inputDepleted());
        
        lzwEncoder = new LZWEncoder(new ByteArrayInputStream(new byte[0]), bitGroupOut);
        lzwEncoder.encode();
        bitGroupOut.flush();
    }

    /**
     * Decode LZW-encoded data that has been encoded using encodeInChunks
     * method.
     *
     * @param in encoded data
     * @param out decoded data
     * @throws IOException if in or out throw IOException
     */
    public static void decodeChunks(InputStream in, OutputStream out) throws IOException {
        BitGroupInputStream bitGroupIn = new BitGroupInputStream(in);
        
        while (true) {
            ByteArrayOutputStream chunkBuffer = new ByteArrayOutputStream();
            LZWDecoder lzwDecoder = new LZWDecoder(bitGroupIn, chunkBuffer);
            lzwDecoder.decode();
            
            if (chunkBuffer.size() == 0) {
                break;
            }
            
            out.write(chunkBuffer.toByteArray());        
        }
    }
}

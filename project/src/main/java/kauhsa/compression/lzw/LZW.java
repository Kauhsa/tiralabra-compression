package kauhsa.compression.lzw;

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
}

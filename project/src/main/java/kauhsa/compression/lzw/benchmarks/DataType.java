package kauhsa.compression.lzw.benchmarks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Different types of binary data. Used for benchmarks, particularly because of
 * Caliper framework.
 */
public enum DataType {

    RANDOM {
        @Override
        byte[] getData(int bytes) {
            Random random = new Random();
            byte[] data = new byte[bytes];
            random.nextBytes(data);
            return data;
        }
    },
    FINNISH_TEXT {
        @Override
        byte[] getData(int bytes) throws IOException {
            InputStream res = DataType.class.getResourceAsStream("/seitseman_veljesta.txt");
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            while (out.size() < bytes) {
                int b = res.read();
                if (b == -1) {
                    throw new RuntimeException("Not enough data in file");
                }
                out.write(b);
            }

            return out.toByteArray();
        }
    };

    /**
     * Get byte array of data appropriate for this enum.
     *
     * @param bytes how many bytes of data is wanted.
     * @return byte array of data
     * @throws IOException
     */
    abstract byte[] getData(int bytes) throws IOException;
}

package kauhsa.compression.lzw.benchmarks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.compression.lzw.LZW;

/**
 * Benchmark for compression ratios with different settings.
 */
public class CompressionBenchmark {

    public static DataType[] dataTypes = DataType.values();
    public static int[] dataLengthsArray = new int[]{10000, 30000, 50000, 70000, 90000, 110000, 130000, 150000};
    public static int[] maximumDictSizes = new int[]{1000, 6000, 12000, -1};

    /**
     * Run benchmark.
     *
     * @param args ignored.
     * @throws IOException if InputStream or OutputStream throw an exception
     */
    public static void main(String[] args) throws IOException {
        for (DataType dataType : dataTypes) {
            for (int maximumDictSize : maximumDictSizes) {
                for (int bytesOfData : dataLengthsArray) {
                    benchmarkRatio(dataType, bytesOfData, maximumDictSize);
                }
            }
        }
    }

    /**
     * Encode data, using wanted maximum LZW dictionary size.
     *
     * @param in input data
     * @param out encoded data
     * @param maximumDictSize maximum LZW dictionary size, -1 disables chunks
     * @throws IOException if in or out throw an exception
     */
    private static void encode(InputStream in, OutputStream out, int maximumDictSize) throws IOException {
        if (maximumDictSize == -1) {
            LZW.encode(in, out);
        } else {
            LZW.encodeInChunks(in, out, maximumDictSize);
        }
    }

    /**
     * Do ratio benchmark.
     *
     * @param dataType selected data type
     * @param bytesOfData how many bytes of data
     * @param maximumDictSize maximum LZW dictionary size, -1 disables chunks
     * @throws IOException if InputStream of OutputStream throw an exception
     */
    private static void benchmarkRatio(DataType dataType, int bytesOfData, int maximumDictSize) throws IOException {
        byte[] inputData = dataType.getData(bytesOfData);
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(in, out, maximumDictSize);

        float ratio = (float) out.size() / inputData.length;
        System.out.printf("dataType: %s\t", dataType);        
        System.out.printf("maximumDictSize: %s\t", maximumDictSize);
        System.out.printf("bytesOfData: %s\t", bytesOfData);
        System.out.printf("ratio: %.2f\t", ratio);
        System.out.println();
    }
}

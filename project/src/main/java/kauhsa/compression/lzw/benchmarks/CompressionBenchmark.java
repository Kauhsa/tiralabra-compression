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
    public static int[] dataLengthsArray = new int[] {10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000};
    public static int[] maximumDictSizes = new int[] {-1, 1000, 3000, 6000, 9000};
    
    public static void main(String[] args) throws IOException {
        for (DataType dataType : dataTypes) {
            for (int bytesOfData : dataLengthsArray) {
                for (int maximumDictSize : maximumDictSizes) {
                    benchmarkRatio(dataType, bytesOfData, maximumDictSize);
                }
            }
        }
    }

    private static void encode(InputStream in, OutputStream out, int maximumDictSize) throws IOException {
        if (maximumDictSize == -1) {
            LZW.encode(in, out);
        } else {
            LZW.encodeInChunks(in, out, maximumDictSize);
        }
    }
    
    private static void benchmarkRatio(DataType dataType, int bytesOfData, int maximumDictSize) throws IOException {
        byte[] inputData = dataType.getData(bytesOfData);        
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(in, out, maximumDictSize);
        
        float ratio = (float) out.size() / inputData.length;
        System.out.printf("dataType: %s\t bytesOfData: %s\t maximumDictSize: %s\t ratio: %.3f", dataType, bytesOfData, maximumDictSize, ratio);
        System.out.println();
    }
}

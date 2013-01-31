package kauhsa.compression.lzw.benchmarks;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import kauhsa.compression.lzw.LZW;
import kauhsa.utils.dummyoutputstream.DummyOutputStream;

public class LZWPerformanceBenchmark extends SimpleBenchmark {

    @Param({"10000", "20000", "30000", "40000", "50000", "60000", "70000", "80000", "90000", "100000"})
    private int bytesInData;
    @Param
    private DataType dataType;
    private static byte[] inputData;
    private static byte[] encodedData;

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
                InputStream res = LZWPerformanceBenchmark.class.getResourceAsStream("/seitseman_veljesta.txt");
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

        abstract byte[] getData(int bytes) throws IOException;
    }

    @Override
    protected void setUp() throws IOException {
        inputData = dataType.getData(bytesInData);
        initializeEncodedData();
    }

    public void timeEncoding(int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(inputData);
            OutputStream out = new DummyOutputStream();
            LZW.encode(in, out);
        }
    }

    public void timeDecoding(int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(encodedData);
            OutputStream out = new DummyOutputStream();
            LZW.decode(in, out);
        }
    }

    public static void main(String[] args) {
        Runner.main(LZWPerformanceBenchmark.class, args);
    }

    private void initializeEncodedData() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZW.encode(in, out);
        encodedData = out.toByteArray();
    }
}

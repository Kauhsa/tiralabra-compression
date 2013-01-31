package kauhsa.compression.lzw.benchmarks;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import kauhsa.compression.lzw.LZW;
import kauhsa.utils.dummyoutputstream.DummyOutputStream;

public class LZWRandomBenchmark extends SimpleBenchmark {

    @Param({"10000", "20000", "30000", "40000", "50000", "60000", "70000", "80000", "90000", "100000"})
    private int size;
    private static byte[] randomData;
    private static byte[] encodedData;

    @Override
    protected void setUp() throws IOException {
        initialize();
        initializeEncodedData();
    }

    public void timeEncoding(int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(randomData);
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
        Runner.main(LZWRandomBenchmark.class, args);
    }

    private void initialize() {
        Random random = new Random();
        randomData = new byte[size];
        random.nextBytes(randomData);
    }

    private void initializeEncodedData() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(randomData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LZW.encode(in, out);        
        encodedData = out.toByteArray();
    }
}

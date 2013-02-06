package kauhsa.compression.lzw.benchmarks;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kauhsa.compression.lzw.LZW;
import kauhsa.utils.DummyOutputStream;

public class PerformanceBenchmark extends SimpleBenchmark {

    @Param
    private DataType dataType;
    @Param({"1000", "6000", "12000", "-1"})
    private int maximumDictSize;
    @Param({"10000", "30000", "50000", "70000", "90000", "110000", "130000", "150000"})
    private int bytesInData;
    private static byte[] inputData;
    private static byte[] encodedData;

    @Override
    protected void setUp() throws IOException {
        inputData = dataType.getData(bytesInData);
        initializeEncodedData();
    }

    private void initializeEncodedData() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doEncoding(in, out);
        encodedData = out.toByteArray();
    }

    private void doEncoding(InputStream in, OutputStream out) throws IOException {
        if (maximumDictSize == -1) {
            LZW.encode(in, out);
        } else {
            LZW.encodeInChunks(in, out, maximumDictSize);
        }
    }

    private void doDecoding(InputStream in, OutputStream out) throws IOException {
        if (maximumDictSize == -1) {
            LZW.decode(in, out);
        } else {
            LZW.decodeChunks(in, out);
        }
    }

    public void timeEncoding(int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(inputData);
            OutputStream out = new DummyOutputStream();
            doEncoding(in, out);
        }
    }

    public void timeDecoding(int reps) throws IOException {
        for (int i = 0; i < reps; i++) {
            ByteArrayInputStream in = new ByteArrayInputStream(encodedData);
            OutputStream out = new DummyOutputStream();
            doDecoding(in, out);
        }
    }

    public static void main(String[] args) {
        Runner.main(PerformanceBenchmark.class, args);
    }
}

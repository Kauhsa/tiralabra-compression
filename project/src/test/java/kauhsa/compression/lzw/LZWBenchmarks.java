package kauhsa.compression.lzw;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import kauhsa.utils.bitgroup.BitGroupOutputStream;
import kauhsa.utils.dummyoutputstream.DummyOutputStream;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class LZWBenchmarks {
    private static byte[][] randomData;
    
    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    private static byte[] getRandomData(int length) {
        Random random = new Random();
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
    
    @Before
    public void prepare() {
        randomData = new byte[][] {
            getRandomData(10000),
            getRandomData(20000),
            getRandomData(30000),
            getRandomData(40000),
            getRandomData(50000),
            getRandomData(60000),
            getRandomData(70000),
            getRandomData(80000),
            getRandomData(90000),
            getRandomData(100000),
        };
    }
    
    @Test
    public void RandomCharacters10000() throws IOException {
        runTest(randomData[0]);
    }
    
    @Test
    public void RandomCharacters20000() throws IOException {
        runTest(randomData[1]);
    }
    
    @Test
    public void RandomCharacters30000() throws IOException {
        runTest(randomData[2]);
    }
    
    @Test
    public void RandomCharacters40000() throws IOException {
        runTest(randomData[3]);
    }
    
    @Test
    public void RandomCharacters50000() throws IOException {
        runTest(randomData[4]);
    }
    
    @Test
    public void RandomCharacters60000() throws IOException {
        runTest(randomData[5]);
    }
    
    @Test
    public void RandomCharacters70000() throws IOException {
        runTest(randomData[6]);
    }
    
    @Test
    public void RandomCharacters80000() throws IOException {
        runTest(randomData[7]);
    }
    
    @Test
    public void RandomCharacters90000() throws IOException {
        runTest(randomData[8]);
    }
    
    @Test
    public void RandomCharacters100000() throws IOException {
        runTest(randomData[9]);
    }
    
    private void runTest(byte[] data) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        OutputStream dummyOut = new DummyOutputStream();
        BitGroupOutputStream out = new BitGroupOutputStream(dummyOut);
        LZWEncoder lzwe = new LZWEncoder(in, out);
        lzwe.encode();
    }
}

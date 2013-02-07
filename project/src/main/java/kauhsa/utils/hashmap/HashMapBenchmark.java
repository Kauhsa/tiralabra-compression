package kauhsa.utils.hashmap;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

/**
 *
 */
public class HashMapBenchmark extends SimpleBenchmark {

    @Param({"50000", "100000", "150000", "200000", "250000", "300000"})
    private int keyCount;
    private KauhsaHashMap<Object, Object> insertionHashMap = new KauhsaHashMap<>();
    private KauhsaHashMap<Object, Object> retrievalHashMap = new KauhsaHashMap<>();

    @Override
    protected void setUp() {
        for (Integer keyNumber = 0; keyNumber < keyCount; keyNumber++) {
            retrievalHashMap.put(keyNumber, "");
        }
    }

    public void timePut(int reps) {
        for (int i = 0; i < reps; i++) {
            insertionHashMap = new KauhsaHashMap<>();
            for (Integer keyNumber = 0; keyNumber < keyCount; keyNumber++) {
                insertionHashMap.put(keyNumber, "");
            }
        }
    }

    public void timeGet(int reps) {
        for (int i = 0; i < reps; i++) {
            for (Integer keyNumber = 0; keyNumber < keyCount; keyNumber++) {
                retrievalHashMap.get(keyNumber);
            }
        }
    }
    
    public void timeContains(int reps) {
        for (int i = 0; i < reps; i++) {
            for (Integer keyNumber = 0; keyNumber < keyCount * 2; keyNumber++) {
                retrievalHashMap.containsKey(keyNumber);
            }
        }
    }

    public static void main(String[] args) {
        Runner.main(HashMapBenchmark.class, args);
    }
}

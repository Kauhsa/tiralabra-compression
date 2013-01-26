package kauhsa.utils.hashmap;

import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KauhsaHashMapTest {
    private KauhsaHashMap<String, byte[]> hashMap;
    
    @Before
    public void setUp() {
        hashMap = new KauhsaHashMap<>();
    }
    
    @Test
    public void putTest() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            sb.append((char) random.nextInt(256));
            String currentKey = sb.toString();
            byte[] randomBytes = new byte[10];
            random.nextBytes(randomBytes);
            
            assertFalse(hashMap.containsKey(currentKey));
            assertNull(hashMap.get(currentKey));
            hashMap.put(currentKey, randomBytes);
            assertTrue(hashMap.containsKey(currentKey));
            assertArrayEquals(randomBytes, hashMap.get(currentKey));
        }
    }
    
    @Test
    public void updateTest() {
        byte[] randomKey = new byte[10];        
        byte[] randomValue = new byte[10];
        Random random = new Random();
        
        for (int i = 0; i < 100; i++) {
            random.nextBytes(randomKey);
            String currentKey = new String(randomKey);
                        
            for (int i2 = 0; i2 < 100; i2++) {
                random.nextBytes(randomValue);
                hashMap.put(currentKey, randomValue);            
                assertArrayEquals(randomValue, hashMap.get(currentKey));
            }
        }
    }
}

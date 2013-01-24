package kauhsa.utils.bitgroup;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author mcviinam
 */
public class BitGroupTest {

    private BitGroup entry;
    
    @Before
    public void setUp() {
        entry = null;
    }

    public void entryTest(long data, int bytes, String s) {
        entry = new BitGroup(data, bytes);
        assertEquals(s, entry.toString());
    }
    
    @Test
    public void stringRepresentationTest() {
        entryTest(1, 1, "1");
        entryTest(2, 2, "10");
        entryTest(2, 3, "010");        
        entryTest(3, 7, "0000011");
    }
}

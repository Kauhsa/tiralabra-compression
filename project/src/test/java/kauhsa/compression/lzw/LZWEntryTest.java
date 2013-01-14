package kauhsa.compression.lzw;

import kauhsa.utils.bitgroup.BitGroup;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mcviinam
 */
public class LZWEntryTest {

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

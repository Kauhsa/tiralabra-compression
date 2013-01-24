package kauhsa.utils.bitgroup;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BitGroupTest {

    private BitGroup entry;
    
    @Before
    public void setUp() {
        entry = null;
    }

    private void assertStringRepresentation(long data, int bytes, String s) {
        entry = new BitGroup(data, bytes);
        assertEquals(s, entry.toString());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void bitCountLessThan1Test() {
        new BitGroup(0, 0);
    }          
    
    @Test(expected=IllegalArgumentException.class)
    public void bitCountMoreThan64Test() {
        new BitGroup(0, 65);
    }
    
    @Test
    public void stringRepresentationTest() {
        assertStringRepresentation(1, 1, "1");
        assertStringRepresentation(2, 2, "10");
        assertStringRepresentation(2, 3, "010");        
        assertStringRepresentation(3, 7, "0000011");
    }
}

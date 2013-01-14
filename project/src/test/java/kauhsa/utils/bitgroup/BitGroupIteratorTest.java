/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mika
 */
public class BitGroupIteratorTest {
        
    @Before
    public void setUp() {
       
    }
    
    @Test
    public void BitIteratorTest() {
        BitGroup bg = new BitGroup(2, 2);
        BitIterator bi = new BitIterator(bg);        
        assertEquals(1, (byte) bi.next());
        assertEquals(0, (byte) bi.next());
    }
    
    
    @Test
    public void BitIteratorTest2() {
        BitGroup bg = new BitGroup(50, 7);
        BitIterator bi = new BitIterator(bg);        
        assertEquals(0, (byte) bi.next());
        assertEquals(1, (byte) bi.next());
        assertEquals(1, (byte) bi.next());
        assertEquals(0, (byte) bi.next());        
        assertEquals(0, (byte) bi.next());        
        assertEquals(1, (byte) bi.next());        
        assertEquals(0, (byte) bi.next());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mika
 */
public class BitGroupInputStreamTest {
    
    @Test
    public void readTest() throws IOException{
        byte[] bytes = new byte[]{(byte) 0b11110000, (byte) 0b11110000};
        BitGroupInputStream bgis = new BitGroupInputStream(new ByteArrayInputStream(bytes));
        
        assertEquals(new BitGroup(0b00001111, 4), bgis.read(4));
        assertEquals(new BitGroup(0b00000000, 2), bgis.read(2));
        assertEquals(new BitGroup(0b00000011, 4), bgis.read(4));        
        assertEquals(new BitGroup(0b00110000, 6), bgis.read(6));
    }
}

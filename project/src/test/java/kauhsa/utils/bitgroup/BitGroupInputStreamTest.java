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
        byte[] bytes = new byte[]{(byte) 0b11110000, (byte) 0b11110000, (byte) 0b10011011, (byte) 0b10010111};
        BitGroupInputStream bgis = new BitGroupInputStream(new ByteArrayInputStream(bytes));
        
        assertEquals(new BitGroup(0b1111, 4), bgis.read(4));
        assertEquals(new BitGroup(0b00, 2), bgis.read(2));
        assertEquals(new BitGroup(0b0011, 4), bgis.read(4));        
        assertEquals(new BitGroup(0b110000, 6), bgis.read(6));               
        assertEquals(new BitGroup(0b1001101110010111, 16), bgis.read(16));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mika
 */
public class BitGroupOutputStreamTest {

    private BitGroupOutputStream bitGroupWriter;
    private ByteArrayOutputStream outputStream;

    public BitGroupOutputStreamTest() {
    }

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        bitGroupWriter = new BitGroupOutputStream(outputStream);
    }

    private void singleByteTest(BitGroup bitGroup, int binary) throws IOException {
        bitGroupWriter.writeBitGroup(bitGroup);
        bitGroupWriter.flush();

        byte[] byteArray = outputStream.toByteArray();
        assertEquals((byte) binary, byteArray[0]);
        assertEquals("byteArray is longer than 1", 1, byteArray.length);
    }

    @Test
    public void singleByteTest1() throws IOException {
        singleByteTest(new BitGroup(1, 1), 0b10000000);
    }

    @Test
    public void singleByteTest2() throws IOException {
        singleByteTest(new BitGroup(1, 2), 0b01000000);
    }

    @Test
    public void singleByteTest3() throws IOException {
        singleByteTest(new BitGroup(3, 2), 0b11000000);
    }

    @Test
    public void singleByteTest4() throws IOException {
        singleByteTest(new BitGroup(3, 3), 0b01100000);
    }

    @Test
    public void singleByteTest5() throws IOException {
        singleByteTest(new BitGroup(1, 5), 0b00001000);
    }

    @Test
    public void singleByteTest6() throws IOException {
        singleByteTest(new BitGroup(255, 8), 0b11111111);
    }

    @Test
    public void testMultipleBitGroups1() throws IOException {
        bitGroupWriter.writeBitGroup(new BitGroup(1, 1));        
        bitGroupWriter.writeBitGroup(new BitGroup(1, 1));
        bitGroupWriter.flush();
        
        assertEquals((byte) 0b11000000, outputStream.toByteArray()[0]);        
    }
    
    @Test
    public void testMultipleBitGroups2() throws IOException {
        bitGroupWriter.writeBitGroup(new BitGroup(1, 2));        
        bitGroupWriter.writeBitGroup(new BitGroup(1, 2));
        bitGroupWriter.flush();
        
        assertEquals((byte) 0b01010000, outputStream.toByteArray()[0]);        
    }
    
    @Test
    public void testMultipleBitGroups3() throws IOException {
        bitGroupWriter.writeBitGroup(new BitGroup(2, 2));        
        bitGroupWriter.writeBitGroup(new BitGroup(1, 2));           
        bitGroupWriter.writeBitGroup(new BitGroup(1, 3));
        bitGroupWriter.flush();
        
        assertEquals((byte) 0b10010010, outputStream.toByteArray()[0]);        
    }
}

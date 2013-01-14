/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kauhsa.utils.bitgroup;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author mika
 */
public class BitGroupBitIterator implements Iterator<Byte>, Iterable<Byte> {
    private final BitGroup bg;
    private final long readBitLocation;
    private long data;
    private long bitsLeft;
    
    public BitGroupBitIterator(BitGroup bg) {
        this.bg = bg;
        this.data = bg.getData();
        this.bitsLeft = bg.getBits();
        this.readBitLocation = bitsLeft - 1;
    }

    public boolean hasNext() {
        return bitsLeft > 0;
    }

    public Byte next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        
        byte currentBit = (byte) ((data & (1 << readBitLocation)) >> readBitLocation);
        data = data << 1;
        bitsLeft--;
        return currentBit;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public Iterator<Byte> iterator() {
        return new BitGroupBitIterator(bg);
    } 
    
}

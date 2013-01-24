package kauhsa.utils.bitgroup;

/**
 * BitGroup represents 1-64 bits of data, stored in a single long.
  */
public class BitGroup {
    private final long data;
    private final int bitCount;    

    /**
     * Create a new BitGroup. 
     * 
     * BitGroup(1, 1) = "1"
     * BitGroup(1, 2) = "01"
     * BitGroup(3, 3) = "011"
     * BitGroup(3, 10) = "0000000011"
     * 
     * @param data Data stored in BitGroup. Should not be larger that is
     * possible to represent with selected bitCount.
     * @param bitCount How many bits are used to represent data. Must be between
     * 1 and 64.
     */
    public BitGroup(long data, int bitCount) {
        if (bitCount < 1 || bitCount > 64) {
            throw new IllegalArgumentException("bitCount must be between 1 and 64");
        }
        
        this.data = data;
        this.bitCount = bitCount;
    }
    
    public long getData() {
        return data;
    }

    public int getBitCount() {
        return bitCount;
    }
    
    @Override
    public String toString() {
        String dataAsBinaryString = Long.toBinaryString(data);
        StringBuilder sb = new StringBuilder();
        
        // prepend the binary representation with appropriate count of zeros
        if (dataAsBinaryString.length() < bitCount) {
            int zeroCount = bitCount - dataAsBinaryString.length();
            for (int i = 0; i < zeroCount; i++) {
                sb.append("0");
            }
        }
        
        sb.append(dataAsBinaryString);
        return sb.toString();
    }
}

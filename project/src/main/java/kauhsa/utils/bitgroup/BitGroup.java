package kauhsa.utils.bitgroup;

public class BitGroup {
    private final long data;
    private final int bits;    

    public BitGroup(long data, int bits) {
        this.data = data;
        this.bits = bits;
    }

    public long getData() {
        return data;
    }

    public long getBits() {
        return bits;
    }
    
    @Override
    public String toString() {
        String dataAsBinaryString = Long.toBinaryString(data);
        StringBuilder sb = new StringBuilder();
        
        // prepend the binary representation with appropriate count of zeros
        if (dataAsBinaryString.length() < bits) {
            int zeroCount = bits - dataAsBinaryString.length();
            for (int i = 0; i < zeroCount; i++) {
                sb.append("0");
            }
        }
        
        sb.append(dataAsBinaryString);
        return sb.toString();
    }
}

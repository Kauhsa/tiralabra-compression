package kauhsa.utils.word;

import java.util.Arrays;

public class Word {
    private final byte[] data;
    
    public Word(byte b) {
        data = new byte[] {b};
    }
    
    public Word(byte[] byteArray) {
        data = byteArray;
    }
    
    public Word append(byte newByte) {
        byte[] newArray = Arrays.copyOf(data, data.length + 1);
        newArray[newArray.length - 1] = newByte;
        return new Word(newArray);
    }

    @Override
    public int hashCode() {
        if (this.data == null) {
            return 0;
        }
        return Arrays.hashCode(this.data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Word other = (Word) obj;
        if (this.data == null && other.data == null) {
            return true;
        }
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (data == null) {
            return "[null word]";
        }
        return new String(data);
    }
    
}

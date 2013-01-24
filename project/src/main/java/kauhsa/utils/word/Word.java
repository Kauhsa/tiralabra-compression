package kauhsa.utils.word;

import java.util.Arrays;

/**
 * Container for byte array. Exists mainly because you can not use byte arrays
 * directly in Maps as they don't implement hashCode or equals.
 */
public class Word {

    private final byte[] data;

    /**
     * Create new Word from a single byte.
     *
     * @param b contents of the new Word.
     */
    public Word(byte b) {
        data = new byte[]{b};
    }

    /**
     * Create new Word from byte array.
     *
     * @param byteArray contents of the new Word.
     */
    public Word(byte[] byteArray) {
        data = byteArray;
    }

    /**
     * Create new Word containing the contents of this Word and the byte given
     * as parameter added to end of the byte array.
     *
     * @param newByte byte to be added to the end of new Word.
     * @return new Word containing the byte given as parameter.
     */
    public Word append(byte newByte) {
        byte[] newArray = Arrays.copyOf(data, data.length + 1);
        newArray[newArray.length - 1] = newByte;
        return new Word(newArray);
    }

    @Override
    public int hashCode() {
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
        if (!Arrays.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (data == null) {
            return "null";
        }
        return new String(data);
    }

    public byte[] getData() {
        return data;
    }
}

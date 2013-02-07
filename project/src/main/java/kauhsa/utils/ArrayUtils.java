package kauhsa.utils;

/**
 * Collection of random utility methods related to arrays.
 */
public class ArrayUtils {

    /**
     * Return a copy of array that has new empty indices on the right.
     *
     * @param array array to copy
     * @param increase how many new indices are created
     * @return new array
     */
    @SuppressWarnings("ManualArrayToCollectionCopy")
    public static byte[] increaseArray(byte[] array, int increase) {
        byte[] newArray = new byte[array.length + increase];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    /**
     * Test if two arrays are equal.
     * 
     * @param a first array
     * @param b second array
     * @return true if a and b are equal
     */
    public static boolean arrayEquals(byte[] a, byte[] b) {
        if (a == null && b == null) {
            return true;
        } else if (a == null || b == null) {
            return false;
        } else if (a.length != b.length) {
            return false;
        }
        
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Return hash code of array.
     * 
     * @param a array
     * @return hash code
     */
    public static int arrayHashCode(byte[] a) {
        if (a == null) {
            return 0;
        }
        
        int hashCode = 1;
        for (byte i : a) {
            hashCode = hashCode * 31 + i;
        }        
        return hashCode;
    }
}

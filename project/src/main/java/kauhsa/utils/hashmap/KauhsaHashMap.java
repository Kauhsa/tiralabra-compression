package kauhsa.utils.hashmap;

import static kauhsa.utils.hashmap.Primes.primes;

/**
 * Simple hash map implementation. Uses open addressing and double hashing.
 */
public class KauhsaHashMap<KeyT, ValueT> {

    private final double MAX_LOAD_FACTOR = 0.5;
    private ObjectContainer<KeyT, ValueT>[] objectArray;
    private int objectCount = 0;
    private int currentPrimeIndex = 0;

    /**
     * Create a new, empty hash map.
     */
    public KauhsaHashMap() {
        objectArray = createNewObjectArray(primes[currentPrimeIndex]);
    }

    /**
     * Create new ObjectContainer[] with wanted size.
     *
     * @param size size of array
     * @return
     */
    private ObjectContainer<KeyT, ValueT>[] createNewObjectArray(int size) {
        return new ObjectContainer[size];
    }

    /**
     * Return index in array for specific object, try and array size. Returns
     * every index exactly once when called with tryNumbers from 0 to arraySize
     * - 1.
     *
     * @param obj Object of which hashCode is used.
     * @param tryNumber Try number.
     * @param arraySize Size of container array.
     * @return
     */
    private int hashFunction(Object obj, int tryNumber, int arraySize) {
        int hashCodeAbs = Math.abs(obj.hashCode());
        int hashFunctionOne = hashCodeAbs % arraySize;
        int hashFunctionTwo = 1 + (hashCodeAbs % (arraySize - 2));
        return (hashFunctionOne + tryNumber * hashFunctionTwo) % arraySize;
    }

    /**
     * Set a value for key in hash map.
     *
     * @param key selected key
     * @param value value to be set
     */
    public void put(KeyT key, ValueT value) {
        if (getLoadFactor() > MAX_LOAD_FACTOR) {
            rehash(primes[++currentPrimeIndex]);
        }

        put(new ObjectContainer(key, value), objectArray);
        objectCount++;
    }

    private double getLoadFactor() {
        return objectCount / (double) objectArray.length;
    }

    /**
     * Put objectContainer in either first table index that is empty or one that
     * has same key than objectContainer.
     *
     * @param objectContainer objectContainer to put
     * @param table table where objectContainer is put
     */
    private void put(ObjectContainer<KeyT, ValueT> objectContainer, ObjectContainer<KeyT, ValueT>[] table) {
        for (int i = 0; i < table.length; i++) {
            int tableIndex = hashFunction(objectContainer.getKey(), i, table.length);

            if (table[tableIndex] == null || table[tableIndex].keyEquals(objectContainer)) {
                table[tableIndex] = objectContainer;
                return;
            }
        }

        throw new RuntimeException("Something went terribly wrong!");
    }

    /**
     * Rehash map using container array of specific size.
     *
     * @param size size of new container array.
     */
    private void rehash(int size) {
        ObjectContainer<KeyT, ValueT>[] newObjectArray = createNewObjectArray(size);

        for (int i = 0; i < objectArray.length; i++) {
            ObjectContainer<KeyT, ValueT> objectContainer = objectArray[i];

            if (objectContainer != null) {
                put(objectContainer, newObjectArray);
            }
        }

        objectArray = newObjectArray;
    }

    /**
     * Get value set for specific key.
     *
     * @param key key of wanted value.
     * @return value if set for key, otherwise null
     */
    public ValueT get(KeyT key) {
        for (int i = 0; i < objectArray.length; i++) {
            int tableIndex = hashFunction(key, i, objectArray.length);
            ObjectContainer<KeyT, ValueT> objectContainer = objectArray[tableIndex];

            if (objectContainer == null) {
                return null;
            } else if (objectContainer.getKey().equals(key)) {
                return objectContainer.getValue();
            }
        }

        return null;
    }

    /**
     * Return true if value is set for specific key.
     * 
     * @param key
     * @return true if value if set for key, otherwise false
     */
    public boolean containsKey(KeyT key) {
        for (int i = 0; i < objectArray.length; i++) {
            int tableIndex = hashFunction(key, i, objectArray.length);
            ObjectContainer<KeyT, ValueT> objectContainer = objectArray[tableIndex];

            if (objectContainer == null) {
                return false;
            } else if (objectContainer.getKey().equals(key)) {
                return true;
            }
        }

        return false;
    }
}

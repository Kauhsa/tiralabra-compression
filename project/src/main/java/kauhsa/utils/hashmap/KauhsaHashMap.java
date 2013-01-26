package kauhsa.utils.hashmap;

import static kauhsa.utils.hashmap.Primes.primes;

/**
 * Simple hash map implementation. Uses open addressing.
 */
public class KauhsaHashMap<KeyT, ValueT> {
    private final double MAX_LOAD_FACTOR = 0.75;
    
    private ObjectContainer<KeyT, ValueT>[] objectTable;
    private int objectCount = 0;
    private int currentPrimeIndex = 0;
    
    private ObjectContainer<KeyT, ValueT>[] createNewObjectTable(int size) {
        return new ObjectContainer[size];
    }
    
    private int getHashIndex(int hashCode, int tryNumber, int containerSize) {
        int hashCodeAbs = Math.abs(hashCode);
        int hashFunctionOne = hashCodeAbs % containerSize;
        int hashFunctionTwo = 1 + (hashCodeAbs % (containerSize - 2));
        return (hashFunctionOne + tryNumber * hashFunctionTwo) % containerSize; 
    }
    
    public KauhsaHashMap() {
        objectTable = createNewObjectTable(primes[currentPrimeIndex]);
    }
    
    public void put(KeyT key, ValueT value) {
        if (objectCount / (double) objectTable.length > MAX_LOAD_FACTOR) {
            currentPrimeIndex++;
            rehash(primes[currentPrimeIndex]);
        }
        
        put(new ObjectContainer(key, value), objectTable);
        objectCount++;
    }
    
    private void put(ObjectContainer<KeyT, ValueT> objectContainer, ObjectContainer<KeyT, ValueT>[] table) {
        for (int i = 0; i < table.length; i++) {
            int tableIndex = getHashIndex(objectContainer.getKey().hashCode(), i, table.length);
            if (table[tableIndex] == null) {
                table[tableIndex] = objectContainer;
                return;
            } else if (table[tableIndex].getKey().equals(objectContainer.getKey())) {
                table[tableIndex].setValue(objectContainer.getValue());
                return;
            }
        }
        
        throw new RuntimeException("Something went terribly wrong!");
    }
    
    private void rehash(int size) {
        ObjectContainer<KeyT, ValueT>[] newObjectTable = createNewObjectTable(size);
        
        for (int i = 0; i < objectTable.length; i++) {
            ObjectContainer<KeyT, ValueT> objectContainer = objectTable[i];
            if (objectContainer != null) {
                put(objectContainer, newObjectTable);
            }
        }        
        
        objectTable = newObjectTable;
    }
    
    public ValueT get(KeyT key) {
        for (int i = 0; i < objectTable.length; i++) {
            int tableIndex = getHashIndex(key.hashCode(), i, objectTable.length);
            ObjectContainer<KeyT, ValueT> objectContainer = objectTable[tableIndex];
            if (objectContainer == null) {
                return null;
            } else if (objectContainer.getKey().equals(key)) {
                return objectContainer.getValue();
            }
        } 
        
        return null;
    }
    
    public boolean containsKey(KeyT key) {
        for (int i = 0; i < objectTable.length; i++) {
            int tableIndex = getHashIndex(key.hashCode(), i, objectTable.length);
            ObjectContainer<KeyT, ValueT> objectContainer = objectTable[tableIndex];
            if (objectContainer == null) {
                return false;
            } else if (objectContainer.getKey().equals(key)) {
                return true;
            }
        } 
        
        return false;
    }
    
}

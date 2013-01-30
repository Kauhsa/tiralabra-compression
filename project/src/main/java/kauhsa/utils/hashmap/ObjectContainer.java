package kauhsa.utils.hashmap;

/**
 * Container object for key and value for hash map.
 */
public class ObjectContainer<KeyT, ValueT> {

    private final KeyT key;
    private final ValueT value;

    /**
     * Create new ObjectContainer.
     *
     * @param key
     * @param value
     */
    public ObjectContainer(KeyT key, ValueT value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get key.
     *
     * @return key.
     */
    public KeyT getKey() {
        return key;
    }

    /**
     * Get value.
     *
     * @return value.
     */
    public ValueT getValue() {
        return value;
    }

    /**
     * Check if keys of two ObjectContainers are equal.
     * 
     * @param obj another ObjectContainer
     * @return true if keys are equal, otherwise false
     */
    public boolean keyEquals(ObjectContainer<KeyT, ValueT> obj) {
        return (obj.getKey().equals(key));
    }
}

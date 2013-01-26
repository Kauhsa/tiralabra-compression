package kauhsa.utils.hashmap;

/**
 * Container object for key and value for hash map.
 */
public class ObjectContainer<KeyT, ValueT> {

    private final KeyT key;
    private ValueT value;

    public ObjectContainer(KeyT key, ValueT value) {
        this.key = key;
        this.value = value;
    }

    public KeyT getKey() {
        return key;
    }

    public ValueT getValue() {
        return value;
    }

    public void setValue(ValueT value) {
        this.value = value;
    }
}

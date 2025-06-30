package src.dsa;

public class CustomHashMap<K, V> {
    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final CustomLinkedList<Entry<K, V>> entries = new CustomLinkedList<>();

    public void put(K key, V value) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        entries.add(new Entry<>(key, value));
    }

    public V get(K key) {
        for (Entry<K, V> entry : entries) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public CustomLinkedList<K> keySet() {
    	CustomLinkedList<K> keys = new CustomLinkedList<K>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.key);
        }
        return keys;
    }
}
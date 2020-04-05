package com.webcheckers.util;

/**
 * Custom pair class
 * 
 * @param <K> K can be any key for a value
 * @param <V> V can be any value corresponding to the key
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    /**
     * Quick construction of a Pair<K,V> without a new call
     * 
     * @param <K>   Any key
     * @param <V>   Any value
     * @param key   Any key corresponding to the value
     * @param value Anyvalue corresponding to the key
     * @return A pair of the key and the value
     */
    public static <K, V> Pair<K, V> createPair(K key, V value) {
        return new Pair<K, V>(key, value);
    }

    /**
     * Constructs a new Key-value pair
     * 
     * @param key   Key corresponding to the value
     * @param value Value corresponding to the key
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key stored internally
     * 
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value stored internally
     * 
     * @return the value
     */
    public V getValue() {
        return value;
    }

}

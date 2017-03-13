/*
 * Copyright (C) 2017 Shakhar Dasgupta
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.shakhar.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A hash table based implementation of the <tt>Map</tt> interface.
 *
 * <tt>MyHashMap</tt> permits <tt>null</tt> to be used as both keys and values.
 * <tt>MyHashMap</tt> always starts with a capacity of 16 and has a load factor
 * of 0.75.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 *
 * @author Shakhar Dasgupta
 */
public class MyHashMap<K, V> implements Map<K, V> {

    static final int INITIAL_CAPACITY = 16;

    static class Entry<K, V> implements Map.Entry<K, V> {

        K key;
        V value;
        Entry<K, V> next;
        int hash;

        public Entry(K key, V value, Entry<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry) o;
                return (getKey() == null ? e.getKey() == null : getKey().equals(e)) && (getValue() == null ? e.getValue() == null : getValue().equals(e.getValue()));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

    }

    Entry<K, V>[] table;
    int count;

    /**
     * Constructs an empty <tt>MyHashMap</tt>.
     */
    public MyHashMap() {
        table = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mapping.
     *
     * @return <tt>true</tt> if this map contains no key-value mapping
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     */
    @Override
    public boolean containsKey(Object key) {
        int h = key == null ? 0 : key.hashCode();
        Entry<K, V>[] t = table;
        int i = h & (t.length - 1);

        for (Entry<K, V> e = t[i]; e != null; e = e.next) {
            if (e.hash == h && (key == null ? e.key == null : key.equals(e.key))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified
     * value.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified
     * value
     */
    @Override
    public boolean containsValue(Object value) {
        Entry[] t = table;

        for (int i = 0; i < t.length; i++) {
            for (Entry<K, V> e = t[i]; e != null; e = e.next) {
                if (value == null ? e.value == null : value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return the value to which the specific key is mapped, or <tt>null</tt> if
     * this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specific key is mapped, or <tt>null</tt>
     * if this map contains no mapping for the key
     */
    @Override
    public V get(Object key) {
        int h = key == null ? 0 : key.hashCode();
        Entry<K, V>[] t = table;
        int i = h & (t.length - 1);

        for (Entry<K, V> e = t[i]; e != null; e = e.next) {
            if (e.hash == h && (key == null ? e.key == null : key.equals(e.key))) {
                return e.value;
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     * if there was no mapping for <tt>key</tt>
     */
    @Override
    public V put(K key, V value) {
        int h = key == null ? 0 : key.hashCode();
        Entry[] t = table;
        int i = h & (t.length - 1);
        V oldValue;
        for (Entry<K, V> e = t[i]; e != null; e = e.next) {
            if (e.hash == h && (key == null ? e.key == null : key.equals(e.key))) {
                oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }

        Entry<K, V> p = new Entry<>(key, value, t[i], h);
        t[i] = p;
        int c = count++;

        int n = t.length;
        if (c < (n - (n >>> 2))) {
            return null;
        }

        int newN = n << 1;
        Entry<K, V>[] newtab = (Entry<K, V>[]) new Entry[newN];

        for (i = 0; i < n; i++) {
            Entry<K, V> e;
            while ((e = t[i]) != null) {
                t[i] = e.next;
                int j = e.hash & (newN - 1);
                e.next = newtab[j];
                newtab[j] = e;
            }
        }
        table = newtab;
        return null;
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     * if there was no mapping for <tt>key</tt>
     */
    @Override
    public V remove(Object key) {
        int h = key == null ? 0 : key.hashCode();
        Entry<K, V>[] t = table;
        int i = h & (t.length - 1);
        Entry<K, V> pred = null;
        Entry<K, V> p = t[i];
        while (p != null) {
            if (p.hash == h && (key == null ? p.key == null : key.equals(p.key))) {
                if (pred == null) {
                    t[i] = p.next;
                } else {
                    pred.next = p.next;
                }
                count--;
                return p.value;
            }
            pred = p;
            p = p.next;
        }
        return null;
    }

    /**
     * Copies all of the mappings from the specified map to this map.
     *
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m != null) {
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        Entry<K, V>[] t = table;

        for (int i = 0; i < t.length; i++) {
            t[i] = null;
        }
        count = 0;
    }

    /**
     * Returns a <tt>Set</tt> view of the keys contained in this map.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        Entry[] t = table;
        for (int i = 0; i < t.length; i++) {
            for (Entry<K, V> e = t[i]; e != null; e = e.next) {
                keySet.add(e.key);
            }
        }
        return keySet;
    }

    /**
     * Returns a <tt>Collection</tt> view of the values contained in this map.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        Entry[] t = table;
        for (int i = 0; i < t.length; i++) {
            for (Entry<K, V> e = t[i]; e != null; e = e.next) {
                values.add(e.value);
            }
        }
        return values;
    }

    /**
     * Returns a <tt>Set</tt> view of the mappings contained in this map.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = new HashSet<>();
        Entry[] t = table;
        for (int i = 0; i < t.length; i++) {
            for (Entry<K, V> e = t[i]; e != null; e = e.next) {
                entrySet.add(e);
            }
        }
        return entrySet;
    }

    /**
     * Compare the specified object with this map for equality.
     *
     * @param o object to be compared for equality with this map
     * @return <tt>true</tt> if the specified object is equal to this map
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Map) {
            Map m = (Map) o;
            return entrySet().equals(m.entrySet());
        }
        return false;
    }

    /**
     * Returns the hash code value for this object.
     *
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        int s = 0;
        for (Map.Entry<K, V> e : entrySet()) {
            s += e.hashCode();
        }
        return s;
    }
}

/*
 * Copyright (C) 2017 Shakhar Dasgupta <sdasgupt@oswego.edu>
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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Shakhar Dasgupta
 * @param <K>
 * @param <V>
 */
public class BTreeMap<K extends Comparable<K> & Serializable, V extends Serializable> {

    private static class Node<K extends Comparable<K> & Serializable, V extends Serializable> implements Serializable {

        final int id;
        int n;
        boolean leaf;
        ArrayList<Entry<K, V>> entries;
        ArrayList<Integer> children;

        private Node(int id) {
            this.id = id;
            n = 0;
            leaf = true;
            entries = new ArrayList<>();
            children = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }

        public Entry<K, V> getEntry(int i) {
            return entries.get(i - 1);
        }

        public void setEntry(int i, Entry<K, V> e) {
            i--;
            if (i == entries.size()) {
                entries.add(e);
            } else {
                entries.set(i, e);
            }
        }

        public int getChild(int i) {
            return children.get(i - 1);
        }

        public void setChild(int i, int n) {
            i--;
            if (i == children.size()) {
                children.add(n);
            } else {
                children.set(i, n);
            }
        }

    }

    private static class Entry<K extends Comparable<K> & Serializable, V extends Serializable> implements Serializable {

        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V v) {
            value = v;
        }
        
        public String toString() {
            return key + ":" + value;
        }

    }

    final Disk<Node<K, V>> disk;
    int count;
    final int degree;
    Node<K, V> root;

    public BTreeMap(String name, int degree) throws IOException {
        disk = new Disk<>(name + ".index", name + ".objects", 3);

        if (disk.isEmpty()) {
            this.degree = degree;
            disk.writeInt(0, degree);
            count = 0;
            disk.writeInt(1, count);
            create();
        } else {
            this.degree = disk.readInt(0);
            count = disk.readInt(1);
            root = read(disk.readInt(2));
        }
    }

    private void write(Node node) {
        disk.writeElement(node.getId(), node);
    }

    private Node<K, V> read(int nodeId) {
        return disk.readElement(nodeId);
    }

    private void writeRoot() {
        disk.writeInt(2, root.getId());
    }

    private Node<K, V> allocateNode() {
        Node<K, V> node = new Node<>(count++);
        disk.writeInt(1, count);
        write(node);
        return node;
    }

    private void create() {
        Node<K, V> x = allocateNode();
        x.setLeaf(true);
        x.setN(0);
        write(x);
        root = x;
        writeRoot();
    }

    private void splitChild(Node<K, V> x, int i) {
        Node<K, V> z = allocateNode();
        Node<K, V> y = read(x.getChild(i));
        z.setLeaf(y.isLeaf());
        int t = degree;
        z.setN(t - 1);
        for (int j = 1; j <= t - 1; j++) {
            z.setEntry(j, y.getEntry(j + t));
        }
        if (!y.isLeaf()) {
            for (int j = 1; j <= t; j++) {
                z.setChild(j, y.getChild(j + t));
            }
        }
        y.setN(t - 1);
        for (int j = x.getN() + 1; j >= i + 1; j--) {
            x.setChild(j + 1, x.getChild(j));
        }
        x.setChild(i + 1, z.getId());
        for (int j = x.getN(); j >= i; j--) {
            x.setEntry(j + 1, x.getEntry(j));
        }
        x.setEntry(i, y.getEntry(t));
        x.setN(x.getN() + 1);
        write(y);
        write(z);
        write(x);
    }

    private void insertNonFull(Node<K, V> x, Entry<K, V> e) {
        int i = x.getN();
        if (x.isLeaf()) {
            while (i >= 1 && e.getKey().compareTo(x.getEntry(i).getKey()) < 0) {
                x.setEntry(i + 1, x.getEntry(i));
                i--;
            }
            x.setEntry(i + 1, e);
            x.setN(x.getN() + 1);
            write(x);
        } else {
            while (i >= 1 && e.getKey().compareTo(x.getEntry(i).getKey()) < 0) {
                i--;
            }
            i++;
            Node<K, V> node = read(x.getChild(i));
            int t = degree;
            if (node.getN() == 2 * t - 1) {
                splitChild(x, i);
                if (e.getKey().compareTo(x.getEntry(i).getKey()) > 0) {
                    i++;
                }
            }
            insertNonFull(node, e);
        }
    }

    private void insert(K k, V v) {
        Node<K, V> r = root;
        int t = degree;
        if (r.getN() == 2 * t - 1) {
            Node<K, V> s = allocateNode();
            root = s;
            writeRoot();
            s.setLeaf(false);
            s.setN(0);
            s.setChild(1, r.getId());
            splitChild(s, 1);
            insertNonFull(s, new Entry<>(k, v));
        } else {
            insertNonFull(r, new Entry<>(k, v));
        }
    }

    private V get(Node<K, V> x, K k) {
        int i = 1;
        while (i <= x.getN() && k.compareTo(x.getEntry(i).getKey()) > 0) {
            i++;
        }
        if (i <= x.getN() && k.compareTo(x.getEntry(i).getKey()) == 0) {
            return x.getEntry(i).getValue();
        } else if (x.isLeaf()) {
            return null;
        } else {
            return get(read(x.getChild(i)), k);
        }
    }

    public V get(K k) {
        return get(root, k);
    }

    private void put(Node<K, V> x, K k, V v) {
        int i = 1;
        while (i <= x.getN() && k.compareTo(x.getEntry(i).getKey()) > 0) {
            i++;
        }
        if (i <= x.getN() && k.compareTo(x.getEntry(i).getKey()) == 0) {
            x.getEntry(i).setValue(v);
            write(x);
        } else if (x.isLeaf()) {
            insert(k, v);
        } else {
            put(read(x.getChild(i)), k, v);
        }
    }

    public void put(K k, V v) {
        put(root, k, v);
    }
    
    private void traverse(Node<K,V> x) {
        int i;
        for(i = 1; i <= x.getN(); i++) {
            if(!x.isLeaf())
                traverse(read(x.getChild(i)));
            System.out.println(x.getEntry(i));
        }
        if(!x.isLeaf())
            traverse(read(x.getChild(i)));
    }
    
    public void traverse() {
        traverse(root);
    }
    
    private void keySet(Set<K> keySet, Node<K,V> x) {
        int i;
        for(i = 1; i <= x.getN(); i++) {
            if(!x.isLeaf())
                keySet(keySet, read(x.getChild(i)));
            keySet.add(x.getEntry(i).getKey());
        }
        if(!x.isLeaf())
            keySet(keySet, read(x.getChild(i)));
    }
    
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        keySet(keySet, root);
        return keySet;
    }
    
    private void values(Collection<V> values, Node<K,V> x) {
        int i;
        for(i = 1; i <= x.getN(); i++) {
            if(!x.isLeaf())
                values(values, read(x.getChild(i)));
            values.add(x.getEntry(i).getValue());
        }
        if(!x.isLeaf())
            values(values, read(x.getChild(i)));
    }
    
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        values(values, root);
        return values;
    }
    
    public boolean isEmpty() {
        return count == 1 && root.getN() == 0;
    }
}

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shakhar Dasgupta
 * @param <E> type of objects stored
 */
public class Disk<E extends Serializable> {

    private static final int POSITION_SIZE = 8;
    private static final int OBJECTS_OFFSET = 1;
    private static final int INT_SIZE = 4;

    private final RandomAccessFile indexFile;
    private final RandomAccessFile objectsFile;
    private final int integers;
    private long objectsEnd;

    public Disk(String indexFilename, String objectsFilename, int integers) throws FileNotFoundException, IOException {
        this.indexFile = new RandomAccessFile(indexFilename, "rw");
        this.objectsFile = new RandomAccessFile(objectsFilename, "rw");
        this.integers = integers;
        objectsEnd = objectsFile.length() == 0 ? objectsEnd = OBJECTS_OFFSET : objectsFile.length();
    }

    public boolean isEmpty() {
        try {
            return indexFile.length() == 0 && objectsFile.length() == 0;
        } catch (IOException ex) {
            Logger.getLogger(Disk.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void writeInt(int i, int n) {
        try {
            indexFile.seek(i * INT_SIZE);
            indexFile.writeInt(n);
        } catch (IOException ex) {
            Logger.getLogger(Disk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int readInt(int i) {
        try {
            indexFile.seek(i * INT_SIZE);
            return indexFile.readInt();
        } catch (IOException ex) {
            Logger.getLogger(Disk.class.getName()).log(Level.SEVERE, null, ex);
            return Integer.MIN_VALUE;
        }
    }

    public void writeElement(int index, E element) {
        try {
            indexFile.seek(integers * INT_SIZE + index * POSITION_SIZE);
            indexFile.writeLong(objectsEnd);
            objectsEnd += writeObject(objectsEnd, element);
        } catch (IOException ex) {
            Logger.getLogger(Disk.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public E readElement(int index) {
        try {
            int position = integers * INT_SIZE + index * POSITION_SIZE;
            if (position < indexFile.length()) {
                indexFile.seek(position);
                return readObject(indexFile.readLong());
            } else {
                return null;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Disk.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private int writeObject(long position, E element) throws IOException {
        objectsFile.seek(position);
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(byteArrayOutput)) {
            objectOutput.writeObject(element);
        }
        objectsFile.writeInt(byteArrayOutput.size());
        objectsFile.write(byteArrayOutput.toByteArray());
        return 4 + byteArrayOutput.size();
    }

    private E readObject(long position) throws IOException, ClassNotFoundException {
        if (position == 0) {
            return null;
        }
        objectsFile.seek(position);
        int size = objectsFile.readInt();
        byte[] bytes = new byte[size];
        objectsFile.read(bytes);
        ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
        E element = (E) objectInput.readObject();
        return element;
    }
}

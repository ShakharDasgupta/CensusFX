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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shakhar Dasgupta
 * @param <E> the type of elements
 */
public class FileArray<E> {

    private static final int POSITION_SIZE = 8;
    private static final int INT_SIZE = 4;
    private static final int NULL_ADDR = -1;

    private final int length;
    private int count;
    private String cacheFilename;
    private RandomAccessFile cacheFile;
    private long objectsPos;

    public FileArray(int length, String cacheFilename) {
        this.length = length;
        this.cacheFilename = cacheFilename;
        try {
            this.cacheFile = new RandomAccessFile(cacheFilename, "rw");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("FileNotFoundException");
        }

        try {
            boolean isNew = cacheFile.length() == 0;
            if (isNew) {
                count = 0;
                cacheFile.seek(INT_SIZE);
                for (int i = 0; i < length; i++) {
                    cacheFile.writeLong(NULL_ADDR);
                }
            } else {
                cacheFile.seek(0);
                count = cacheFile.readInt();
            }
            objectsPos = cacheFile.length();
        } catch (IOException ex) {
            Logger.getLogger(FileArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void set(int index, E element) {
        if (index >= length) {
            throw new RuntimeException("FileArrayIndexOutOfBoundsException");
        }
        try {
            if(get(index) == null) {
            cacheFile.seek(0);
            cacheFile.writeInt(++count);
        }
            cacheFile.seek(index * POSITION_SIZE + INT_SIZE);
            if (element != null) {
                cacheFile.writeLong(objectsPos);
                objectsPos += writeElement(objectsPos, element);
            } else {
                cacheFile.writeLong(NULL_ADDR);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public E get(int index) {
        if (index >= length) {
            throw new RuntimeException("FileArrayIndexOutOfBoundsException");
        }
        try {
            cacheFile.seek(index * POSITION_SIZE + INT_SIZE);
            long pos;
            if ((pos = cacheFile.readLong()) != NULL_ADDR) {
                return readElement(pos);
            } else {
                return null;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int length() {
        return length;
    }
    
    public int count() {
        return count;
    }

    public void rename(String newName) {
        try {
            cacheFile.close();
        } catch (IOException ex) {
            Logger.getLogger(FileArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        File file = new File(cacheFilename);
        file.renameTo(new File(newName));
        cacheFilename = newName;
        try {
            cacheFile = new RandomAccessFile(cacheFilename, "rw");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileArray.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int writeElement(long position, E element) throws IOException {
        cacheFile.seek(position);
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(byteArrayOutput)) {
            objectOutput.writeObject(element);
        }
        cacheFile.writeInt(byteArrayOutput.size());
        cacheFile.write(byteArrayOutput.toByteArray());
        return INT_SIZE + byteArrayOutput.size();
    }

    private E readElement(long position) throws IOException, ClassNotFoundException {
        cacheFile.seek(position);
        int size = cacheFile.readInt();
        byte[] bytes = new byte[size];
        cacheFile.read(bytes);
        ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
        E element = (E) objectInput.readObject();
        return element;
    }
}

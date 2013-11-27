package com.telenav.datatypes;

public class LongIntArrayList
{
    protected long[] elementData;

    protected int elementCount;

    public LongIntArrayList(int initialCapacity) 
    {
        this.elementData = new long[initialCapacity];
    }

    public synchronized int size() 
    {
        return elementCount;
    }

    public boolean contains(long elem) 
    {
        return indexOf(elem, 0) >= 0;
    }

    public int indexOf(long elem) 
    {
        return indexOf(elem, 0);
    }

    public synchronized int indexOf(long elem, int index) 
    {
        for (int i = index ; i < elementCount ; i++)
            if (elem == elementData[i])
                return i;
        return -1;
    }

    public synchronized int lastIndexOf(long elem) 
    {
        return lastIndexOf(elem, elementCount-1);
    }

    public synchronized int lastIndexOf(long elem, int index) 
    {
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);

        for (int i = index; i >= 0; i--)
            if (elem == elementData[i])
                return i;
        return -1;
    }

    public synchronized long elementAt(int index) 
    {
        if (index >= elementCount) 
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }

        return elementData[index];
    }

    public synchronized long firstElement() 
    {
        if (elementCount == 0) 
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return elementData[0];
    }

    public synchronized long lastElement() 
    {
        if (elementCount == 0) 
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        return elementData[elementCount - 1];
    }

    public synchronized void setElementAt(long obj, int index) 
    {
        if (index >= elementCount) 
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + 
                    elementCount);
        }
        elementData[index] = obj;
    }

    public synchronized long removeElementAt(int index) 
    {
        if (index >= elementCount) 
        {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + 
                    elementCount);
        }
        else if (index < 0) 
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        int j = elementCount - index - 1;
        if (j > 0) 
        {
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }
        elementCount--;
        long d = elementData[elementCount];
        elementData[elementCount] = 0;
        return d;
    }

    public synchronized void ensureCapacity(int minCapacity) 
    {
        ensureCapacityHelper(minCapacity);
    }

    private void ensureCapacityHelper(int minCapacity) 
    {
        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            long oldData[] = elementData;
            int newCapacity = oldCapacity * 2;
                    if (newCapacity < minCapacity) {
                        newCapacity = minCapacity;
                    }
                    elementData = new long[newCapacity];
                    System.arraycopy(oldData, 0, elementData, 0, elementCount);
        }
    }
    
    public synchronized void insertElementAt(long obj, int index) 
    {
        if (index > elementCount) 
        {
            throw new ArrayIndexOutOfBoundsException(index
                    + " > " + elementCount);
        }
        ensureCapacityHelper(elementCount + 1);
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        elementData[index] = obj;
        elementCount++;
    }

    public synchronized void addElement(long obj) 
    {
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = obj;
    }

    public synchronized boolean removeElement(long obj) 
    {
        int i = indexOf(obj);
        if (i >= 0) 
        {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public synchronized void removeAllElements() 
    {
        for (int i = 0; i < elementCount; i++)
            elementData[i] = 0;

        elementCount = 0;
    }
}

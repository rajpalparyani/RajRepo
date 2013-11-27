package com.telenav.cache.intcache;

import java.util.Vector;

public class LongIntLRUCache
{
    private Entry table[];

    private Entry header;
    
    private int count;

    private int maxSize; 

    public LongIntLRUCache(int maxSize) 
    {
        this.maxSize = maxSize;
        table = new Entry[maxSize];
        header = new Entry(-1, 0, null, null);
        clear();
    }

    public synchronized int size() 
    {
        return count;
    }

    public synchronized void keys(LongIntVector v) 
    {
        Entry entry = header.after;
        while(entry != header)
        {
            v.addElement(entry.key);
            entry = entry.after;
        }
    }
    
    public synchronized LongIntVector keys()
    {
        LongIntVector v = new LongIntVector(count);
        keys(v);
        return v;
    }

    public synchronized void elements(Vector v) 
    {
        Entry entry = header.after;
        while(entry != header)
        {
            v.addElement(entry.value);
            entry = entry.after;
        }
    }
    
    public synchronized Vector elements()
    {
        Vector v = new Vector(count);
        elements(v);
        return v;
    }

    protected synchronized boolean contains(Object value) 
    {
        if (value==null) 
        {
            for (Entry e = header.after; e != header; e = e.after)
                if (e.value==null)
                    return true;
        } 
        else 
        {
            for (Entry e = header.after; e != header; e = e.after)
                if (value.equals(e.value))
                    return true;
        }
        return false;
    }

    public boolean containsValue(Object value) 
    {
        return contains(value);
    }

    public synchronized boolean containsKey(long key) 
    {
        Entry tab[] = table;
        int hash = (int)(key ^ (key >>> 32));
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key == key) 
            {
                return true;
            }
        }
        return false;
    }

    public synchronized Object get(long key) 
    {
        Entry tab[] = table;
        int hash = (int)(key ^ (key >>> 32));
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key == key) 
            {
                e.recordAccess(this);
                return e.value;
            }
        }
        return null;
    }

    public synchronized Object put(long key, Object value) 
    {
        // Makes sure the key is not already in the hashtable.
        Entry tab[] = table;
        int hash = (int)(key ^ (key >>> 32));
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key == key) 
            {
                Object old = e.value;
                e.value = value;
                e.recordAccess(this);
                return old;
            }
        }

        // Creates the new entry.
        Entry e = new Entry(hash, key, value, tab[index]);
        tab[index] = e;
        e.addBefore(header);
        count++;

        if (count > maxSize)
        {
            //remove the eldest entry
            Entry eldest = header.after;
            return remove(eldest.key);
        }
        else
        {
            return null;
        }
    }

    public synchronized Object remove(long key) 
    {
        Entry tab[] = table;
        int hash = (int)(key ^ (key >>> 32));
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index], prev = null ; e != null ; prev = e, e = e.next) 
        {
            if ((e.hash == hash) && e.key == key) 
            {
                if (prev != null) 
                {
                    prev.next = e.next;
                } 
                else 
                {
                    tab[index] = e.next;
                }
                count--;
                Object oldValue = e.value;
                e.value = null;
                e.remove();
                return oldValue;
            }
        }
        return null;
    }

    public synchronized void clear() 
    {
        Entry tab[] = table;
        for (int index = tab.length; --index >= 0; )
            tab[index] = null;
        count = 0;
        header.before = header.after = header;
    }

    public int getMaxSize()
    {
        return maxSize;
    }

    public synchronized void putAll(LongIntLRUCache cache)
    {
        LongIntVector keys = cache.keys();
        for (int i=0; i<keys.size(); i++)
        {
            long key = keys.elementAt(i);
            put(key, cache.get(key));
        }
    }

    private static class Entry
    {
        int hash;
        long key;
        Object value;
        Entry next, before, after;

        protected Entry(int hash, long key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        protected Object clone() {
            return new Entry(hash, key, value,
                    (next==null ? null : (Entry)next.clone()));
        }

        public long getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            if (value == null)
                throw new NullPointerException();

            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) 
        {
            if (!(o instanceof Entry))
                return false;
            Entry e = (Entry)o;

            return key == e.getKey() &&
            (value==null ? e.getValue()==null : value.equals(e.getValue()));
        }

        public int hashCode() 
        {
            return hash ^ (value==null ? 0 : value.hashCode());
        }

        public String toString() 
        {
            return key+"="+value.toString();
        }
    
        private void remove() 
        {
            before.after = after;
            after.before = before;
        }

        private void addBefore(Entry existingEntry) 
        {
            after  = existingEntry;
            before = existingEntry.before;
            before.after = this;
            after.before = this;
        }
    
        void recordAccess(LongIntLRUCache cache) 
        {
            remove();
            addBefore(cache.header);
        }
    }
}

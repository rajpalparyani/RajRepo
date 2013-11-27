package com.telenav.cache;

import java.util.Vector;

public class LRUCache extends AbstractCache
{
    // Types of Enumerations/Iterations
    private static final int KEYS = 0;
    private static final int VALUES = 1;

    private Entry table[];

    private Entry header;
    
    private int count;

    private int maxSize; 

    public LRUCache(int maxSize) 
    {
        this.maxSize = maxSize;
        table = new Entry[maxSize];
        header = new Entry(-1, null, null, null);
        clear();
    }

    public synchronized int size() 
    {
        return count;
    }
    
    public synchronized void keys(Vector v)
    {
        Entry entry = header.after;
        while(entry != header)
        {
            v.addElement(entry.key);
            entry = entry.after;
        }
    }

    public synchronized Vector keys() 
    {
        Vector v = new Vector(count);
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

    public synchronized boolean containsKey(Object key) 
    {
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key.equals(key)) 
            {
                return true;
            }
        }
        return false;
    }

    public synchronized Object get(Object key) 
    {
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key.equals(key)) 
            {
                e.recordAccess(this);
                return e.value;
            }
        }
        return null;
    }

    public synchronized Object put(Object key, Object value) 
    {
        // Makes sure the key is not already in the hashtable.
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index] ; e != null ; e = e.next) 
        {
            if ((e.hash == hash) && e.key.equals(key)) 
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

    public synchronized Object remove(Object key) 
    {
        Entry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (Entry e = tab[index], prev = null ; e != null ; prev = e, e = e.next) 
        {
            if ((e.hash == hash) && e.key.equals(key)) 
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

    public synchronized void putAll(AbstractCache cache)
    {
        Vector keys = cache.keys();
        for (int i=0; i<keys.size(); i++)
        {
            Object key = keys.elementAt(i);
            put(key, cache.get(key));
        }
    }

    private static class Entry
    {
        int hash;
        Object key;
        Object value;
        Entry next, before, after;

        protected Entry(int hash, Object key, Object value, Entry next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        protected Object clone() {
            return new Entry(hash, key, value,
                    (next==null ? null : (Entry)next.clone()));
        }

        public Object getKey() {
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

            return (key==null ? e.getKey()==null : key.equals(e.getKey())) &&
            (value==null ? e.getValue()==null : value.equals(e.getValue()));
        }

        public int hashCode() 
        {
            return hash ^ (value==null ? 0 : value.hashCode());
        }

        public String toString() 
        {
            return key.toString()+"="+value.toString();
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
    
        void recordAccess(LRUCache cache) 
        {
            remove();
            addBefore(cache.header);
        }
    }

}

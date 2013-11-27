/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IRimPersistentContext.java
 *
 */
package com.telenav.persistent.rim;

import com.telenav.persistent.ITnPersistentContext;

/**
 * This class provider some necessary persistent information at rim platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 17, 2010
 */
public interface IRimPersistentContext extends ITnPersistentContext
{
    /**
     * persistent object is hashtable.
     */
    public final static int TYPE_HASHTABLE = 0;

    /**
     * persistent object is vector.
     */
    public final static int TYPE_VECTOR = 1;

    /**
     * create a persistent object.
     * 
     * @param persistentType the object's type.
     * @return a persistent object.
     */
    public IRimPersistentObject createPersistentObject(int persistentType);

    /**
     * A persistent object interface.
     * 
     * @author fqming
     *
     */
    public interface IRimPersistentObject
    {
        /**
         * Retrieve the persistent content.
         * 
         * @return a object
         */
        public Object getContents();

        /**
         * set the persistent content.
         * 
         * @param content a object
         */
        public void setContents(Object content);
    }
}

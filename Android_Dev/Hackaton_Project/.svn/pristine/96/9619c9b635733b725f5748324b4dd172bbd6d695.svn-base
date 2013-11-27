/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IJ2sePersistentContext.java
 *
 */
package com.telenav.persistent.j2se;

import java.io.Serializable;

import com.telenav.persistent.ITnPersistentContext;

/**
 * This class provider some necessary database information at j2se platform.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Aug 25, 2010
 */
public interface IJ2sePersistentContext extends ITnPersistentContext
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
    public IJ2sePersistentObject createPersistentObject(int persistentType);

    /**
     * A persistent object interface.
     * 
     * @author fqming
     *
     */
    public interface IJ2sePersistentObject extends Serializable
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

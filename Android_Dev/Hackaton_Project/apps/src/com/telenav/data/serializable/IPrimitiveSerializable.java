/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IPairSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.data.datatypes.primitive.BytesList;
import com.telenav.data.datatypes.primitive.StringList;
import com.telenav.data.datatypes.primitive.StringMap;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public interface IPrimitiveSerializable
{
    public StringList createStringList(byte[] data);
    
    public byte[] toBytes(StringList list);
    
    public BytesList createBytesList(byte[] data);
    
    public byte[] toBytes(BytesList list);
    
    public StringMap createStringMap(byte[] data);
    
    public byte[] toBytes(StringMap map);
}

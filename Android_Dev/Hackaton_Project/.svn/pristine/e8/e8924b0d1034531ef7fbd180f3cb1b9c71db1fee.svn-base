/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SerializableManager.java
 *
 */
package com.telenav.data.serializable;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-22
 */
public abstract class SerializableManager
{
    private static SerializableManager serializableManager;
    private static int initCount;
    
    public static SerializableManager getInstance()
    {
        return serializableManager;
    }
    
    public synchronized static void init(SerializableManager serializableMngr)
    {
        if(initCount >= 1)
            return;
        
        serializableManager = serializableMngr;
        initCount++;
    }
    
    public abstract IAddressSerializable getAddressSerializable();
    
    public abstract IPoiSerializable getPoiSerializable();
    
    public abstract IPrimitiveSerializable getPrimitiveSerializable();
    
    public abstract ILocationSerializable getLocationSerializable();
    
    public abstract IPreferenceSerializable getPreferenceSerializable();
    
    public abstract IMisLogSerializable getMisLogSerializable();
    
    public abstract IMandatorySerializable getMandatorySerializable();
    
    public abstract IExtraInfoSerializable getExtraInfoSerializable();
    
    public abstract IAndroidBillingSerializable getAndroidBillingSerializable();
    
    public abstract ILoginInfoSerializable getLoginInfoSerializable();
}

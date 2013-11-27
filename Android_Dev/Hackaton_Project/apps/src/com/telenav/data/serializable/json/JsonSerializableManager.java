/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * JsonSerializableManager.java
 *
 */
package com.telenav.data.serializable.json;

import com.telenav.data.serializable.IAddressSerializable;
import com.telenav.data.serializable.IAndroidBillingSerializable;
import com.telenav.data.serializable.IExtraInfoSerializable;
import com.telenav.data.serializable.ILocationSerializable;
import com.telenav.data.serializable.ILoginInfoSerializable;
import com.telenav.data.serializable.IMandatorySerializable;
import com.telenav.data.serializable.IMisLogSerializable;
import com.telenav.data.serializable.IPoiSerializable;
import com.telenav.data.serializable.IPreferenceSerializable;
import com.telenav.data.serializable.IPrimitiveSerializable;
import com.telenav.data.serializable.SerializableManager;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2011-1-25
 */
public class JsonSerializableManager extends SerializableManager
{
    private static JsonSerializableManager instance = new JsonSerializableManager();
    
    public static JsonSerializableManager getJsonInstance()
    {
        return instance;
    }
    
    public IAddressSerializable getAddressSerializable()
    {
        return new JsonAddressSerializable();
    }

    public ILocationSerializable getLocationSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public IMandatorySerializable getMandatorySerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public IMisLogSerializable getMisLogSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public IPoiSerializable getPoiSerializable()
    {
        return new JsonPoiSerializable();
    }

    public IPreferenceSerializable getPreferenceSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public IPrimitiveSerializable getPrimitiveSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public IExtraInfoSerializable getExtraInfoSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public IAndroidBillingSerializable getAndroidBillingSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ILoginInfoSerializable getLoginInfoSerializable()
    {
        // TODO Auto-generated method stub
        return null;
    }
}

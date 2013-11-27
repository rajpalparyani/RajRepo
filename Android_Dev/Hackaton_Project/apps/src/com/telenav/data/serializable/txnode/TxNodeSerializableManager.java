/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * SerializableFactory.java
 *
 */
package com.telenav.data.serializable.txnode;

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
 * @author fqming (fqming@telenav.cn)
 * @date 2010-12-22
 */
public class TxNodeSerializableManager extends SerializableManager
{
    private IAddressSerializable addressSerializable;

    private IPoiSerializable poiSerializable;

    private IPrimitiveSerializable primitiveSerializable;

    private ILocationSerializable locationSerializable;

    private IPreferenceSerializable preferenceSerializable;

    private IMisLogSerializable misLogSerializable;

    private IMandatorySerializable mandatorySerializable;

    private IExtraInfoSerializable extraInfoSerializable;

    private IAndroidBillingSerializable androidBillingSerializable;

    private ILoginInfoSerializable loginInfoSerializable;

    public IAddressSerializable getAddressSerializable()
    {
        if (addressSerializable == null)
        {
            addressSerializable = new TxNodeAddressSerializable();
        }

        return addressSerializable;
    }

    public IPoiSerializable getPoiSerializable()
    {
        if (poiSerializable == null)
        {
            poiSerializable = new TxNodePoiSerializable();
        }

        return poiSerializable;
    }

    public IPrimitiveSerializable getPrimitiveSerializable()
    {
        if (primitiveSerializable == null)
        {
            primitiveSerializable = new TxNodePrimitiveSerializable();
        }

        return primitiveSerializable;
    }

    public ILocationSerializable getLocationSerializable()
    {
        if (locationSerializable == null)
        {
            locationSerializable = new TxNodeLocationSerializable();
        }

        return locationSerializable;
    }

    public IPreferenceSerializable getPreferenceSerializable()
    {
        if (preferenceSerializable == null)
        {
            preferenceSerializable = new TxNodePreferenceSerializable();
        }

        return preferenceSerializable;
    }

    public IMisLogSerializable getMisLogSerializable()
    {
        if (misLogSerializable == null)
        {
            misLogSerializable = new TxNodeMisLogSerializable();
        }

        return misLogSerializable;
    }

    public IMandatorySerializable getMandatorySerializable()
    {
        if (mandatorySerializable == null)
        {
            mandatorySerializable = new TxNodeMandatorySerializable();
        }

        return mandatorySerializable;
    }

    public IExtraInfoSerializable getExtraInfoSerializable()
    {
        if (extraInfoSerializable == null)
        {
            extraInfoSerializable = new TxNodeExtraInfoSerializable();
        }

        return extraInfoSerializable;
    }

    public IAndroidBillingSerializable getAndroidBillingSerializable()
    {
        if (androidBillingSerializable == null)
        {
            androidBillingSerializable = new TxNodeAndroidBillingSerializable();
        }
        return androidBillingSerializable;
    }

    public ILoginInfoSerializable getLoginInfoSerializable()
    {
        if (loginInfoSerializable == null)
        {
            loginInfoSerializable = new TxNodeLoginInfoSerializable();
        }

        return loginInfoSerializable;
    }
}

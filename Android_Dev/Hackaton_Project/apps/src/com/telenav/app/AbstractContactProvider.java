/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractContactProvider.java
 *
 */
package com.telenav.app;

import java.util.Vector;

import android.graphics.Bitmap;

import com.telenav.data.datatypes.address.Stop;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-24
 */
public abstract class AbstractContactProvider
{
    public interface IContactProviderCallback
    {
        public void onError(String errorMessage);

        public void onResult(Vector results);
    }

    public static class TnContact implements java.io.Serializable
    {
        public final static int ADDRESS_HOME = 0;

        public final static int ADDRESS_OFFICE = 1;

        public final static int TYPE_MOBILE = 0;

        public final static int TYPE_WORK = 1;

        public final static int TYPE_HOME = 2;

        public final static int TYPE_FAX_WORK = 3;

        public final static int TYPE_FAX_HOME = 4;

        public final static int TYPE_OTHER = 5;

        public static final int TYPE_ASSISTANT = 6;

        public static final int TYPE_CALLBACK = 7;

        public static final int TYPE_CAR = 8;

        public static final int TYPE_COMPANY_MAIN = 9;

        public static final int TYPE_ISDN = 10;

        public static final int TYPE_MAIN = 11;

        public static final int TYPE_MMS = 12;

        public static final int TYPE_OTHER_FAX = 13;

        public static final int TYPE_PAGER = 14;

        public static final int TYPE_RADIO = 15;

        public static final int TYPE_TELEX = 16;

        public static final int TYPE_TTY_TDD = 17;

        public static final int TYPE_WORK_MOBILE = 18;

        public static final int TYPE_WORK_PAGER = 19;
        
        public String id;

        public int addressType;

        public String address;

        public int phoneNumberType;

        public String phoneNumber;
        
        public String name;
        
        public String country;
        
        public String phoneNumberCategory;

        public Stop stop;
        
        public Bitmap b;

        @Override
        public boolean equals(Object o)
        {
            if (o instanceof TnContact)
            {
                return ((TnContact)o).name.equals(this.name) && ((TnContact)o).phoneNumber.equals(this.phoneNumber);
            }
            return false;
        }
        
        
    }

    private static AbstractContactProvider instance;

    public static AbstractContactProvider getInstance()
    {
        return instance;
    }

    public static void init(AbstractContactProvider p)
    {
        instance = p;
    }

    public abstract void lookup(IContactProviderCallback callback);
}

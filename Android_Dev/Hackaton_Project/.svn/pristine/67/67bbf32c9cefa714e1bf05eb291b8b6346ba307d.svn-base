/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * ContactUtil.java
 *
 */
package com.telenav.module.dwf;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.android.AndroidPersistentContext;

/**
 *@author fangquanm
 *@modifier jpwang
 *@date Jul 2, 2013
 */
public class ContactUtil
{
    private final static int LIMIT = 50;
    
    public static boolean isNumeric(String str)
    {
        if(str == null || str.trim().length() == 0)
            return false;
        
        return str.matches("[+-]?\\d*(\\.\\d+)?");
    }
    
    public static void trimPhone(TnContact contact)
    {
        contact.phoneNumber = contact.phoneNumber.replace("(", "");
        contact.phoneNumber = contact.phoneNumber.replace(")", "");
        contact.phoneNumber = contact.phoneNumber.replace("-", "");
        contact.phoneNumber = contact.phoneNumber.replace(" ", "");
    }
    
    public static ArrayList<TnContact> retreiveContact(String keyword)
    {
        String[] projection = new String[]
        { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String selection = "" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? OR "
                + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
        String arg = "%" + keyword + "%";
        String phoneArg = arg;
        if(isNumeric(keyword))
        {
            if(keyword.length() > 3 && keyword.length() <= 6)
            {
                phoneArg = "%" + keyword.substring(0, 3) + "%" + keyword.substring(3) + "%";
            }
            if( keyword.length() > 6)
            {
                phoneArg = "%" + keyword.substring(0, 3) + "%" + keyword.substring(3, 6) + "%" + keyword.substring(6) + "%";
            }
        }
        String[] selectionArgs = new String[]
        { arg, phoneArg };
        Cursor cursor = AndroidPersistentContext
                .getInstance()
                .getContext()
                .getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, selectionArgs,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " LIMIT " + LIMIT); // Per PRD, 50 suggestions should be display after entry letters.

        TnContact tnContact = null;
        ArrayList<TnContact> vContacts = new ArrayList<TnContact>();
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                tnContact = new TnContact();
                tnContact.id = cursor.getString(0);
                tnContact.phoneNumberType = cursor.getInt(1);
                tnContact.phoneNumber = cursor.getString(2);
                tnContact.name = cursor.getString(3);
                if (!vContacts.contains(tnContact))
                {
                    vContacts.add(tnContact);
                }
            }
        }
        cursor.close();
        
        if (LIMIT - vContacts.size() > 0)
        {
            retriveContactFromMail(keyword, vContacts, LIMIT - vContacts.size());
        }

        return vContacts;
    }
    
    private static void retriveContactFromMail(String keyword, ArrayList<TnContact> contacts, int limit)
    {
        String[] projection = new String[]
        { ContactsContract.CommonDataKinds.Email.CONTACT_ID};
        String selection = "" + ContactsContract.CommonDataKinds.Email.ADDRESS + " LIKE ?";
        String phoneArg = "%" + keyword + "%";
        String[] selectionArgs = new String[]
        { phoneArg };
        Cursor cursor = AndroidPersistentContext
                .getInstance()
                .getContext()
                .getContentResolver()
                .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, selection, selectionArgs,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID);
        if (cursor == null || cursor.getCount() == 0)
        {
            return;
        }
        StringBuffer selection2 = new StringBuffer();
        while (cursor.moveToNext())
        {
            selection2.append(ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + cursor.getString(0) + " OR ");
        }
        cursor.close();

        String[] projection2 = new String[]
                { ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        
        Cursor cursor2 = AndroidPersistentContext
                .getInstance()
                .getContext()
                .getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection2, selection2.substring(0,  selection2.length() - 4), null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " LIMIT " + limit);
        TnContact tnContact = null;
        if (cursor2 != null)
        {
            while (cursor2.moveToNext())
            {
                tnContact = new TnContact();
                tnContact.id = cursor2.getString(0);
                tnContact.phoneNumberType = cursor2.getInt(1);
                tnContact.phoneNumber = cursor2.getString(2);
                tnContact.name = cursor2.getString(3);
                if (!contacts.contains(tnContact))
                {
                    contacts.add(tnContact);
                }
            }
        }
        cursor2.close();
    }
    
    public static Bitmap loadContactPhoto(long id, Context ct)
    {
        ContentResolver cr = ct.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null)
        {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }
    
    public static Bitmap loadContaBitmapByPhoneNumber(Context ct, String phoneNumber)
    {
        ContentResolver cr = ct.getContentResolver();
        String[] projection = new String[]
        { ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

        String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
        String[] selectionArgs = new String[]
        { phoneNumber };
        Cursor cu = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, selectionArgs,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        if (cu.moveToNext())
        {
            long id = cu.getLong(0);
            return loadContactPhoto(id, ct);
        }
        return null;
    }
    
    public static int getTypeLabelResource(int type)
    {
        return ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type);
    }
}

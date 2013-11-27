/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * AccountFetcher.java
 *
 */
package com.telenav.module.login;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * @author ziweiz
 * @date Mar 6, 2013 This class is use to get the primary email address, user name of the current user. need follwing
 *       permission: <uses-permission android:name="android.permission.READ_PROFILE" /> <uses-permission
 *       android:name="android.permission.READ_CONTACTS" /> <uses-permission
 *       android:name="android.permission.GET_ACCOUNTS" />
 */

public class AccountFetcher
{

    private final static int ICE_CREAM_SANDWICH = 14;

    public static String getEmail(Context context)
    {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        if (account == null)
        {
            return null;
        }
        else
        {
            return account.name;
        }
    }

    public static String getUserName(Context context)
    {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        if (osVersion >= ICE_CREAM_SANDWICH)
        {
            try
            {
                Cursor cursor = context.getContentResolver()
                        .query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                int count = cursor.getCount();
                String[] columnNames = cursor.getColumnNames();
                cursor.moveToFirst();
                int position = cursor.getPosition();
                if (count == 1 && position == 0)
                {
                    for (int j = 0; j < columnNames.length; j++)
                    {
                        String columnName = columnNames[j];
                        if ("display_name".equalsIgnoreCase(columnName))
                        {
                            String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
                            cursor.close();
                            return columnValue;
                        }
                    }
                }
            }
            catch (Exception e)
            {

            }
            return null;
        }
        else
        {
            return null;
        }
    }

    public static byte[] getUserPhoto(Context context)
    {
        int osVersion = android.os.Build.VERSION.SDK_INT;
        if (osVersion >= ICE_CREAM_SANDWICH)
        {
            try
            {
                Cursor cursor = context.getContentResolver()
                        .query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
                int count = cursor.getCount();
                String[] columnNames = cursor.getColumnNames();
                cursor.moveToFirst();
                int position = cursor.getPosition();
                if (count == 1 && position == 0)
                {
                    for (int j = 0; j < columnNames.length; j++)
                    {
                        String columnName = columnNames[j];
                        if ("photo_thumb_uri".equalsIgnoreCase(columnName))
                        {
                            if (cursor.getColumnIndex(columnName) < 0 || cursor.getString(cursor.getColumnIndex(columnName)) == null)
                            {
                                break;
                            }
                            Uri photoUri = Uri.parse(cursor.getString(cursor.getColumnIndex(columnName)));

                            Cursor c = context.getContentResolver().query(photoUri, new String[]
                            { ContactsContract.CommonDataKinds.Photo.PHOTO }, null, null, null);
                            try
                            {
                                if (c == null || !c.moveToNext())
                                {
                                    return null;
                                }
                                byte[] data = c.getBlob(0);
                                if (data == null)
                                {
                                    return null;
                                }

                                Bitmap b = BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                stream.close();

                                return byteArray;
                            }
                            finally
                            {
                                if (c != null)
                                {
                                    c.close();
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        else
        {
            return null;
        }
    }

    private static Account getAccount(AccountManager accountManager)
    {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0)
        {
            account = accounts[0];
        }
        else
        {
            account = null;
        }
        return account;
    }
}

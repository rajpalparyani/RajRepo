/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidContactProvider.java
 *
 */
package com.telenav.app.android;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.telenav.app.AbstractContactProvider;
import com.telenav.app.TeleNavDelegate;
import com.telenav.tnui.core.android.AndroidActivity;
import com.telenav.tnui.core.android.AndroidActivity.IAndroidActivityResultCallback;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-11-24
 */
public class AndroidContactProvider extends AbstractContactProvider implements IAndroidActivityResultCallback
{
    private IContactProviderCallback callback;
    
    public void lookup(IContactProviderCallback callback)
    {
        this.callback = callback;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        AndroidActivity androidActivity = (AndroidActivity)AndroidPersistentContext.getInstance().getContext();
        androidActivity.setActivityResultCallback(this);
        androidActivity.startActivityForResult(intent, TeleNavDelegate.LAUNCH_CONTACT_REQUESTCODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try
        {
            if(data == null)
            {
                this.callback.onResult(null);
                return;
            }

            AndroidActivity androidActivity = (AndroidActivity)AndroidPersistentContext.getInstance().getContext();
            this.callback.onResult(getContacts(androidActivity, data));
        }
        catch (Throwable e)
        {
            this.callback.onError(e.getMessage());
        }
        finally
        {
            this.callback = null;
        }
    }

    public Vector getContacts(Activity androidActivity, Intent intent) throws Exception
    {
        String[] results = getPeopleAttrs(androidActivity, intent);

        Vector contacts = new Vector();
        Cursor addressCursor = null;
        try
        {
            String[] addressProjection = new String[]
            { ContactsContract.CommonDataKinds.StructuredPostal.DATA, ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY};
            addressCursor = androidActivity.getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                addressProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + results[0], null, null);

            if (addressCursor != null)
            {
                if (addressCursor.moveToFirst())
                {
                    int addressColumn = addressCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.DATA);
                    int addressTypeColumn = addressCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.TYPE);
                    int countryColumn = addressCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY);

                    do
                    {
                        if (addressColumn != -1 || addressTypeColumn != -1)
                        {
                            TnContact contact = new TnContact();
                            String address = addressCursor.getString(addressColumn);
                            int addressType = addressCursor.getInt(addressTypeColumn);
                            String country = addressCursor.getString(countryColumn);

                            contact.id = results[0];
                            contact.addressType = convertAddressType(addressType);
                            contact.address = address;
                            contact.name = results[1];
                            contact.country = country;

                            boolean needAdded = true;
                            for(int i = 0; i < contacts.size(); i++)
                            {
                                TnContact existContact = (TnContact)contacts.elementAt(i);
                                if(existContact.address != null && existContact.address.equals(address))
                                {
                                    needAdded = false;
                                    break;
                                }
                            }
                            if(needAdded)
                            {
                                contacts.add(contact);
                            }
                        }
                    } while (addressCursor.moveToNext());
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (addressCursor != null)
            {
                addressCursor.close();
                addressCursor = null;
            }
        }

        Cursor phoneCursor = null;
        try
        {
            String[] phoneProjection = new String[]
            { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.LABEL };
            phoneCursor = androidActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, phoneProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + results[0], null, null);

            if (phoneCursor != null)
            {
                if (phoneCursor.moveToFirst())
                {
                    int numberColumn = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeColumn = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int labelColumn = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL);

                    do
                    {
                        if (numberColumn != -1 || typeColumn != -1 || labelColumn != -1)
                        {
                            TnContact contact = new TnContact();

                            String number = phoneCursor.getString(numberColumn);
                            if (number != null)
                            {
                            	number = removeDashOfNumber(number);
                            }
                            
                            int phoneType = phoneCursor.getInt(typeColumn);
                            contact.id = results[0];
                            contact.phoneNumberType = convertPhoneType(phoneType);
                            contact.phoneNumber = number;
                            contact.name = results[1];
                            contact.phoneNumberCategory = phoneCursor.getString(labelColumn);
                            if (contact.phoneNumberCategory == null || contact.phoneNumberCategory.trim().length() < 1)
                            {
                                CharSequence label = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                                    androidActivity.getResources(), phoneType, "");
                                if (label != null)
                                {
                                    contact.phoneNumberCategory = label.toString();
                                }
                                else
                                {
                                    contact.phoneNumberCategory = "";
                                }
                            }
                            
                            boolean needAdded = true;
                            for(int i = 0; i < contacts.size(); i++)
                            {
                                TnContact existContact = (TnContact)contacts.elementAt(i);
                                if(existContact.phoneNumber != null && existContact.phoneNumber.equals(number))
                                {
                                    needAdded = false;
                                    break;
                                }
                            }
                            if(needAdded)
                            {
                                contacts.add(contact);
                            }
                        }
                    } while (phoneCursor.moveToNext());
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (phoneCursor != null)
            {
                phoneCursor.close();
                phoneCursor = null;
            }
        }

        return contacts;
    }
    
    private String[] getPeopleAttrs(Activity androidActivity, Intent intent)
    {
        String[] peopleProjection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        String[] results = null;
        
        Cursor peopleCursor = null;
        try
        {
            peopleCursor = androidActivity.getContentResolver().query(intent.getData(), peopleProjection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
            if (peopleCursor != null)
            {
                if (peopleCursor.moveToFirst())
                {
                    int idColumn = peopleCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                    int nameColumn = peopleCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);

                    String peopleId = "";
                    String displayName = "";
                    if (idColumn != -1)
                    {
                        peopleId = peopleCursor.getString(idColumn);
                    }
                    if (nameColumn != -1)
                    {
                        displayName = peopleCursor.getString(nameColumn);
                    }
                    
                    results = new String[]{peopleId, displayName};
                }
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            if (peopleCursor != null)
                peopleCursor.close();
        }
        
        return results;
    }
    
    private String removeDashOfNumber(String phoneNumber)
    {
        return phoneNumber.replaceAll("-", "");
    }
    
    private int convertAddressType(int nativeType)
    {
        switch(nativeType)
        {
            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME:
                return TnContact.ADDRESS_HOME;
            case ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK:
                return TnContact.ADDRESS_OFFICE;
        }
        
        return TnContact.TYPE_OTHER;
    }
    
    private int convertPhoneType(int nativeType)
    {
        switch (nativeType)
        {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return TnContact.TYPE_MOBILE;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return TnContact.TYPE_WORK;
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return TnContact.TYPE_HOME;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                return TnContact.TYPE_FAX_WORK;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                return TnContact.TYPE_FAX_HOME;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                return TnContact.TYPE_OTHER;
            case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                return TnContact.TYPE_ASSISTANT;
            case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                return TnContact.TYPE_CALLBACK;
            case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                return TnContact.TYPE_COMPANY_MAIN;
            case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                return TnContact.TYPE_CAR;
            case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                return TnContact.TYPE_ISDN;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                return TnContact.TYPE_MAIN;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                return TnContact.TYPE_MMS;
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                return TnContact.TYPE_OTHER_FAX;
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                return TnContact.TYPE_PAGER;
            case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                return TnContact.TYPE_RADIO;
            case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                return TnContact.TYPE_TELEX;
            case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                return TnContact.TYPE_TTY_TDD;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                return TnContact.TYPE_WORK_MOBILE;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                return TnContact.TYPE_WORK_PAGER;
        }
        
        return TnContact.TYPE_OTHER;
    }
}

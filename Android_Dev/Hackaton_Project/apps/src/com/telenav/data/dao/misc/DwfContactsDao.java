/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfContactsDao.java
 *
 */
package com.telenav.data.dao.misc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.data.dao.AbstractDao;
import com.telenav.logger.Logger;

/**
 * @author fangquanm
 * @date Jul 3, 2013
 */
public class DwfContactsDao extends AbstractDao
{
    public static class Group implements Comparable<Group>
    {
        public String groupName;

        public ArrayList<TnContact> contacts;
        
        long time;
        
        public boolean equals(Object o)
        {
            if(o instanceof Group)
            {
                boolean isEqual = true;
                Group g = (Group)o;
                if(this.contacts.size() == g.contacts.size())
                {
                    for(int i = 0; i < this.contacts.size(); i++)
                    {
                        if(!this.contacts.get(i).phoneNumber.equals(g.contacts.get(i).phoneNumber))
                        {
                            isEqual = false;
                            break;
                        }
                    }
                }
                else
                {
                    isEqual = false;
                }
                
                return isEqual;
            }
            return false;
        }

        @Override
        public int compareTo(Group another)
        {
            Group g = (Group) another;
            if (this.time > g.time)
                return 1;
            else if (this.time < g.time)
                return -1;

            return 0;
        }
    }

    private String name;

    private ArrayList<Group> groups = new ArrayList<Group>();

    public DwfContactsDao(String name)
    {
        this.name = name;

        load();
    }
    
    public ArrayList<Group> getGroups()
    {
        return this.groups;
    }

    public void addGroup(Group group)
    {
        if (group == null || group.contacts == null || group.contacts.isEmpty())
            return;

        Group sameGroup = null;
        for (Group g : groups)
        {
            if (g.equals(group))
            {
                sameGroup = g;
                break;
            }
        }

        group.time = System.currentTimeMillis();

        if (sameGroup != null)
        {
            groups.remove(sameGroup);
        }

        groups.add(0, group);
        
        if(groups.size() > 10)
        {
            groups.remove(groups.size() - 1);
        }
    }

    @Override
    public void store()
    {
        if(this.groups.isEmpty())
            return;
        
        ArrayList<String> data = new ArrayList<String>();
        for(Group g : groups)
        {
            try
            {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("group", "");
                
                JSONArray array = new JSONArray();
                for(TnContact c : g.contacts)
                {
                    JSONObject elementJson = new JSONObject();
                    elementJson.put("id", c.id);
                    elementJson.put("name", c.name);
                    elementJson.put("phoneNumber", c.phoneNumber);
                    if(c.b != null)
                    {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        c.b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.close();
                        
                        elementJson.put("image", Base64.encodeToString(byteArray, Base64.DEFAULT));
                    }
                    array.put(elementJson);
                }
                jsonObj.put("elements", array);
                
                data.add(jsonObj.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        storeObject(AndroidPersistentContext.getInstance().getContext(), this.name, data);
    }

    @Override
    public void clear()
    {
        File f = new File(AndroidPersistentContext.getInstance().getContext().getFilesDir().getAbsolutePath() + "/" + name);
        if(f.exists())
        {
           boolean isDeleted = f.delete();
           if(!isDeleted)
           {
               Logger.log(Logger.WARNING, this.getClass().getName(), "name can't be deleted.");
           }
        }
    }

    @SuppressWarnings("unchecked")
    void load()
    {
        ArrayList<String> data = (ArrayList<String>) loadObject(AndroidPersistentContext.getInstance().getContext(), this.name);

        if (data != null)
        {
            for (String s : data)
            {
                try
                {

                    JSONObject jsonObj = new JSONObject(s);

                    String groupName = jsonObj.getString("group");
                    JSONArray groupElements = jsonObj.getJSONArray("elements");

                    Group group = new Group();
                    group.groupName = groupName;
                    group.contacts = new ArrayList<TnContact>();

                    for (int j = 0; j < groupElements.length(); j++)
                    {
                        TnContact contact = new TnContact();
                        JSONObject elementJson = groupElements.getJSONObject(j);
                        contact.id = elementJson.has("id") ? elementJson.getString("id") : "";
                        contact.name = elementJson.getString("name");
                        contact.phoneNumber = elementJson.getString("phoneNumber");
                        if (elementJson.has("image"))
                        {
                            byte[] bitmap = Base64.decode(elementJson.getString("image"), Base64.DEFAULT);
                            contact.b = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
                        }

                        group.contacts.add(contact);
                    }

                    groups.add(group);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                
                Collections.sort(groups);
            }
        }
    }
    
    void storeObject(Context context, String serializeName, Object serializeObj)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            File f = new File(context.getFilesDir().getAbsolutePath() + "/" + serializeName);

            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(serializeObj);
        }
        catch (Throwable e)
        {
            // don't need this log, for that currently there is not a easy way to check the file if exist in the system.
            // Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (oos != null)
                    oos.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
    }

    Object loadObject(Context context, String serializeName)
    {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            File f = new File(context.getFilesDir().getAbsolutePath() + "/" + serializeName);

            fis = new FileInputStream(f);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            return obj;
        }
        catch (Throwable e)
        {
            // don't need this log, for that currently there is not a easy way to check the file if exist in the system.
            // Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (ois != null)
                    ois.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        return null;
    }
}

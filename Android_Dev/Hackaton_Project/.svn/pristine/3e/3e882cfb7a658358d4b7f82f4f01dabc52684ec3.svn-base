/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidFilePersistentUtil.java
 *
 */
package com.telenav.persistent.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import android.content.Context;

import com.telenav.logger.Logger;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-2
 */
class AndroidFilePersistentUtil
{
    static void mkDir(Context context, String name)
    {
        File f = new File(context.getFilesDir().getAbsolutePath() + "/" + name);
        if(!f.exists())
        {
            boolean isMkdir = f.mkdir();
            if(!isMkdir)
            {
                Logger.log(Logger.WARNING, AndroidFilePersistentUtil.class.getName(), "The directory of name can't be made.");
            }
        }
    }
    
    static void saveData(Context context, String name, byte[] data)
    {
        FileOutputStream fos = null;
        
        try
        {
            File f = new File(context.getFilesDir().getAbsolutePath() + "/" + name);
            if (!f.exists())
            {
                int index = name.lastIndexOf('/');
                if (index != -1)
                {
                    String path = name.substring(0, index);
                    File dir = new File (context.getFilesDir().getAbsolutePath() + "/" + path);
                    if (! dir.exists())
                    {
                        boolean isMkDir = dir.mkdirs();
                        if(!isMkDir)
                        {
                            return;
                        }
                    }
                }
                
                boolean isNew = f.createNewFile();
                if(!isNew)
                {
                    return;
                }
            }

            fos = new FileOutputStream(f);
            fos.write(data);
        }
        catch(Throwable e)
        {
            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e)
            {
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
    }
    
    static byte[] readData(Context context, String name)
    {
        FileInputStream fis = null;
        byte[] data = null;
        try
        {
            File f = new File(context.getFilesDir().getAbsolutePath() + "/" + name);
            if(f.exists())
            {
                fis = new FileInputStream(f);
                int size = (int)f.length();
                data = new byte[size];
                fis.read(data);
            }
        }
        catch(Throwable e)
        {
            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fis != null)
                {
                    fis.close();
                }
            }
            catch (Exception e)
            {
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
        
        return data;
    }
    
    static void deleteData(Context context, String name)
    {
        File f = new File(context.getFilesDir().getAbsolutePath() + "/" + name);
        if(f.exists())
        {
           boolean isDeleted = f.delete();
           if(!isDeleted)
           {
               Logger.log(Logger.WARNING, AndroidFilePersistentUtil.class.getName(), "name can't be deleted.");
           }
        }
    }
    
    static long measureData(Context context, String name)
    {
        File f = new File(context.getFilesDir().getAbsolutePath() + "/" + name);
        if(f.exists())
        {
            return f.length();
        }
        return 0;
    }
    
    static void saveIndexes(Context context, Vector v, String indexKey)
    {
        if(v == null)
            return;
        
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(indexKey, Context.MODE_PRIVATE);
            
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < v.size(); i++)
            {
                String tmpKey = (String)v.elementAt(i);
                sb.append(tmpKey + ",");
            }
            
            fos.write(sb.toString().getBytes());
        }
        catch(Throwable e)
        {
            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e)
            {
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
    }
    
    static Vector readIndexes(Context context, String indexKey)
    {
        Vector v = new Vector();
        FileInputStream fis = null;
        try
        {
            File f = new File(context.getFilesDir().getAbsolutePath() + "/" + indexKey);
            
            if(f.exists())
            {
                fis = new FileInputStream(f);
                byte[] buffer = new byte[(int)f.length()];
                fis.read(buffer);
                StringTokenizer st = new StringTokenizer(new String(buffer), ",");
                while(st.hasMoreTokens())
                {
                    String key = st.nextToken();
                    if(key.length() > 0)
                    {
                        v.addElement(key);
                    }
                }
            }
        }
        catch(Throwable e)
        {
//            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
        }
        finally
        {
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (Exception e)
            {
                //don't need this log, for that currently there is not a easy way to check the file if exist in the system.
//                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
        
        return v;
    }
    
    static void storeObject(Context context, String serializeName, Object serializeObj)
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
            //don't need this log, for that currently there is not a easy way to check the file if exist in the system.
//            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
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
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
            try
            {
                if (fos != null)
                    fos.close();
            }
            catch (Exception e)
            {
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
    }
    
    static Object loadObject(Context context, String serializeName)
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
        catch(Throwable e)
        {
            //don't need this log, for that currently there is not a easy way to check the file if exist in the system.
//            Logger.log(AndroidFilePersistentUtil.class.getName(), e);
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
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
            try
            {
                if (fis != null)
                    fis.close();
            }
            catch (Exception e)
            {
                Logger.log(AndroidFilePersistentUtil.class.getName(), e);
            }
        }
        return null;
    }
}

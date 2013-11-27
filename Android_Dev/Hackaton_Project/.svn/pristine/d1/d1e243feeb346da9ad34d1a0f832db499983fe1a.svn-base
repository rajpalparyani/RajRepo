/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * ResourceBundle.java
 *
 */
package com.telenav.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import com.telenav.io.TnIoManager;
import com.telenav.io.TnProperties;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnPersistentManager;
import com.telenav.persistent.TnStore;

/**
 * Resource bundles contain locale-specific objects. When your program needs a locale-specific resource, a String for
 * example, your program can load it from the resource bundle that is appropriate for the current user's locale. In this
 * way, you can write program code that is largely independent of the user's locale isolating most, if not all, of the
 * locale-specific information in resource bundles. <br />
 * <br />
 * The directory of i18n of the projects should be:<br/>
 * ***---i18n<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-en_US<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-audios<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-binaries<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-images<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-strings<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-generic<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-binaries<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-images<br />
 * <br />
 * In the generic directory, means that the resource is generic, not in any locale's group.
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 14, 2010
 */
public class ResourceBundle
{
    /**
     * the root's path.
     */
    final static String ROOT_PATH = "i18n";

    /**
     * the generic's path.
     */
    protected final static String GENERIC_PATH = "generic";

    /**
     * the text's path.
     */
    final static String TEXT_PATH = "strings";

    /**
     * the image's path.
     */
    final static String IMAGE_PATH = "images";

    /**
     * the audio's path.
     */
    final static String AUDIO_PATH = "audios";

    /**
     * the binary's path.
     */
    final static String BINARY_PATH = "binaries";

    /**
     * the name of persistent store.
     */
    final static int META_STORE_NAMES = 7000000;
    
    /**
     * current locale.
     */
    protected String locale;

    protected TnIoManager ioManager;

    protected TnPersistentManager persistentManager;
    
    protected PersistentResourceBundle persistentResourceBundle;

    protected Hashtable propertiesTable;

    protected int stringSource;
    
    protected int audioSource;
    
    protected int imageSource;
    
    protected int genericImageSource;
    
    protected int binarySource;
    
    protected int genericBinarySource;
    
    protected TnStore metaStore;
    
    /**
     * create a resource bundle object.
     * 
     * @param ioManager the io manager
     * @param persistentManager the persistent manager
     */
    public ResourceBundle(String locale, TnIoManager ioManager, TnPersistentManager persistentManager)
    {
        this.locale = locale;
        
        this.ioManager = ioManager;
        this.persistentManager = persistentManager;
        
        this.persistentResourceBundle = new PersistentResourceBundle(this);

        propertiesTable = new Hashtable();
        
        metaStore = this.persistentManager.createStore(TnPersistentManager.RMS_CRUMB, getMetaDataPath());
        metaStore.load();
    }
    
    /**
     * Retrieve current locale.
     * 
     * @return current locale.
     */
    public String getLocale()
    {
        return this.locale;
    }

    /**
     * Retrieve the persistent bundle to store the bundle's data into RMS.
     * 
     * @return persistent bundle object
     */
    public PersistentResourceBundle getPersistentBundle()
    {
        return this.persistentResourceBundle;
    }

    /**
     * Set current locale.
     * 
     * @param locale
     */
    public void setLocale(String locale)
    {
        if (locale == null || locale.trim().length() == 0)
        {
            throw new IllegalArgumentException("The locale is empty.");
        }

        boolean isLocaleChanged = !locale.equals(this.locale);

        this.locale = locale;

        if (isLocaleChanged)
        {
            this.propertiesTable.clear();
            
            this.persistentResourceBundle.clearCurrentLocaleData();
        }
    }
    
    /**
     * Retrieves string form of resource object by key. 
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @return String form of resource object.
     */
    public String getString(int key, String familyName)
    {
        if(familyName == null || familyName.trim().length() == 0)
        {
            throw new IllegalArgumentException("The family name is empty.");
        }
        
        String value = this.persistentResourceBundle.getString(key, familyName);
        
        if(value != null && value.length() > 0)
        {
            return value;
        }
        
        return getStringFromFile(key, familyName);
    }

    String getStringFromFile(int key, String familyName)
    {
        TnProperties properties = (TnProperties) propertiesTable.get(familyName);
        if (properties == null)
        {
            properties = this.ioManager.createProperties();

            InputStream is = null;
            try
            {
                is = this.ioManager.openFileFromAppBundle(getDataPath(TEXT_PATH, false) + familyName + ".properties");
                properties.load(is);

                propertiesTable.put(familyName, properties);
            }
            catch (IOException e)
            {
                Logger.log(this.getClass().getName(), e);

                return null;
            }
            finally
            {
                try
                {
                    if (is != null)
                    {
                        is.close();
                    }
                }
                catch (IOException e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    is = null;
                }
            }
        }

        return (String) properties.getProperty(key + "");
    }

    /**
     * Retrieves image form of resource object by key. 
     * 
     * @param key Key for searched-for resource object.
     * @param the file's name
     * @param familyName the group's name of the strings.
     * @return image form of resource object.
     */
    public byte[] getImage(String name, String familyName)
    {
        if(name == null || name.length() == 0)
            return null;
        
        byte[] value = this.persistentResourceBundle.getImage(name, familyName);
        
        if(value != null)
        {
            return value;
        }
        
        return getImageFromFile(name, familyName);
    }
    
    protected byte[] getImageFromFile(String name, String family)
    {
        byte[] data = this.ioManager.openFileBytesFromAppBundle(getDataPath(IMAGE_PATH, false)
                + (family == null || family.length() == 0 ? "" : family + "/") + name);

        return data;
    }

    /**
     * Retrieves image form of resource object by key. 
     * <br />
     * these images are generic, not in any locale group.
     * 
     * @param key Key for searched-for resource object.
     * @param the file's name
     * @param familyName the group's name of the strings.
     * @return image form of resource object.
     */
    public byte[] getGenericImage(String name, String familyName)
    {
        if(name == null || name.length() == 0)
            return null;
        
        byte[] value = this.persistentResourceBundle.getGenericImage(name, familyName);
        
        if(value != null)
        {
            return value;
        }
        
        return getGenericImageFromFile(name, familyName);
    }
    
    protected byte[] getGenericImageFromFile(String name, String family)
    {
        byte[] data = this.ioManager.openFileBytesFromAppBundle(getDataPath(IMAGE_PATH, true)
                + (family == null || family.length() == 0 ? "" : family + "/") + name);

        return data;
    }

    /**
     * Retrieves audio form of resource object by key. 
     * 
     * @param key Key for searched-for resource object.
     * @param familyName the group's name of the strings.
     * @return audio form of resource object.
     */
    public byte[] getAudio(int key, String familyName)
    {
        byte[] value = this.persistentResourceBundle.getAudio(key, familyName);
        
        if(value != null)
        {
            return value;
        }
        
        return getAudioFromFile(key, familyName);
    }
    
    byte[] getAudioFromFile(int key, String family)
    {
        byte[] data = this.ioManager.openFileBytesFromAppBundle(getDataPath(AUDIO_PATH, false)
                + (family == null || family.length() == 0 ? "" : family + "/") + key + "." + family);

        return data;
    }
    
    /**
     * Retrieves binary form of resource object by key. 
     * 
     * @param key Key for searched-for resource object.
     * @param the file's name
     * @param familyName the group's name of the strings.
     * @return binary form of resource object.
     */
    public byte[] getBinary(String name, String familyName)
    {
        byte[] value = this.persistentResourceBundle.getBinary(name, familyName);
        
        if(value != null)
        {
            return value;
        }
        
        return getBinaryFromFile(name, familyName);
    }

    byte[] getBinaryFromFile(String name, String family)
    {
        byte[] data = this.ioManager.openFileBytesFromAppBundle(getDataPath(BINARY_PATH, false)
                + (family == null || family.length() == 0 ? "" : family + "/") + name);

        return data;
    }
    
    /**
     * Retrieves binary form of resource object by key. 
     * <br />
     * these binaries are generic, not in any locale group.
     * 
     * @param key Key for searched-for resource object.
     * @param the file's name
     * @param familyName the group's name of the strings.
     * @return binary form of resource object.
     */
    public byte[] getGenericBinary(String name, String familyName)
    {
        byte[] value = this.persistentResourceBundle.getGenericBinary(name, familyName);
        
        if(value != null)
        {
            return value;
        }
        
        return getGenericBinaryFromFile(name, familyName);
    }
    
    byte[] getGenericBinaryFromFile(String name, String family)
    {
        byte[] data = this.ioManager.openFileBytesFromAppBundle(getDataPath(BINARY_PATH, true)
                + (family == null || family.length() == 0 ? "" : family + "/") + name);

        return data;
    }
    
    public String getDataPath(String dataPath, boolean isGeneric)
    {
        return ROOT_PATH + "/" + (isGeneric ? ResourceBundle.GENERIC_PATH : this.getLocale()) + "/" + dataPath + "/";
    }

    private String getMetaDataPath()
    {
        return ROOT_PATH + "_" + "meta"; 
    }
}

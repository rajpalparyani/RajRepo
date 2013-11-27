/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * Locale.java
 *
 */
package com.telenav.i18n;

/**
 * A Locale object represents a specific geographical, political, or cultural region. 
 * 
 *@author fqming (fqming@telenav.cn)
 *@date Jul 15, 2010
 */
public abstract class Locale
{
    /**
     * English (United States) locale.
     */
    public static final String en_US = "en_US";
    
    /**
     * English (Great Britain) locale.
     */
    public static final String en_GB = "en_GB";
    
    /**
     * English (Canada) locale.
     */
    public static final String en_CA = "en_CA";
    
    /**
     * Spanish (Spain) locale.
     */
    public static final String es_ES = "es_ES";
    
    /**
     * Spanish (Mexican) locale.
     */
    public static final String es_MX = "es_MX";
    
    /**
     * Spanish (Columbia??) locale.
     */
    public static final String es_CO = "es_CO";
    
    /**
     * French (France) locale.
     */
    public static final String fr_FR = "fr_FR";
    
    /**
     * French (Canadian) locale.
     */
    public static final String fr_CA = "fr_CA";
    
    /**
     * Portuguese (Portugal) locale.
     */
    public static final String pt_PT = "pt_PT";
    
    /**
     * Portuguese (Brazil) locale.
     */
    public static final String pt_BR = "pt_BR";
    
    /**
     * Germany (German) locale.
     */
    public static final String de_DE = "de_DE";
    
    /**
     * Italian (Italy) locale.
     */
    public static final String it_IT = "it_IT";
    
    /**
     * Dutch (Netherlands) locale.
     */
    public static final String nl_NL = "nl_NL";
    
    /**
     * Chinese (China) locale.
     */
    public static final String zh_CN = "zh_CN";
    
    /**
     * Get locale's UID by the name.
     * 
     * @param locale a locale's name
     * @return UID
     */
    public static int getLocaleId(String locale)
    {
        if(en_US.equals(locale))
        {
            return 10000;
        }
        else if(en_GB.equals(locale))
        {
            return 10001;
        }
        else if(es_ES.equals(locale))
        {
            return 10002;
        }
        else if(es_MX.equals(locale))
        {
            return 10003;
        }
        else if(es_CO.equals(locale))
        {
            return 10004;
        }
        else if(fr_FR.equals(locale))
        {
            return 10005;
        }
        else if(fr_CA.equals(locale))
        {
            return 10006;
        }
        else if(pt_PT.equals(locale))
        {
            return 10007;
        }
        else if(pt_BR.equals(locale))
        {
            return 10008;
        }
        else if(de_DE.equals(locale))
        {
            return 10009;
        }
        else if(it_IT.equals(locale))
        {
            return 10010;
        }
        else if(nl_NL.equals(locale))
        {
            return 10011;
        }
        else if(zh_CN.equals(locale))
        {
            return 10012;
        }
        else if(en_CA.equals(locale))
        {
            return 10013;
        }
        
        throw new IllegalArgumentException("unknown locale: " + locale);
    }
    
    /**
     * Get locale's name by locale's UID.
     * 
     * @param id locale's UID
     * @return locale's name
     */
    public static String getLocale(int id)
    {
        switch(id)
        {
            case 10000:
                return en_US;
            case 10001:
                return en_GB;
            case 10002:
                return es_ES;
            case 10003:
                return es_MX;
            case 10004:
                return es_CO;
            case 10005:
                return fr_FR;
            case 10006:
                return fr_CA;
            case 10007:
                return pt_PT;
            case 10008:
                return pt_BR;
            case 10009:
                return de_DE;
            case 10010:
                return it_IT;
            case 10011:
                return nl_NL;
            case 10012:
                return zh_CN;
            case 10013:
                return en_CA;
        }
        
        throw new IllegalArgumentException("unknown locale id: " + id);
    }
}

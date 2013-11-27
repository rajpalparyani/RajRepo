/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * TestLocale.java
 *
 */
package com.telenav.i18n;

import junit.framework.TestCase;

/**
 *@author yning
 *@date 2011-6-13
 */
public class LocaleTest extends TestCase
{
    public void testGetLocale()
    {
        assertEquals("en_US", Locale.getLocale(10000));
        assertEquals("en_GB", Locale.getLocale(10001));
        assertEquals("es_ES", Locale.getLocale(10002));
        assertEquals("es_MX", Locale.getLocale(10003));
        assertEquals("es_CO", Locale.getLocale(10004));
        assertEquals("fr_FR", Locale.getLocale(10005));
        assertEquals("fr_CA", Locale.getLocale(10006));
        assertEquals("pt_PT", Locale.getLocale(10007));
        assertEquals("pt_BR", Locale.getLocale(10008));
        assertEquals("de_DE", Locale.getLocale(10009));
        assertEquals("it_IT", Locale.getLocale(10010));
        assertEquals("nl_NL", Locale.getLocale(10011));
        assertEquals("zh_CN", Locale.getLocale(10012));
        assertEquals("en_CA", Locale.getLocale(10013));
        try
        {
            String locale = Locale.getLocale(10030);
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    
    public void testGetLocaleId()
    {
        assertEquals(10000, Locale.getLocaleId("en_US"));
        assertEquals(10001, Locale.getLocaleId("en_GB"));
        assertEquals(10002, Locale.getLocaleId("es_ES"));
        assertEquals(10003, Locale.getLocaleId("es_MX"));
        assertEquals(10004, Locale.getLocaleId("es_CO"));
        assertEquals(10005, Locale.getLocaleId("fr_FR"));
        assertEquals(10006, Locale.getLocaleId("fr_CA"));
        assertEquals(10007, Locale.getLocaleId("pt_PT"));
        assertEquals(10008, Locale.getLocaleId("pt_BR"));
        assertEquals(10009, Locale.getLocaleId("de_DE"));
        assertEquals(10010, Locale.getLocaleId("it_IT"));
        assertEquals(10011, Locale.getLocaleId("nl_NL"));
        assertEquals(10012, Locale.getLocaleId("zh_CN"));
        assertEquals(10013, Locale.getLocaleId("en_CA"));
        try
        {
            int id = Locale.getLocaleId("nothing");
        }
        catch(Exception e)
        {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}

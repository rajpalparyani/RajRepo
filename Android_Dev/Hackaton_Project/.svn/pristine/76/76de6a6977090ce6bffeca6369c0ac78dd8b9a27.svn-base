/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnUiArgs.java
 *
 */
package com.telenav.tnui.core;

import java.util.Enumeration;
import java.util.Hashtable;

import com.telenav.tnui.graphics.AbstractTnImage;


/**
 * The wrapper of some dynamic ui arguments.
 * <br />
 * For example: the width/height of one component at landscape/portrait mode. etc.
 * 
 *@author fqming (fqming@telenav.cn)
 *@date 2010-6-17
 */
public class TnUiArgs
{
    /**
     * Key of prefer width of the component when layout.
     */
    public final static int KEY_PREFER_WIDTH = -10000000;

    /**
     * Key of prefer height of the component when layout.
     */
    public final static int KEY_PREFER_HEIGHT = -10000001;
    
    /**
     * Key of x coordinate of the component when layout.
     */
    public final static int KEY_POSITION_X = -10000004;
    
    /**
     * Key of y coordinate of the component when layout.
     */
    public final static int KEY_POSITION_Y = -10000005;

    /**
     * Key of background image of the component for focus state.
     */
    public final static int KEY_BACKGROUND_IMAGE_FOCUS = -10000006;
    
    /**
     * Key of background image of the component for unfocus state.
     */
    public final static int KEY_BACKGROUND_IMAGE_UNFOCUS = -10000007;
    
    /**
     * Key of background image of the component for focus state.
     */
    public final static int KEY_TOP_SHADOW_IMAGE= -10000008;
    
    /**
     * Key of background image of the component for unfocus state.
     */
    public final static int KEY_BOTTOM_SHADOW_IMAGE = -10000009;
    
    /**
     * Key of background image of the component for unfocus state.
     */
    public final static int KEY_LEFT_SHADOW_IMAGE = -10000010;
    
    
    public final static int KEY_RIGHT_SHADOW_IMAGE = -10000011;
    
    private Hashtable args;

    /**
     * construct this class.
     */
    public TnUiArgs()
    {
        args = new Hashtable();
    }
    
    /**
     * clone the tn
     * @param tnUiArgs
     */
    public void copy(TnUiArgs tnUiArgs)
    {
        Enumeration keys = tnUiArgs.args.keys();
        while(keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            args.put(key, tnUiArgs.args.get(key));
        }
    }

    /**
     * put the arguments with special id and value.
     * 
     * @param key
     * @param uiArgAdapter
     */
    public void put(int key, TnUiArgAdapter uiArgAdapter)
    {
        if(uiArgAdapter == null)
            return;
        
        args.put(new Integer(key), uiArgAdapter);
    }
    
    /**
     * remove the adapter with specific key.
     * 
     * @param key key
     */
    public void remove(int key)
    {
        args.remove(new Integer(key));
    }
    
    /**
     * get the arguments.
     * 
     * @param key
     * @return the adapter
     */
    public TnUiArgAdapter get(int key)
    {
        return (TnUiArgAdapter)args.get(new Integer(key));
    }

    /**
     * The decorator of the value of the ui argument.
     * 
     * @author fqming
     *
     */
    public interface ITnUiArgsDecorator
    {
        /**
         * decorate the value according to the special argument's adapter.
         * 
         * @param args
         * @return decorated value
         */
        public Object decorate(TnUiArgAdapter args);
    }
    
    /**
     * The adapter of ui argument.
     * 
     * @author fqming
     *
     */
    public static class TnUiArgAdapter
    {
        private Object key;
        private ITnUiArgsDecorator uiArgsDecorator;
        
        /**
         * construct this adapter.
         * 
         * @param key
         * @param uiArgsDecorator
         */
        public TnUiArgAdapter(Object key, ITnUiArgsDecorator uiArgsDecorator)
        {
            this.key = key;
            this.uiArgsDecorator = uiArgsDecorator;
        }
        
        public boolean equals(Object o)
        {
            if(o instanceof TnUiArgAdapter)
            {
                return key.equals(((TnUiArgAdapter)o).key);
            }
            
            return this.equals(o);
        }
        
        public int hashCode()
        {
            return key.hashCode();
        }
        
        /**
         * Retrieve the key of this ui argument.
         * 
         * @return Object
         */
        public Object getKey()
        {
            return this.key;
        }
        
        /**
         * Retrieve the image of this ui argument.
         * 
         * @return {@link AbstractTnImage}
         */
        public AbstractTnImage getImage()
        {
            return (AbstractTnImage) this.uiArgsDecorator.decorate(this);
        }

        /**
         * Retrieve the int value of this ui argument.
         * 
         * @return int
         */
        public int getInt()
        {
            return ((Integer) this.uiArgsDecorator.decorate(this)).intValue();
        }
    }
}

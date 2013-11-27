/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * CitizenGallery.java
 *
 */
package com.telenav.ui.citizen;

import com.telenav.module.AppConfigHelper;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author xinrongl (xinrongl@telenav.com)
 * @date Apr 19, 2011
 */

public class CitizenGallery extends AbstractTnContainer implements IGestureListener
{
    public static final int CMD_PAGE_INDEX_INCREASE = 10001;
    public static final int CMD_PAGE_INDEX_DECREASE = 10002;
    public static final int CMD_PAGE_INDEX_REACH_END = 10003;
    
    public final static int SET_GALLERY_ADAPTER = 61000000;
    public final static int MOVE_TO_PREVIOUS    = 61000001;
    public final static int MOVE_TO_NEXT        = 61000002;
    public final static int ON_TOUCH_EVENT      = 61000003;
    public final static int SET_GALLERY_INDEX   = 61000004;
    public final static int GET_GALLERY_INDEX   = 61000005;

    private static final int ID_LEFT_SIDE_PLACEHOLDER  = -101;
    private static final int ID_RIGHT_SIDE_PLACEHOLER  = -102;
    private static final int ID_TOP_MARGIN_HEIGHT      = -103;

    private ICitizenGalleryAdapter adapter;    
    
    private AbstractTnComponent[] galleries;
    
    public CitizenGallery(int id, int selectedIndex)
    {
        super(id);
        
        this.bind();
        
        this.getNativeUiComponent().callUiMethod(SET_GALLERY_INDEX, new Object[] {PrimitiveTypeCache.valueOf(selectedIndex)});
    }
    
    public void setAdapter(ICitizenGalleryAdapter adapter)
    {
        if(adapter == null)
        {
            throw new NullPointerException();
        }

        this.adapter = adapter;
        
        this.getNativeUiComponent().callUiMethod(SET_GALLERY_ADAPTER, new Object[] {adapter});
    }
    
    public boolean onTouchEvent(Object event)
    {
        return ((Boolean)this.getNativeUiComponent().callUiMethod(ON_TOUCH_EVENT, new Object[] {event})).booleanValue();
    }

    public void movePrevious()
    {
        this.getNativeUiComponent().callUiMethod(MOVE_TO_PREVIOUS, null);        
    }
    
    public void moveNext()
    {
        this.getNativeUiComponent().callUiMethod(MOVE_TO_NEXT, null);                
    }
    
    public AbstractTnComponent getCurrentGallery()
    {
        int currentGallery = getCurrentGalleryIndex();
        
        return getGallery(currentGallery);
    }
    
    public AbstractTnComponent getGallery(int index)
    {
        if (this.galleries != null)
        {
            if (index >= 0 && index < this.galleries.length)
                return galleries[index];
        }
        
        return null;        
    }
    
    public int getCurrentGalleryIndex()
    {
        return ((Integer)this.getNativeUiComponent().callUiMethod(GET_GALLERY_INDEX, null)).intValue();
    }
    
    public int getGallerySize()
    {
        return this.adapter != null ? this.adapter.getGallerySize() : 0;
    }
    
    public AbstractTnComponent getDecorationPage(int galleryIndex)
    {
        TnLinearContainer hc = new TnLinearContainer(0, false);
        
        FrogNullField leftField = new FrogNullField(0);
        hc.add(leftField);
        AbstractTnComponent galleryPage = null; 
        if (this.adapter != null)
        {
            galleryPage = this.adapter.getView(galleryIndex);
            hc.add(galleryPage);
            
            int gallerySize = getGallerySize();
            if (galleries == null && gallerySize != 0)
            {
                galleries = new AbstractTnComponent[gallerySize];
            }
            if (galleries != null && galleryIndex < galleries.length)
            {
                galleries[galleryIndex] = galleryPage;   
            }
        }
        
        FrogNullField rightField = new FrogNullField(0);
        hc.add(rightField);
        
        PlaceHolderDecoration placeHolderDecoration = new PlaceHolderDecoration(galleryPage);

        leftField.getTnUiArgs().put(
                TnUiArgs.KEY_PREFER_WIDTH,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_LEFT_SIDE_PLACEHOLDER),
                        placeHolderDecoration));
        
        rightField.getTnUiArgs().put(
                TnUiArgs.KEY_PREFER_WIDTH,
                new TnUiArgAdapter(PrimitiveTypeCache.valueOf(ID_RIGHT_SIDE_PLACEHOLER),
                        placeHolderDecoration));

        
        return hc;
    }
    
    public void notifyGalleryChanged(boolean isNext)
    {
        if (this.commandListener != null)
        {
            TnUiEvent indexChange = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
            if (isNext)
            {
                indexChange.setCommandEvent(new TnCommandEvent(CMD_PAGE_INDEX_INCREASE));
            }
            else
            {
                indexChange.setCommandEvent(new TnCommandEvent(CMD_PAGE_INDEX_DECREASE));
            }
            this.commandListener.handleUiEvent(indexChange);
        }
    }
    
    public void notifyGalleryReachEnd()
    {
        if (this.commandListener != null)
        {
            TnUiEvent indexChange = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
            indexChange.setCommandEvent(new TnCommandEvent(CMD_PAGE_INDEX_REACH_END));
            this.commandListener.handleUiEvent(indexChange);
        }
    }
    
    /**
     * ICitizenGalleryAdapter adapter for the huge size of gallery, recycle to use the component
     */
    public interface ICitizenGalleryAdapter
    {
        /**
         * Retrieve component for the next gallery item.
         * 
         * @param positionIndex next shown gallery item position index
         * @return get refresh gallery component
         */
        AbstractTnComponent getView(int positionIndex);

        /**
         * Retrieve total size of gallery.
         * 
         * @return size of gallery.
         */
        int getGallerySize();
    }
    

    static class PlaceHolderDecoration implements ITnUiArgsDecorator
    {
        private AbstractTnComponent galleryPage;
        
        public PlaceHolderDecoration(AbstractTnComponent galleryPage)
        {
            this.galleryPage = galleryPage;
        }
        
        public Object decorate(TnUiArgAdapter args)
        {
            int screenWidth = AppConfigHelper.getDisplayWidth();
            int compWidth = galleryPage.getPreferredWidth();
            int id = ((Integer) (args.getKey())).intValue();
            switch (id)
            {
                case ID_LEFT_SIDE_PLACEHOLDER:
                    return PrimitiveTypeCache.valueOf((screenWidth - compWidth) >> 1);
                case ID_RIGHT_SIDE_PLACEHOLER:
                    return PrimitiveTypeCache.valueOf((screenWidth - compWidth) >> 1);
                case ID_TOP_MARGIN_HEIGHT:
                {
                    return PrimitiveTypeCache.valueOf(2);
                }
            }
            return null;
        }
    }

}

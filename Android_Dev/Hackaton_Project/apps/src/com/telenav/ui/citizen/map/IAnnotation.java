package com.telenav.ui.citizen.map;


/**
 *@author JY Xu
 *@date Nov 25, 2010
 */

public interface IAnnotation
{
    public boolean handleDownEvent(int x, int y);

    public boolean handleUpEvent(int x, int y);

    public boolean handleMoveEvent(int x, int y);
    
    public boolean handleClickEvent(int x, int y);

    public long addToMap();

    public int getWidth();

    public int getHeight();

    public boolean isSetFocused();
    
    public double getLat();
    
    public double getLon();
    
    public long getGraphicId();
    
    public long getPickableIdNum();

}

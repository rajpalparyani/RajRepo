/*
 * (c) Copyright 2010 by TeleNav, Inc.
 * All Rights Reserved.
 *
 */
package com.telenav.module.nav.routeplanning;

import com.telenav.logger.Logger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnColor;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;

/**
 * 
 * @author JY Xu, created on Feb18, 2013
 */
public class RoutePlanSelecter extends AbstractTnComponent
{
  
    public static final int STATUS_CHOICE_UNFOCUSED = 0;
    public static final int STATUS_CHOICE_FOCUSED_MAP = 1;
    public static final int STATUS_CHOICE_FOCUSED_LIST = 2;
    
    private int[] choicesStatusArray;
    private TnUiArgAdapter[] choicesUnfocusedImageArray;
    private TnUiArgAdapter[] choicesFocusedWithMapImageArray;
    private TnUiArgAdapter[] choicesFocusedWithListImageArray;
    
    private int selectedIndex;
    private int routeChoicesCount;
    private String[] distances;
    private String[] etas;
    
    private int imageWidth;
    private int imageHeight;
    
    public RoutePlanSelecter(int id, int selectedPlan, int routeChoicesCount, String[] distances, String[] etas, int selectedPlanStatus)
    {
        super(id);
        this.routeChoicesCount =  routeChoicesCount;
        this.distances = distances;
        this.etas = etas;
        updateImageArray();

        choicesStatusArray = new int[routeChoicesCount];
        
        for(int i=0; i<routeChoicesCount; i++)
        {
            choicesStatusArray[i] = STATUS_CHOICE_UNFOCUSED;
        }
        this.selectedIndex = selectedPlan;
        this.choicesStatusArray[selectedIndex] = selectedPlanStatus;
    }
    
    public void updateImageArray()
    {
        switch(routeChoicesCount) //according to the 
        {
            case 1:
                choicesUnfocusedImageArray = this.getOneRouteUnfocusAdapters();
                choicesFocusedWithMapImageArray = this.getOneRouteFocusWithMapAdapters();
                choicesFocusedWithListImageArray = this.getOneRouteFocusWithListAdapters();
                break;
            case 2:
                choicesUnfocusedImageArray = this.getTwoRoutesUnfocusAdapters();
                choicesFocusedWithMapImageArray = this.getTwoRoutesFocusWithMapAdapters();
                choicesFocusedWithListImageArray = this.getTwoRoutesFocusWithListAdapters();
                break;
            case 3:
                choicesUnfocusedImageArray = this.getThreeRoutesUnfocusAdapters();
                choicesFocusedWithMapImageArray = this.getThreeRoutesFocusWithMapAdapters();
                choicesFocusedWithListImageArray = this.getThreeRoutesFocusWithListAdapters();
                break;
        }
        
        if (choicesUnfocusedImageArray[0] != null)
        {
            imageWidth = choicesUnfocusedImageArray[0].getImage().getWidth();
            imageHeight = choicesUnfocusedImageArray[0].getImage().getHeight();
        }
    }

    public void setSelectedRouteChoice(int selectedIndex)
    {
        if(selectedIndex < 0 || selectedIndex >= routeChoicesCount)
            return;
        
        if(selectedIndex == this.selectedIndex)
        {
            if(choicesStatusArray[selectedIndex] == STATUS_CHOICE_FOCUSED_MAP)
            {
                choicesStatusArray[selectedIndex] = STATUS_CHOICE_FOCUSED_LIST;
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IRoutePlanningConstants.CMD_SHOW_ROUTE_LIST));
                this.commandListener.handleUiEvent(tnUiEvent);
            }
            else  if(choicesStatusArray[selectedIndex] == STATUS_CHOICE_FOCUSED_LIST)
            {
                choicesStatusArray[selectedIndex] = STATUS_CHOICE_FOCUSED_MAP;
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IRoutePlanningConstants.CMD_SHOW_ROUTE_MAP));
                this.commandListener.handleUiEvent(tnUiEvent);
            }
        }
        else
            
        {
            //clear last selectedIndex
            if(this.selectedIndex >= 0 && this.selectedIndex < routeChoicesCount)
            {
                choicesStatusArray[this.selectedIndex] = STATUS_CHOICE_UNFOCUSED;
            }
            this.selectedIndex = selectedIndex;
            choicesStatusArray[selectedIndex] = STATUS_CHOICE_FOCUSED_MAP;
            TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
            tnUiEvent.setCommandEvent(new TnCommandEvent(IRoutePlanningConstants.CMD_CHANGE_ROUTE_CHOICE_START + selectedIndex));
            this.commandListener.handleUiEvent(tnUiEvent);
        }
        requestPaint();
    }
    
    
    public int getSelectedChoiceButton(int x, int y)
    {
        int toSelectedIndex = -1;
        if (isPortraitMode())
        {
           if(y >=0 && y <= imageHeight)
           {
               toSelectedIndex = x/imageWidth;
           }
           else
           {
               if(isTapInTriangle(x,  y))
               {
                   toSelectedIndex = x/imageWidth;
               }
           }
        }
        else
        {
            if(x >=0 && x <= imageWidth)
            {
                toSelectedIndex = y/imageHeight;
            }
            else
            {
                if(isTapInTriangle(x,  y))
                {
                    toSelectedIndex = y/imageHeight;
                }
            }
        }
        return toSelectedIndex;
    }

    public int getWidth()
    {
        if (isPortraitMode())
        {
           return this.routeChoicesCount * imageWidth;
        }
        else
        {
            return choicesFocusedWithMapImageArray[0].getImage().getWidth();
        }
    }

    public int getHeight()
    {
        if (isPortraitMode())
        {
            return choicesFocusedWithMapImageArray[0].getImage().getHeight(); //use selected icon's height
        }
        else
        {
            return this.routeChoicesCount*imageHeight;
        }

    }
    
    public int getMiniHeight()
    {
        if (isPortraitMode())
        {
            return choicesUnfocusedImageArray[0].getImage().getHeight(); //use selected icon's height
        }
        else
        {
            return imageHeight;
        }
    }
    
    public int getMiniWidth()
    {
        if (isPortraitMode())
        {
            return imageWidth; //use selected icon's height
        }
        else
        {
            return choicesUnfocusedImageArray[0].getImage().getWidth();
        }
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "handleUiEvent()");

        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_TOUCH_EVENT:
            {
                TnMotionEvent motionEvent = tnUiEvent.getMotionEvent();
                boolean handled = false;
                int action = motionEvent.getAction();
                int x = motionEvent.getX();
                int y = motionEvent.getY();
                switch (action)
                {
                    case TnMotionEvent.ACTION_DOWN:
                        break;
                    case TnMotionEvent.ACTION_MOVE:
                        break;
                    case TnMotionEvent.ACTION_UP:
                        handled = handleUpEvent(x, y);
                        break;
                       
                }
               return handled;
            }
        }
        return false;
    }
   
    public void handleDownEvent(int x, int y)
    {
    }

    public boolean handleUpEvent(int x, int y)
    {
        int toSelectedIndex = this.getSelectedChoiceButton(x, y);
        if(toSelectedIndex >=0 && toSelectedIndex < routeChoicesCount)
        {
            this.setSelectedRouteChoice(toSelectedIndex);
            return true;
        }
        return false;
    }

    public void handleMoveEvent(int x, int y)
    {

    }

    protected void initDefaultStyle()
    {
        // TODO Auto-generated method stub

    }

    protected void paintBackground(AbstractTnGraphics graphics)
    {

    }

    protected void paint(AbstractTnGraphics g)
    {
        if (isPortraitMode())
        {
            paintPortraitMode(g);
        }
        else
        {
            paintLandscapeMode(g);
        }
    }

    private void paintPortraitMode(AbstractTnGraphics g)
    {
        int imageX = 0;
        int imageY = 0;
        AbstractTnImage image = null;

        //draw choices background
        for (int i = 0; i < this.routeChoicesCount; i++)
        {
            TnUiArgAdapter unfocusedAdapter =  choicesUnfocusedImageArray[i];
            TnUiArgAdapter focusedWithMapAdapter =  choicesFocusedWithMapImageArray[i];
            TnUiArgAdapter focusedWithListAdapter =  choicesFocusedWithListImageArray[i];

            if(i == this.selectedIndex)
            {
                if(choicesStatusArray[selectedIndex] == STATUS_CHOICE_FOCUSED_MAP)
                {
                    image = focusedWithMapAdapter.getImage();
                    g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
                }
                else //list
                {
                    image = focusedWithListAdapter.getImage();
                    g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
                }
            }
            else
            {
                image = unfocusedAdapter.getImage();
                g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
            }
           
            
            //draw distance
            AbstractTnFont distanceFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_DISTANCE);
            g.setFont(distanceFont);
            
            g.setColor(i == this.selectedIndex ? 0xFFFFFFFF : 0XFFBDCDDC);
            g.drawString(distances[i], imageX + imageWidth/2, 8, g.HCENTER | g.TOP);
            
            int etaY = 8 + distanceFont.getHeight() - 2;
            
            //draw eta
            g.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_ETA));
            g.setColor(0xFFFFFFFF);
            g.drawString(etas[i], imageX + imageWidth/2, etaY, g.HCENTER | g.TOP);
            
            imageX += imageWidth;
        }
        this.setBackgroundColor(TnColor.TRANSPARENT);
    }
    
    private void paintLandscapeMode(AbstractTnGraphics g)
    {
        int imageX = 0;
        int imageY = 0;
        AbstractTnImage image = null;

        //draw choices background
        for (int i = 0; i < this.routeChoicesCount; i++)
        {
            TnUiArgAdapter unfocusedAdapter =  choicesUnfocusedImageArray[i];
            TnUiArgAdapter focusedWithMapAdapter =  choicesFocusedWithMapImageArray[i];
            TnUiArgAdapter focusedWithListAdapter =  choicesFocusedWithListImageArray[i];

            if(i == this.selectedIndex)
            {
                if(choicesStatusArray[selectedIndex] == STATUS_CHOICE_FOCUSED_MAP)
                {
                    image = focusedWithMapAdapter.getImage();
                    g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
                }
                else //list
                {
                    image = focusedWithListAdapter.getImage();
                    g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
                }
            }
            else
            {
                image = unfocusedAdapter.getImage();
                g.drawImage(image, imageX, imageY, g.LEFT | g.TOP);
            }
           
            //draw eta
            g.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_ETA));
            g.setColor(0xFFFFFFFF);
            g.drawString(etas[i], imageX + imageWidth/6, imageY + imageHeight/2, g.VCENTER | g.LEFT);
            
            //draw distance
            AbstractTnFont distanceFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_ROUTE_PLANNING_DISTANCE);
            g.setFont(distanceFont);

            g.setColor(i == this.selectedIndex ? 0xFFFFFFFF : 0XFFBDCDDC);
            g.drawString(distances[i], imageX + imageWidth*4/5 , imageY + imageHeight/2, g.VCENTER | g.RIGHT);
            
            imageY += imageHeight;
        }
        this.setBackgroundColor(TnColor.TRANSPARENT);
    }
    
    public boolean isInsideBoundingBox(int x0, int y0, int x1, int y1, int x, int y)
    {
        return x >= x0 && x < x1 && y >= y0 && y < y1;
    }

    public boolean isPortraitMode()
    {
        return ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
    }
    
    private TnUiArgAdapter[] getOneRouteFocusWithMapAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
            {ImageDecorator.ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_FOCUSED};
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    {ImageDecorator.ROUTE_PLANNING_1A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED};
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getOneRouteFocusWithListAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
            {ImageDecorator.ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_FOCUSED};
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    {ImageDecorator.ROUTE_PLANNING_1A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED};
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getOneRouteUnfocusAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
            {ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_UNFOCUSED};
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    {ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED};
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getTwoRoutesUnfocusAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
        { ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_UNFOCUSED, 
                ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_UNFOCUSED };
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    { ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_LANDSCAPE_UNFOCUSED, 
                    ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_LANDSCAPE_UNFOCUSED };
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getTwoRoutesFocusWithMapAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
              {ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_FOCUSED, 
               ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_FOCUSED };
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    {ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED, 
                     ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED };
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getTwoRoutesFocusWithListAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
              { ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_FOCUSED, 
               ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_LIST_SUMMARY_FOCUSED };
        }
        else
        {
            adapters = new TnUiArgAdapter[]
                { ImageDecorator.ROUTE_PLANNING_2A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, 
                 ImageDecorator.ROUTE_PLANNING_2B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED };
        }
        return adapters;
    }
    
    private TnUiArgAdapter[] getThreeRoutesUnfocusAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters = new TnUiArgAdapter[]
            { 
              ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_UNFOCUSED, 
              ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_UNFOCUSED,
              ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_UNFOCUSED };
            }
        else
        {
            adapters = new TnUiArgAdapter[]
                    { 
                      ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_LANDSCAPE_UNFOCUSED, 
                      ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_LANDSCAPE_UNFOCUSED,
                      ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_LANDSCAPE_UNFOCUSED };
        }
        return adapters;
    }
    
    
    private TnUiArgAdapter[] getThreeRoutesFocusWithMapAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
            { ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_FOCUSED, 
                ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_FOCUSED,
                ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_FOCUSED };
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    { ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_MAP_SUMMARY_LANDSCAPE_FOCUSED, 
                        ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_MAP_SUMMARY_LANDSCAPE_FOCUSED,
                        ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_MAP_SUMMARY_LANDSCAPE_FOCUSED };
        }
        return adapters;
    }
    
    //we just use a rectangle to simulate the triangle for simple
    private boolean isTapInTriangle(int x, int y)
    {
        if(isPortraitMode())
        {
            int tempSelectedIndex = x/imageWidth;
            int tempStatus = STATUS_CHOICE_UNFOCUSED;
            if(tempSelectedIndex >= 0 && tempSelectedIndex < routeChoicesCount)
            {
                tempStatus = choicesStatusArray[tempSelectedIndex];
            }
                    
            int xLeft = x % imageWidth;
            if(tempStatus != STATUS_CHOICE_UNFOCUSED && xLeft >= 0 && xLeft <= imageWidth && y>= imageHeight && y<= getHeight())
            {
                return true;
            } 
            return false;
        }
        else
        {
            int tempSelectedIndex = y/imageHeight;
            int tempStatus = STATUS_CHOICE_UNFOCUSED;
            if(tempSelectedIndex >= 0 && tempSelectedIndex < routeChoicesCount)
            {
                tempStatus = choicesStatusArray[tempSelectedIndex];
            }
                    
            int yLeft = y % imageHeight;
            if (tempStatus != STATUS_CHOICE_UNFOCUSED && yLeft >= 0 && yLeft <= imageHeight && x >= imageWidth
                    && x <= getWidth())
            {
                return true;
            } 
            return false;
        }
    }
    
    private TnUiArgAdapter[] getThreeRoutesFocusWithListAdapters()
    {
        TnUiArgAdapter[] adapters = null;
        if(isPortraitMode())
        {
            adapters =  new TnUiArgAdapter[]
            { ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_FOCUSED, 
                ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_FOCUSED,
                ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_FOCUSED };
        }
        else
        {
            adapters =  new TnUiArgAdapter[]
                    { ImageDecorator.ROUTE_PLANNING_3A_PANEL_GREEN_LIST_SUMMARY_LANDSCAPE_FOCUSED, 
                    ImageDecorator.ROUTE_PLANNING_3B_PANEL_BLUE_LIST_SUMMARY_LANDSCAPE_FOCUSED,
                    ImageDecorator.ROUTE_PLANNING_3C_PANEL_RED_LIST_SUMMARY_LANDSCAPE_FOCUSED };
        }
        return adapters;
    }
}

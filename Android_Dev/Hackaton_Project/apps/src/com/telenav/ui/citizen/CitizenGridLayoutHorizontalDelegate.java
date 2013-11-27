/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * PoiDetailButtonsLayoutDelegate.java
 *
 */
package com.telenav.ui.citizen;

import java.util.Vector;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 * It is for auto layout after orientation changed.
 * Constructor needs to be specified the line item count both in portrait mode and in landscape mode.
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-9-21
 */
public class CitizenGridLayoutHorizontalDelegate
{
    private int compCount;
    private int numsOnLineInPortrait;
    private int numsOnLineInLandscape;
    private Vector components;
    private TnUiArgAdapter linesGapArg;
    private TnUiArgAdapter columnsGapArg;
    private AbstractTnContainer portraitlineContainer;
    private AbstractTnContainer landscapelineContainer;
    private AbstractTnContainer portraitContainer;
    private AbstractTnContainer landscapeContainer;
    private AbstractTnContainer parentContainer;
    private int anchor;
    
    
    /**
     * Constructor for layout  
     * @param container parent container.
     * @param numsOnLineInPortrait numbers of components on a line on portrait mode.
     * @param numsOnLineInLandscape numbers of components on a line on landscape mode.
     */
    public CitizenGridLayoutHorizontalDelegate(AbstractTnContainer container, int numsOnLineInPortrait, int numsOnLineInLandscape)
    {
        this.components = new Vector();
        this.parentContainer = container;   
        this.numsOnLineInPortrait = numsOnLineInPortrait;
        this.numsOnLineInLandscape = numsOnLineInLandscape;
        this.portraitContainer = new TnLinearContainer(0, true);
        this.landscapeContainer = new TnLinearContainer(0, true);
        this.anchor = AbstractTnGraphics.VCENTER;
        init();
       
    }
    private void init()
    {
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() 
                == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            parentContainer.add(portraitContainer);
        }
        else
        {
            parentContainer.add(landscapeContainer);
        }
    }
    /**
     * Re-layout the container after orientation changed. 
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    public void onPreSizeChanged(int w, int h, int oldw, int oldh)
    {
        AbstractTnContainer container = null;
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() 
                 == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            parentContainer.remove(landscapeContainer);
            parentContainer.add(portraitContainer);
            container = landscapeContainer;
        }
        else
        {
            parentContainer.remove(portraitContainer);
            parentContainer.add(landscapeContainer);
            container = portraitContainer;
        }
        
        if (container != null)
        {
            for (int i = 0; i < container.getChildrenSize(); i++)
            {
                if (container.get(i) instanceof AbstractTnContainer)
                {
                    AbstractTnContainer lineContainer = (AbstractTnContainer) container
                            .get(i);
                    if (lineContainer.getChildrenSize() > 0)
                    {
                        lineContainer.removeAll();
                    }
                }
            }
        }
        
        for(int i = 0; i < this.components.size(); i++)
        {
            updateLayout((AbstractTnComponent)this.components.elementAt(i), i);
        }
    }
    
    /**
     * Set TnUiArgAdapter for the line gap.
     * @param argAdapter
     */
    public void setLinesGapArg(TnUiArgAdapter argAdapter)
    {
        linesGapArg = argAdapter;
    }
    
    /**
     * Set TnUiArgAdapter for the column gap.
     * @param argAdapter
     */
    public void setColumnsGapArg(TnUiArgAdapter argAdapter)
    {
        columnsGapArg = argAdapter;
    }
    
    /**
     * Add components.
     * @param tnComponent
     */
    public void add(AbstractTnComponent tnComponent)
    {
        components.addElement(tnComponent);
        addDelegate(tnComponent);
    }
    
    public void setLineAnchor(int anchor)
    {
        this.anchor = anchor;
    }
    
    /**
     * Update layouts according to the screen orientation.
     * @param tnComponent
     * @param compIndex
     */
    protected void updateLayout(AbstractTnComponent tnComponent, int compIndex)
    {
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {

            int lineIndex = (compIndex / numsOnLineInPortrait) * 2;
            AbstractTnContainer lineContainer = (AbstractTnContainer) portraitContainer
                    .get(lineIndex);
            if (compIndex % numsOnLineInPortrait == 0)
            {
                lineContainer.add(tnComponent);
            }
            else
            {
                FrogNullField columGap = new FrogNullField(0);
                columGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, columnsGapArg);
                lineContainer.add(columGap);
                lineContainer.add(tnComponent);
            }
        }
        else
        {
            int lineIndex = (compIndex / numsOnLineInLandscape) * 2;
            AbstractTnContainer lineContainer = (AbstractTnContainer) landscapeContainer
                    .get(lineIndex);
            if (compIndex % numsOnLineInLandscape == 0)
            {
                lineContainer.add(tnComponent);
            }
            else
            {
                FrogNullField columGap = new FrogNullField(0);
                columGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, columnsGapArg);
                lineContainer.add(columGap);
                lineContainer.add(tnComponent);
            }
        }
    }
    
    /**
     * add components to the container delegate
     * @param tnComponent
     */
    protected void addDelegate(AbstractTnComponent tnComponent)
    {
        boolean isLandscape = true;
        if (((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() 
                == AbstractTnUiHelper.ORIENTATION_PORTRAIT)
        {
            isLandscape = false;
        }
       
        if(compCount % numsOnLineInPortrait == 0)
        {
            portraitlineContainer = new TnLinearContainer(0, false, this.anchor);
            if(compCount != 0)
            {
                FrogNullField linesGap = new FrogNullField(0);
                linesGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, linesGapArg);
                portraitContainer.add(linesGap);
            }
            portraitContainer.add(portraitlineContainer);            
        }
        
        if(compCount % numsOnLineInLandscape == 0)
        {
            landscapelineContainer =  new TnLinearContainer(0, false, this.anchor);
            if(compCount != 0)
            {
                FrogNullField linesGap = new FrogNullField(0);
                linesGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, linesGapArg);  
                landscapeContainer.add(linesGap);
            }
            landscapeContainer.add(landscapelineContainer);   
        }
        AbstractTnContainer currLineContainer = portraitlineContainer;
        int compNumEachLine = numsOnLineInPortrait;
        if(isLandscape)
        {
            currLineContainer =  landscapelineContainer;
            compNumEachLine = numsOnLineInLandscape;
        }
        if(compCount % compNumEachLine == 0)
        {
            currLineContainer.add(tnComponent);
        }
        else
        {
            FrogNullField columGap = new FrogNullField(0);
            columGap.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, columnsGapArg);
            currLineContainer.add(columGap);
            currLineContainer.add(tnComponent);
            
        }
        compCount++;
    }
   
}


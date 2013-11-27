/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * FrogTnList.java
 *
 */
package com.telenav.ui.frogui.widget;

import java.util.Hashtable;
import java.util.Vector;

import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnSizeChangListener;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnScrollPanel;
import com.telenav.util.PrimitiveTypeCache;

/**
 * FrogList provide list which is scrolled and needed to add {@link FrogListItem}.
 *@author jwchen (jwchen@telenav.cn)
 *@date 2010-7-27
 */
public class FrogList extends TnScrollPanel implements ITnUiArgsDecorator, ITnSizeChangListener
{
    protected Vector realizedItems;
    protected Hashtable measureItems;
    protected int realizedItemsSize;
    protected int scrolledY = 0;
    protected int realizeItemMaxSize = 30;
    protected int calculateMaxSize = 100;
    protected int visibleSize;
    protected int topBufferSize;
    protected int firstRealizedIndex;
    protected int needScrollDistance = 0;
    protected int childrenHeight[];
    protected int lastOrientation;
    protected FrogNullField topNullField;
    protected FrogNullField bottomNullField;
    protected FrogAdapter listAdapter;
    
    protected final static Object TOP_NULL_FIELD_HEIGHT = new Object();
    protected final static Object BOTTOM_NULL_FIELD_HEIGHT = new Object();
    protected final static Object NULL_FIELD_WIDTH = new Object();
    protected final static Object LIST_FIELD_HEIGHT = new Object();
    
    protected TnLinearContainer listContainer;
    
    /**
     * Constructor of FrogList
     * @param id
     */
    public FrogList(int id)
    {
        super(id, true);
        
        listContainer = new TnLinearContainer(id, true);
        listContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(LIST_FIELD_HEIGHT, this));
        listContainer.setSizeChangeListener(this);
        this.set(listContainer);
        realizedItems = new Vector();
    }
    
    /**
     * Set listAdapter. If you do this the calling of addItem() will be blocked. 
     * 
     * @param adapter
     */
    public  void setAdapter(FrogAdapter adapter)
    {
        if(adapter != null)
        {
            clear();
            this.listAdapter = adapter;
            initAdapter();
            updateUiItems();
            this.scrollTo(0, 0);
        }
    }
    
    public FrogAdapter getAdapter()
    {
        return  this.listAdapter;
    }
    
    protected void initAdapter()
    {
        int index = 0;
        int adapterSize = listAdapter.getCount();
        while(realizedItems.size() < realizeItemMaxSize && index < adapterSize)
        {
            AbstractTnComponent component = (AbstractTnComponent)listAdapter.getComponent(index, null, this);
            if(component != null)
            {
                realizedItems.addElement(component);
                if(this.bottomNullField != null)
                {
                    listContainer.add(component, listContainer.getChildrenSize() - 1);
                }
                else
                {
                    listContainer.add(component);
                }
                this.realizedItemsSize = realizedItems.size();
                init(component);
            }
            index ++;
        }        
    }

    protected void init(AbstractTnComponent item)
    {
        AbstractTnComponent itemComponent = null;
        itemComponent = (AbstractTnComponent)realizedItems.elementAt(0);
        
        int itemHeight = itemComponent.getPreferredHeight();
        if(itemHeight <= 0)
        {
            itemComponent.sublayout(-1, -1);
            itemHeight = itemComponent.getPreferredHeight();
            if(itemHeight > 0)
            {
                int height = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayHeight();
                int width = ((AbstractTnUiHelper)AbstractTnUiHelper.getInstance()).getDisplayWidth();
                int maxHeight = width > height? width: height;
                realizeItemMaxSize =  (maxHeight / itemHeight) * 2;
                if(realizeItemMaxSize <= 0)
                {
                    realizeItemMaxSize = 2;
                }
                calculateMaxSize = realizeItemMaxSize * 2;
            }
        }
        
        
        if(listAdapter.getCount() > realizeItemMaxSize)
        {        
            TnUiArgs itemArgs = item.getTnUiArgs();
            if(topNullField == null)
            {
                topNullField = new FrogNullField(-111);
                if(itemArgs != null)
                {
                    TnUiArgs tnUiArgs = new TnUiArgs();
                    tnUiArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, itemArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS));
                    tnUiArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, itemArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS));
                    tnUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(TOP_NULL_FIELD_HEIGHT, this));
                    tnUiArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(NULL_FIELD_WIDTH, this));
                    topNullField.getTnUiArgs().copy(tnUiArgs);
                }
                
                listContainer.add(topNullField, 0);
            }
            
            if(bottomNullField == null)
            {
                bottomNullField = new FrogNullField(-111);
                
                if(itemArgs != null)
                {
                    TnUiArgs tnUiArgs = new TnUiArgs();
                    tnUiArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, itemArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS));
                    tnUiArgs.put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, itemArgs.get(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS));
                    tnUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(BOTTOM_NULL_FIELD_HEIGHT, this));
                    tnUiArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(NULL_FIELD_WIDTH, this));
                    bottomNullField.getTnUiArgs().copy(tnUiArgs);
                }
                
                listContainer.add(bottomNullField);
            }
            
            //visibleSize = realizedItems.size() < realizeItemMaxSize? realizedItems.size() : realizeItemMaxSize;
        }
    }

    /**
     * Set current select index for list.
     * 
     * @param index
     */
    public void setSelectedIndex(int index)
    {
        int totalScrollDistance = 0;
        if(index > 0)
        {
            totalScrollDistance = measureChildrenHeight(0, index - 1);
            if (listContainer.getPreferredHeight() - this.getHeight() < totalScrollDistance)
            {
                totalScrollDistance = listContainer.getPreferredHeight() - getHeight();
            }
        }

        if (totalScrollDistance != 0)
        {
            needScrollDistance = totalScrollDistance;
            scrollTo(0, totalScrollDistance);
        }

        AbstractTnComponent c = listContainer.getComponentById(index);

        if (c != null)
        {
            c.requestFocus();
        }
    }
    
    protected int measureChildrenHeight(int startIndex, int endIndex)
    {
        if(startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return 0;
        
        int orientation = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation();
        
        if(orientation != lastOrientation)
        {
            lastOrientation = orientation;
            childrenHeight = null;
        }
        
        if(measureItems == null)
            measureItems = new Hashtable();
        
        if(childrenHeight == null || childrenHeight.length != listAdapter.getCount())
            childrenHeight = new int[listAdapter.getCount()];
        
        int height = 0;
        
        int calculateEndIndex = endIndex;
        
        if(endIndex > calculateMaxSize)
        {
            calculateEndIndex = firstRealizedIndex + realizedItemsSize;
            if(calculateEndIndex > endIndex)
                calculateEndIndex = endIndex;
        }
        
        for(int i = startIndex ; i <= calculateEndIndex && i < childrenHeight.length; i ++)
        {
            if(childrenHeight[i] <= 0)
            {
                Integer key = PrimitiveTypeCache.valueOf(listAdapter.getItemType(i));
                AbstractTnComponent component;
                if(measureItems.get(key) == null)
                {
                    component = listAdapter.getComponent(i, null, this);
                    measureItems.put(key, component);
                }
                else
                {
                    component = listAdapter.getComponent(i, (AbstractTnComponent)measureItems.get(key), this);
                }
                
                if(component == null)
                {
                    continue;
                }
                component.sublayout(this.getWidth(), 0);
                childrenHeight[i] = component.getPreferredHeight();
            }
            
            height += childrenHeight[i];
        }
        
        if(calculateEndIndex != endIndex)
            height = height * ( endIndex - startIndex + 1 ) / (calculateEndIndex - startIndex + 1);
        
        return height;
    }
    
    protected int getVisableIndex(int scrollY)
    {
        if(measureItems == null)
            measureItems = new Hashtable();
        
        if(childrenHeight == null || childrenHeight.length != listAdapter.getCount())
            childrenHeight = new int[listAdapter.getCount()];
        
        int height = 0;
        int index = 0;
        
        while(index < this.listAdapter.getCount())
        {
            if(childrenHeight[index] <= 0)
            {
                Integer key = PrimitiveTypeCache.valueOf(listAdapter.getItemType(index));
                AbstractTnComponent component = listAdapter.getComponent(index, (AbstractTnComponent)measureItems.get(key), this);
                if(component == null)
                {
                    index ++;
                    continue;
                }
                component.sublayout(this.getWidth(), 0);
                childrenHeight[index] = component.getPreferredHeight();
            }
            height += childrenHeight[index];
            if(height >= scrollY)
            {
                break;
            }
            index ++;
        }
        
        return index;
    }
    
    protected void scrollTo(int x, int y)
    {
        this.nativeUiComponent.callUiMethod(METHOD_SCROLL_TO, new Object[]{PrimitiveTypeCache.valueOf(x), PrimitiveTypeCache.valueOf(y)});
    }
    
    public void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        if(needScrollDistance != 0 && needScrollDistance == t)
        {
            needScrollDistance = 0;
        }
        scrolledY = t;
        updateUiItems();
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if(realizedItems == null || realizedItems.size() <= 0)
            return;
        
        childrenHeight = null;
        visibleSize = getVisableIndex(h) + 2;
        
        topBufferSize = (this.realizedItemsSize - visibleSize) / 2;
        if(topBufferSize < 0)
        {
            topBufferSize = 0;
        }
        updateUiItems();
        
        if(needScrollDistance > 0)
        {
            this.scrollTo(0, needScrollDistance);
        }
    }
    
    protected AbstractTnComponent getRealizeItem(int index, AbstractTnComponent convertItem)
    {
        convertItem = (AbstractTnComponent)listAdapter.getComponent(index, convertItem, this);
        return convertItem;
    }
    
    protected void updateBufferSize()
    {
        int size = listAdapter.getCount();
        if(size == 0)
            return;
        
        if(size > realizeItemMaxSize)
        {
            this.topBufferSize = (this.realizeItemMaxSize - visibleSize) / 2;
        }
        else
        {
            this.topBufferSize = (size - visibleSize)/2;
            if(topBufferSize < 0)
            {
                topBufferSize = 0;
            }
        }
    }
    
    protected void updateUiItems()
    {
        int scrollY = this.scrolledY;
        
        updateBufferSize();
        
        int tempFirstRealizedIndex = getVisableIndex(scrollY) - topBufferSize;
        if(tempFirstRealizedIndex < 0)
        {
            tempFirstRealizedIndex = 0;
        }
        
        int size = listAdapter.getCount();
        
        if(size == 0)
        	return;
        
        if(tempFirstRealizedIndex +  realizeItemMaxSize > size)
        {
            tempFirstRealizedIndex = size - realizeItemMaxSize;
        }
        
        if(tempFirstRealizedIndex < 0)
        {
            tempFirstRealizedIndex = 0;
        }
        
        if(tempFirstRealizedIndex == this.firstRealizedIndex)
        {
            return;
        }
        else
        {
            Vector children = this.realizedItems;
            if(tempFirstRealizedIndex > this.firstRealizedIndex)
            {
                int needRemoveChildrenSize = tempFirstRealizedIndex - this.firstRealizedIndex;
                
                int tmpVisibelIndex = this.firstRealizedIndex;
                this.firstRealizedIndex = tempFirstRealizedIndex;
                
                if(needRemoveChildrenSize > children.size())
                {
                    needRemoveChildrenSize = children.size();
                    for(int i = 0; i < needRemoveChildrenSize; i ++)
                    {
                        AbstractTnComponent itemComponent = (AbstractTnComponent) children.elementAt(0);
                        children.removeElementAt(0);
                        listContainer.remove(itemComponent);
                        itemComponent = getRealizeItem(tempFirstRealizedIndex + i, itemComponent);
                        children.addElement(itemComponent);
                        if(itemComponent.getParent() != null)
                        {
                            ((AbstractTnContainer)itemComponent.getParent()).remove(itemComponent);
                        }
                        listContainer.add(itemComponent, listContainer.getChildrenSize() - 1);
                    }
                }
                else
                {
                    for(int i = 0; i < needRemoveChildrenSize; i ++)
                    {
                        AbstractTnComponent itemComponent = (AbstractTnComponent) children.elementAt(0);
                        children.removeElementAt(0);
                        listContainer.remove(itemComponent);
                        itemComponent = getRealizeItem(tmpVisibelIndex + realizedItemsSize + i, itemComponent);
                        children.addElement(itemComponent);
                        if(itemComponent.getParent() != null)
                        {
                            ((AbstractTnContainer)itemComponent.getParent()).remove(itemComponent);
                        }
                        listContainer.add(itemComponent, listContainer.getChildrenSize() - 1);
                    }
                }
                this.requestLayout();
            }
            else if(tempFirstRealizedIndex < this.firstRealizedIndex)
            {
                int needRemoveChildrenSize = -(tempFirstRealizedIndex - this.firstRealizedIndex);
                int tmpVisibelIndex = this.firstRealizedIndex;
                this.firstRealizedIndex = tempFirstRealizedIndex;
                
                if(needRemoveChildrenSize > children.size())
                    needRemoveChildrenSize = children.size();
                
                for(int i = 0; i < needRemoveChildrenSize; i ++)
                {
                    int index = children.size() - 1;
                    AbstractTnComponent itemComponent = (AbstractTnComponent) children.elementAt(index);
                    children.removeElementAt(index);
                    listContainer.remove(itemComponent);
                    itemComponent = getRealizeItem(tmpVisibelIndex - i - 1, itemComponent);
                    children.insertElementAt(itemComponent, 0);
                    if(itemComponent.getParent() != null)
                    {
                        ((AbstractTnContainer)itemComponent.getParent()).remove(itemComponent);
                    }
                    listContainer.add(itemComponent, 1);
                }
                this.requestLayout();
            }
        }
    }
    
    /**
     * Update all items base on ListAdapter.
     * 
     * @param selectedIndex
     */
    public void updateAllItems(int selectedIndex)
    {
        Vector children = this.realizedItems;
        if(selectedIndex < 0)
            selectedIndex = firstRealizedIndex;
        
        int size = listAdapter.getCount();
        if(size == 0)
            return;
        
        updateBufferSize();
        
        int itemStartIndex = selectedIndex - this.topBufferSize;
        realizedItemsSize = children.size() > size ? size : children.size();
        
        if(itemStartIndex + realizedItemsSize > size)
        {
            itemStartIndex = size - realizedItemsSize; 
        }
        
        if(itemStartIndex < 0)
        {
            itemStartIndex = 0;
        }
        
        if(topNullField != null)
        {
            while(listContainer.getChildrenSize() > 1)
            {
                listContainer.remove(1);
            }
        }
        else
        {
            listContainer.removeAll();
        }
        
        this.firstRealizedIndex = itemStartIndex;
        
        for(int i = 0; i < realizedItemsSize; i ++)
        {
            AbstractTnComponent item = (AbstractTnComponent) children.elementAt(i);
            item = getRealizeItem(itemStartIndex + i, item);
            if(item == null)
                break;
            if(bottomNullField == null)
            {
                listContainer.add(item);
            }
            else
            {
                listContainer.add(item, listContainer.getChildrenSize() - 1);
            }
        }
        
        int needAddedChildSize = realizeItemMaxSize - realizedItemsSize; 
        int base = itemStartIndex + realizedItemsSize;
        for(int j = 0 ; j < needAddedChildSize; j ++)
        {
            if(base + j > listAdapter.getCount() - 1)
                break;
            
            AbstractTnComponent item = getRealizeItem(base + j, null);
            if(item == null)
                break;
            
            children.addElement(item);
            this.realizedItemsSize = children.size();
            if(bottomNullField == null)
            {
                listContainer.add(item);
            }
            else
            {
                listContainer.add(item, listContainer.getChildrenSize() - 1);
            }
        }
        
        this.setSelectedIndex(selectedIndex);
    }

    public Object decorate(TnUiArgAdapter args)
    {
        return PrimitiveTypeCache.valueOf(getDimenison(args.getKey()));
    }
    
    /**
     * reset all data in list. 
     */
    public void reset()
    {
        this.removeAllViews();
        clear();
    }
    
    /**
     * it is called before handling the onSizeChanged.
     * @param tnComponent onSizeChanged event source.
     * @param w needed width.
     * @param h needed height.
     * @param oldw width before changing.
     * @param oldh height before changing.
     */
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        if(tnComponent == this.listContainer && this.needScrollDistance > 0)
        {
            this.scrollTo(0, needScrollDistance);
        }
    }
    
    protected int getDimenison(Object key)
    {
        int result = 0;
        
        if(key == TOP_NULL_FIELD_HEIGHT)
        {
            result = measureChildrenHeight(0, firstRealizedIndex - 1);
        }
        else if(key == BOTTOM_NULL_FIELD_HEIGHT)
        {
            result = this.listContainer.getPreferredHeight() - this.topNullField.getPreferredHeight() - measureChildrenHeight(this.firstRealizedIndex, this.firstRealizedIndex + realizedItemsSize - 1);
        }
        else if(key == LIST_FIELD_HEIGHT)
        {
            result = measureChildrenHeight(0, listAdapter.getCount() - 1);
        }
        else if(key == NULL_FIELD_WIDTH)
        {
            result = listContainer.getPreferredWidth();
        }
        
        return result;
    }
    
    protected void clear()
    {
        this.scrolledY = 0;
        this.bottomNullField = null;
        this.topNullField = null;
        this.childrenHeight = null;
        this.isFocused = false;
        this.firstRealizedIndex = 0;
        this.topBufferSize = 0;
        if(measureItems != null)
            this.measureItems.clear();
        this.realizedItems.removeAllElements();
        this.listContainer.removeAll();
        this.realizedItemsSize = 0;
    }
    
}

/**
 *
 * Copyright 2011 TeleNav, Inc. All rights reserved.
 * Filter4TextField.java
 *
 */
package com.telenav.module.oneboxsearch;

import java.util.Vector;

import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.module.ac.AddressListFilter;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.NinePatchImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogKeywordFilter;
import com.telenav.ui.frogui.widget.FrogList;

/**
 *@author qli
 *@date 2011-6-8
 */
public class Filter4TextField
{
    protected FrogList strList = null;//filter list container
    protected Vector filterData = null;//filter list data
    protected TnUiArgAdapter listItemHeight = null;//list item height
    protected ITnUiEventListener commandListener = null;//list item command listener
    protected int listItemCmd = 0;//list item command id
    
    public Filter4TextField()
    {
    }
    
    /**
     * Add address filter into textField.
     * @param textField  filter host.
     * @param data  address object vector.
     * @param filter  filter object vector. Can be empty vector.
     * @param listItemCmd  list item command id.
     * @param listItemHeight  list item height.
     */
    public void addFilter4Address(CitizenTextField textField, Vector data, 
            Vector filter, final int listItemCmd, TnUiArgAdapter listItemHeight)
    {
        addFilter4Address(textField, 0, data, filter, listItemCmd, listItemHeight);
    }
    
    /**
     * Add address filter into textField.
     * @param textField  filter host.
     * @param frogListId  list container component id
     * @param data  address object vector.
     * @param filter  filter object vector. Can be empty vector.
     * @param listItemCmd  list item command id.
     * @param listItemHeight  list item height.
     */
    public void addFilter4Address(CitizenTextField textField, int frogListId, 
            Vector data, Vector filter, final int listItemCmd, TnUiArgAdapter listItemHeight)
    {
        this.listItemCmd = listItemCmd;
        this.listItemHeight = listItemHeight;
        strList = UiFactory.getInstance().createList(frogListId);
        strList.setAdapter(new AddressFilterAdapter(filter));
        textField.setKeyworkFilter(new AddressFilter(textField, strList, data));
        textField.setDropDownList(strList);
        textField.setTextChangeListener(new ITnTextChangeListener()
        {
            
            public void onTextChange(AbstractTnComponent component, String text)
            {
                if("".equals(text))
                {
                    strList.setAdapter(new AddressFilterAdapter(new Vector()));
                }
            }
        });
    }

    /**
     * Add address filter into textField.
     * @param textField  filter host.
     * @param data  city object vector.
     * @param filter  filter object vector. Can be empty vector.
     * @param listItemCmd  list item command id.
     * @param listItemHeight  list item height.
     */
    public void addFilter4City(CitizenTextField textField, Vector data, 
            Vector filter, final int listItemCmd, TnUiArgAdapter listItemHeight)
    {
        addFilter4City(textField, 0, data, filter, listItemCmd, listItemHeight);
    }

    /**
     * Add address filter into textField.
     * @param textField  filter host.
     * @param frogListId  list container component id
     * @param data  city object vector.
     * @param filter  filter object vector. Can be empty vector.
     * @param listItemCmd  command id of list item.
     * @param listItemHeight  height of list item.
     */
    public void addFilter4City(CitizenTextField textField, int frogListId, 
            Vector data, Vector filter, final int listItemCmd, TnUiArgAdapter listItemHeight)
    {
        this.listItemCmd = listItemCmd;
        this.listItemHeight = listItemHeight;
        strList = UiFactory.getInstance().createList(frogListId);
        strList.setAdapter(new CityFilterAdapter(filter));
        textField.setKeyworkFilter(new CityFilter(textField, strList, data));
        textField.setDropDownList(strList);
        textField.setTextChangeListener(new ITnTextChangeListener()
        {
            
            public void onTextChange(AbstractTnComponent component, String text)
            {
                if("".equals(text))
                {
                    strList.setAdapter(new CityFilterAdapter(new Vector()));
                }
            }
        });
    }
    
    /**
     * set width of list component  
     **/
    public void setFilterWidth(TnUiArgAdapter width)
    {
        this.strList.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, width);
    }
    
    /**
     * set height of list component  
     **/
    public void setFilterHeight(TnUiArgAdapter height)
    {
        this.strList.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, height);
    }
    
    /**
     * set commandListener of list item component  
     **/
    public void setFilterCommandEventListener(ITnUiEventListener commandListener)
    {
        this.commandListener = commandListener;
    }
    
    /**
     * 
     * @return data that percolated.
     */
    public Vector getFilterData()
    {
        return filterData;
    }
    
    
    class CityFilterAdapter implements FrogAdapter
    {
        private Vector data;
        
        public CityFilterAdapter(Vector data)
        {
            this.data = data;
        }
        
        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            Stop cities = (Stop) data.elementAt(position);
            CitizenAddressListItem item;
            if (convertComponent == null)
            {
                item = UiFactory.getInstance().createCitizenAddressListItem(position);
                item.setGap(5, 0);
                item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, listItemHeight);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", listItemCmd);
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            }
            else
            {
                item =(CitizenAddressListItem)convertComponent;
                item.setId(position);
            }
            item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(cities, false));
            if (position != data.size() - 1)
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_FOCUSED);
            }
            else
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_FOCUSED);
            }
            return item;
           }
        
        public int getCount()
        {
            return data.size();
        }

        public int getItemType(int position)
        {
            return 0;
        }
    }
    
    
    class CityFilter extends FrogKeywordFilter
    {
        private FrogList list;
        private Vector items;
        private CitizenTextField textField;
        
        public CityFilter(CitizenTextField textField, FrogList list, Vector items)
        {
            this.list = list;
            this.items = items;
            this.textField = textField;
        }
        
        protected FilterResults performFilter(FilterRequest constraint)
        {
            FilterResults results = new FilterResults();
            Vector searchList = AddressListFilter.getFilteredList(constraint.keyword, items, false);
            results.constraint = constraint.keyword;
            results.values = searchList;
            results.count = searchList.size();
            return results;
        }

        protected void publishResults(FilterResults results)
        {
            final Vector data = (Vector) results.values;
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (list != null)
                    {
                        list.setAdapter(new CityFilterAdapter(data));
                        if (textField != null)
                        {
                            if (data.size() > 0)
                                textField.showDropDown();
                            else
                                textField.hideDropDown();
                        }
                    }
                }
            });
        }
    }
    

    class AddressFilterAdapter implements FrogAdapter
    {
        private Vector data;
        
        public AddressFilterAdapter(Vector data)
        {
            this.data = data;
        }
        
        public AbstractTnComponent getComponent(int position, AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            Address address = (Address) data.elementAt(position);
            CitizenAddressListItem item;
            if (convertComponent == null)
            {
                item = UiFactory.getInstance().createCitizenAddressListItem(position);
                item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
                item.setGap(5, 0);
                TnMenu menu = UiFactory.getInstance().createMenu();
                menu.add("", listItemCmd);
                item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
                item.setCommandEventListener(commandListener);
                item.setTitleColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setForegroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH), UiStyleManager.getInstance()
                        .getColor(UiStyleManager.TEXT_COLOR_ME_GR));
                item.setFont(UiStyleManager.getInstance().getFont(UiStyleManager.FONT_TEXT_FIELD));
                item.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, listItemHeight);
            }
            else
            {
                item = (CitizenAddressListItem)convertComponent;
                item.setId(position);
            }
            int type = address.getType();
            if(type == Address.TYPE_FAVORITE_POI || type == Address.TYPE_FAVORITE_STOP && address.getCatagories() != null 
                    && !address.getCatagories().contains(AddressDao.RECEIVED_ADDRESSES_CATEGORY))
            {
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_FAV_ICON_LIST_UNFOCUS);
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_FAV_ICON_LIST_FOCUSED);
            }
            else
            {
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_UNFOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_UNFOCUSED);
                item.getTnUiArgs().put(CitizenAddressListItem.KEY_LEFT_ICON_FOCUS, ImageDecorator.IMG_LIST_HISTORY_ICON_FOCUSED);
            }
            item.setAddress(ResourceManager.getInstance().getStringConverter().convertAddress(address.getStop(), false));
            if (position != data.size() - 1)
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.DROPDOWN_NORMAL_ITEM_FOCUSED);
            }
            else
            {
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_UNFOCUS);
                item.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, NinePatchImageDecorator.DROPDOWN_BOTTOM_ITEM_FOCUSED);
            }
            return item;
        }

        public int getCount()
        {
            return data.size();
        }

        public int getItemType(int position)
        {
            return 0;
        }
    }

    class AddressFilter extends FrogKeywordFilter
    {

        private FrogList list;
        private Vector items;
        private CitizenTextField textField;
        
        public AddressFilter(CitizenTextField textField, FrogList list, Vector items)
        {
            this.list = list;
            this.items = items;
            this.textField = textField;
        }
        
        protected FilterResults performFilter(FilterRequest constraint)
        {
            FilterResults results = new FilterResults();
            Vector searchList = AddressListFilter.getFilteredList(constraint.keyword, items, false, false);
            results.constraint = constraint.keyword;
            results.values = searchList;
            results.count = searchList.size();
            filterData = searchList;
            return results;
        }

        protected void publishResults(FilterResults results)
        {
            final Vector data = (Vector) results.values;
            ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                public void run()
                {
                    if (list != null)
                    {
                        list.setAdapter(new AddressFilterAdapter(data));
                        if (textField != null)
                        {
                            if (data.size() > 0)
                                textField.showDropDown();
                            else
                                textField.hideDropDown();
                        }
                    }
                }
            });
        }
    }
}

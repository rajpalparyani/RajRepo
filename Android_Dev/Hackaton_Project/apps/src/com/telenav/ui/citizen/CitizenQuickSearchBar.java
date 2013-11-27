/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * CitizenQuickSearchInputBox.java
 *
 */
package com.telenav.ui.citizen;

import java.util.Vector;

import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.AddressDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.ITnTextChangeListener;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.frogui.widget.FrogAdapter;
import com.telenav.ui.frogui.widget.FrogList;
import com.telenav.ui.frogui.widget.FrogListItem;
import com.telenav.ui.frogui.widget.FrogTextField;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Albert Ma (byma@telenav.cn)
 *@date 2010-10-15
 */
public class CitizenQuickSearchBar extends TnLinearContainer 
{
  
    /**
     * the key value for {@link CitizenTextField} width.
     */
    public static final int KEY_TEXT_FIELD_WIDTH   = 1200;
    
    /**
     * the key value for {@link CitizenTextField} height.
     */
    public static final int KEY_TEXT_FIELD_HEIGHT  = 1201;
    
    /**
     * the key value of {@link CitizenTextField} unfocused background.
     */
    public static final int KEY_TEXT_FIELD_BG_UNFOCUS = 1202;
    
    /**
     * the key value of {@link CitizenTextField} focused background.
     */
    public static final int KEY_TEXT_FIELD_BG_FOCUS = 1203;
    
    /**
     * the key value of {@link FrogList} height for dropdownlist
     */
    public static final int KEY_DROP_DOWN_LIST_HEIGHT = 1204;
    
    /**
     * the key value of space width before home icon
     */
    public static final int KEY_NULLFIELD_BESIDE_HOME_WIDTH = 1205;
    
    protected CitizenTextField textField;
    protected int lat;
    protected int lon;
    
    protected FrogList dropDownList;
    protected RecentSearchListAdapter autoSuggestAdapter;

    protected TnUiArgAdapter focusItemBG;
    protected TnUiArgAdapter unfocusItemBG;

    protected TnUiArgAdapter listItemBottomUnFocusBG;

    protected TnUiArgAdapter listItemBottomFocusBG;
    
    protected ITnUiEventListener quickSearchTextChangeListener;
    
    protected ITnUiEventListener listCommandHandlerDelegate;

    protected int textChangeCommandId;

    protected AbstractTnFont listItemFont;

    protected int listFocusedColor;

    protected int listUnFocusedColor;
    
    protected static final int INPUT_TEXTFIELD = 10000;
    
    protected CitizenQuickSearchInputBoxDecorator decorator;

    private boolean isTriggerBySetText;
    
    private int leftCmdId = -1;
    
    private boolean isLeftButtonEnabled;
    
    /**
     * 
     * @param id
     * @param keypadConstraints
     * @param hint
     * @param iconSearch
     * @param iconBackSpace
     */
    public CitizenQuickSearchBar(int id, int keypadConstraints, String hint, AbstractTnImage iconSearch,  AbstractTnImage iconBackSpace, ITnUiEventListener textChangeListener)
    {
        super(id, false, AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
        
        decorator = new CitizenQuickSearchInputBoxDecorator();
        
        textField = new CitizenTextField("", 100, keypadConstraints, hint, INPUT_TEXTFIELD, iconSearch, iconBackSpace);
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, decorator.TEXT_FIELD_WIDTH);
        textField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, decorator.TEXT_FIELD_HEIGHT);
        textField.getTnUiArgs().put(CitizenTextField.KEY_PREFER_TEXTFIELD_HEIGHT, decorator.TEXT_FIELD_HEIGHT);
        textField.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, decorator.TEXT_FIELD_BG_FOCUSED);
        textField.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, decorator.TEXT_FIELD_BG_UNFOCUSED);
        int wpad = UiFactory.getInstance().getCharWidth()/4;
        if(wpad < 5)
        {
            wpad = 5;
        }
        textField.setPadding(wpad, 3, 3, 3);
        
        TextInputHandler handler = new TextInputHandler();
        textField.setTouchEventListener(handler);
        textField.setKeyEventListener(handler);
        textField.setTextChangeListener(handler);
        dropDownList = new FrogList(0);
        dropDownList.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, decorator.DROP_DOWN_HEIGHT);
        dropDownList.setCommandEventListener(handler);
        quickSearchTextChangeListener = textChangeListener;
        autoSuggestAdapter = new RecentSearchListAdapter(dropDownList);
        
        dropDownList.setAdapter(autoSuggestAdapter);
        this.add(textField);
        isLeftButtonEnabled = iconSearch != ImageDecorator.SEARCHBOX_MIC_DISABLED.getImage();
    }
    
    public void addComponentBeforeTextField(AbstractTnComponent component)
    {
        int index = this.indexOf(textField);
        if(index >= 0)
        {
            this.add(component, index);
        }
    }
    
    protected void paintBackground(AbstractTnGraphics graphics)
    {
        super.paintBackground(graphics);
    }
    
    public void setHintText(String hint)
    {
        this.textField.setHintText(hint);
    }

    public void setTextFieldCommandId(int leftCmdId, int rightCmdId)
    {
        this.textField.setBackspaceCommand(rightCmdId);
    }
    
    public void setTextFieldLeftCommandId(int leftCmdId)
    {
        this.leftCmdId = leftCmdId;
        this.textField.setSearchCommand(leftCmdId);
    }
    
    public void setTextFieldFont(AbstractTnFont font)
    {
        this.textField.setFont(font);
    }
    
    public void enableLeftButton(AbstractTnImage image)
    {
        this.textField.setSearchCommand(leftCmdId);
        textField.setFindIcon(image);
        isLeftButtonEnabled = true;
    }
    
    public void disableLeftButton(AbstractTnImage image)
    {
        this.textField.setSearchCommand(-1);
        textField.setFindIcon(image);
        isLeftButtonEnabled = false;
    }
    
    public boolean isLeftButtonEnabled()
    {
        return this.isLeftButtonEnabled;
    }
    
    class CitizenQuickSearchInputBoxDecorator implements ITnUiArgsDecorator
    {
        public TnUiArgAdapter TEXT_FIELD_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_TEXT_FIELD_WIDTH), this);
        public TnUiArgAdapter TEXT_FIELD_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_TEXT_FIELD_HEIGHT), this);
        public TnUiArgAdapter TEXT_FIELD_BG_UNFOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_TEXT_FIELD_BG_UNFOCUS), this);
        public TnUiArgAdapter TEXT_FIELD_BG_FOCUSED = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_TEXT_FIELD_BG_FOCUS), this);
        public TnUiArgAdapter DROP_DOWN_HEIGHT = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_DROP_DOWN_LIST_HEIGHT), this);
        public TnUiArgAdapter NULLFIELD_BESIDE_HOME_WIDTH = new TnUiArgAdapter(PrimitiveTypeCache.valueOf(KEY_NULLFIELD_BESIDE_HOME_WIDTH), this);
        public Object decorate(TnUiArgAdapter args)
        {
        	if (args == null || args.getKey() == null) return null;
        	
            int keyIndexValue = ((Integer)args.getKey()).intValue();
            switch(keyIndexValue)
            {
                case KEY_TEXT_FIELD_WIDTH:
                {
                	if (tnUiArgs.get(KEY_TEXT_FIELD_WIDTH) == null) 
                		return PrimitiveTypeCache.valueOf(0);
                	
                    return PrimitiveTypeCache.valueOf(tnUiArgs.get(KEY_TEXT_FIELD_WIDTH).getInt());
                }
                case KEY_TEXT_FIELD_HEIGHT:
                { 
                	if (tnUiArgs.get(KEY_TEXT_FIELD_HEIGHT) == null)
                		return PrimitiveTypeCache.valueOf(0);
                	
                    return PrimitiveTypeCache.valueOf(tnUiArgs.get(KEY_TEXT_FIELD_HEIGHT).getInt());
                }
                case KEY_TEXT_FIELD_BG_UNFOCUS:
                {
                	if (tnUiArgs.get(KEY_TEXT_FIELD_BG_UNFOCUS) == null) return null;
                	
                    return tnUiArgs.get(KEY_TEXT_FIELD_BG_UNFOCUS).getImage();
                }
                case KEY_TEXT_FIELD_BG_FOCUS:
                {
                	if (tnUiArgs.get(KEY_TEXT_FIELD_BG_FOCUS) == null) return null;
                	
                    return tnUiArgs.get(KEY_TEXT_FIELD_BG_FOCUS).getImage();
                }
                case KEY_DROP_DOWN_LIST_HEIGHT:
                {
                    if(dropDownList != null && ((AbstractTnContainer)dropDownList.get()).getChildrenSize() > 0)
                    {
                        return PrimitiveTypeCache.valueOf(autoSuggestAdapter.getCount() * ((AbstractTnContainer)dropDownList.get()).get(0).getPreferredHeight());
                    }
                    else
                    {
                        return PrimitiveTypeCache.valueOf(0);
                    }
                }
                case KEY_NULLFIELD_BESIDE_HOME_WIDTH:
                {
//                    if (tnUiArgs.get(KEY_TEXT_FIELD_HEIGHT) == null)
//                        return PrimitiveTypeCache.valueOf(0);
//                    
//                    return PrimitiveTypeCache.valueOf(tnUiArgs.get(KEY_TEXT_FIELD_HEIGHT).getInt()) * 1 / 3;
                    return 15;
                }
            }
            return null;
        }
        
    }

    public void setTextFieldBackgroundColor(int color)
    {
        this.textField.setBackgroundColor(color);
    }

    public String getTextFieldText()
    {
        String shownText = this.textField.getText();
        
        return shownText;
    }


    public void setTextFieldCommandEventListener(ITnUiEventListener uiEventListener)
    {
        this.textField.setCommandEventListener(uiEventListener);
    }

    public void setTextFieldBackMenu(int backCommandId)
    {
        TnMenu backMenu = new TnMenu();
        backMenu.add("", backCommandId);
        this.textField.getDropDownField().setMenu(backMenu, TYPE_BACK);
    }
    
    public void setAnchor(Stop stop)
    {
        this.lat = stop.getLat();
        this.lon = stop.getLon();
    }
    
    public void setDropDownNormalItemBG(TnUiArgAdapter focusItemBG, TnUiArgAdapter unfocusItemBG)
    {
        this.focusItemBG = focusItemBG;
        this.unfocusItemBG = unfocusItemBG;
    }
    
    public void setDropDownBottomItemBG(TnUiArgAdapter focusBG, TnUiArgAdapter unfocusBG)
    {
        this.listItemBottomFocusBG = focusBG;
        this.listItemBottomUnFocusBG = unfocusBG;
    }
    
    public void setListSelectedListener(ITnUiEventListener commandListener)
    {
        if(dropDownList != null)
        {
            listCommandHandlerDelegate = commandListener;
            
            textField.setDropDownList(dropDownList);
        }
    }
    
    public void setListSelectedMenu(TnMenu menu, int type)
    {
        if(dropDownList != null)
        {
            dropDownList.setMenu(menu, type);
           
        }
    }
    
    protected class RecentSearchListAdapter implements FrogAdapter
    {
        protected final FrogList list;
        protected boolean isProgressShown = false;
        protected boolean isEnable = true;

        protected Vector localQueryList;
        protected Object mutex = null;
        protected CitizenCircleAnimation progressAnimation;
        protected TnLinearContainer progressBar;
        protected final static int DROPDOWN_ITEMS_COUNTS_MAX = 5;
        protected AddressDao addressDao;
        public RecentSearchListAdapter(FrogList list)
        {
            this.list = list;
            localQueryList = new Vector();
            addressDao = DaoManager.getInstance().getAddressDao();
        }
        private synchronized void getRecentSearch()
        {
            clearUpLocalList();
            
            //FIXME: do we need handle fav/rencent?
            Vector recentSearchVector = addressDao.getRecentSearch();
            Vector recentVector = addressDao.getRecentAddresses();
            Vector favVector = addressDao.getFavorateAddresses();
            int i = 0;
            for(i = 0; i < recentSearchVector.size(); i++)
            {
                String recentString = (String)recentSearchVector.elementAt(i);
                localQueryList.addElement(recentString);
            } 
            for(i = 0; i < recentVector.size(); i ++)
            {
                Address address = (Address)recentVector.elementAt(i);
                String label = address.getLabel();
                if(label != null && label.length() > 0)
                {
                    localQueryList.addElement(label);
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null)
                    {
                        String stopName = stop.getFirstLine();
                        if(stopName != null && stopName.length() > 0)
                        {
                            localQueryList.addElement(label);
                        }
                    }
                }
                
            }
            for(i = 0; i < favVector.size(); i++)
            {
                Address address = (Address)favVector.elementAt(i);
                String label = address.getLabel();
                if(label != null && label.length() > 0)
                {
                    localQueryList.addElement(label);
                }
                else
                {
                    Stop stop = address.getStop();
                    if(stop != null)
                    {
                        String stopName = stop.getFirstLine();
                        if(stopName != null && stopName.length() > 0)
                        {
                            localQueryList.addElement(label);
                        }
                    }
                }
            }
        }
        
        public void enable(boolean isEnable)
        {
            this.isEnable = isEnable;
        }

        public AbstractTnComponent getComponent(int position,
                AbstractTnComponent convertComponent, AbstractTnComponent parent)
        {
            FrogListItem listItem = null;

            int maxCount = DROPDOWN_ITEMS_COUNTS_MAX;
            int showSize = Math.min(maxCount, localQueryList.size());
            
            if (position < showSize)
            {
                if (convertComponent != null && convertComponent instanceof FrogListItem)
                {
                    listItem = (FrogListItem) convertComponent;
                }
                else
                {
                    listItem = UiFactory.getInstance().createListItem(position);
                    listItem.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, decorator.TEXT_FIELD_WIDTH);
                    TnMenu tnMenu = list.getMenu(AbstractTnComponent.TYPE_CLICK);
                    listItem.setMenu(tnMenu, AbstractTnComponent.TYPE_CLICK);
                    if(listItemFont != null)
                    {
                        listItem.setFont(listItemFont);
                    }
                    listItem.setForegroundColor(listFocusedColor, listUnFocusedColor);
                }

                Object obj = localQueryList.elementAt(position);
                if (obj instanceof String)
                {
                    listItem.setText((String) obj);
                }
                else
                    return null;
                
                if(position == showSize -1)
                {
                    listItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, listItemBottomFocusBG);
                    listItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, listItemBottomUnFocusBG);
                }
                else
                {
                    listItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS, focusItemBG);
                    listItem.getTnUiArgs().put(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS, unfocusItemBG);
                }
                
                return listItem;
            }
            
            return null;
        }

        public int getCount()
        {
            if(isEnable)
                return Math.min(localQueryList.size(), DROPDOWN_ITEMS_COUNTS_MAX);
            else
                return 0;
        }

        public int getItemType(int position)
        {
            return 0;
        }

        private void clearUpLocalList()
        {
            this.localQueryList.removeAllElements();
        }
       
    }
    class TextInputHandler implements  ITnUiEventListener, ITnTextChangeListener
    {
        public boolean handleUiEvent(TnUiEvent tnUiEvent)
        {
            switch (tnUiEvent.getType())
            {
                case TnUiEvent.TYPE_TOUCH_EVENT:
                {
                    TnMotionEvent tnMotionEvent = tnUiEvent.getMotionEvent();
                    if (tnUiEvent.getComponent() instanceof FrogTextField
                            && tnMotionEvent.getAction() == TnMotionEvent.ACTION_DOWN)
                    {
                        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT,
                                CitizenQuickSearchBar.this);
                        TnCommandEvent commandEvent = new TnCommandEvent(
                                textChangeCommandId);
                        uiEvent.setCommandEvent(commandEvent);
                        uiEvent.setComponent(tnUiEvent.getComponent());
                        quickSearchTextChangeListener.handleUiEvent(uiEvent);
                    }
                    break;
                }
                case TnUiEvent.TYPE_KEY_EVENT:
                {
                    TnKeyEvent tnKeyEvent = tnUiEvent.getKeyEvent();
                    if (tnUiEvent.getComponent() instanceof FrogTextField
                            && tnKeyEvent.getAction() == TnMotionEvent.ACTION_UP && tnKeyEvent.getCode() == TnKeyEvent.KEYCODE_PAD_CENTER)
                    {
                        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT,
                            tnUiEvent.getComponent());
                        TnCommandEvent commandEvent = new TnCommandEvent(
                                textChangeCommandId);
                        uiEvent.setCommandEvent(commandEvent);
                        uiEvent.setComponent(tnUiEvent.getComponent());
                        quickSearchTextChangeListener.handleUiEvent(uiEvent);
                    }
                    break;
                }
                case TnUiEvent.TYPE_COMMAND_EVENT:
                {
                    if (tnUiEvent.getComponent() instanceof FrogListItem)
                    {
                        listCommandHandlerDelegate.handleUiEvent(tnUiEvent);
                    }
                    break;
                }
            }

            return false;
        }

        public void onTextChange(AbstractTnComponent component, String text)
        {
            if(isTriggerBySetText)
            {
                isTriggerBySetText = false;
                return;
            }
            if (component instanceof FrogTextField)
            {
                if (text.length() > 0)
                {
                    TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT,
                        component);
                    TnCommandEvent commandEvent = new TnCommandEvent(textChangeCommandId);
                    uiEvent.setCommandEvent(commandEvent);
                    quickSearchTextChangeListener.handleUiEvent(uiEvent);
                }
            }

        }
        
    }

    public void setTextChangeCommandId(int textChangeCommandId)
    {
        this.textChangeCommandId = textChangeCommandId;
    }
    
    public void setTextFieldText(String string)
    {
        this.isTriggerBySetText = true;
        this.textField.setText(string);
    }

    public void setHintTextColor(int color)
    {
        this.textField.setHintTextColor(color);
    }
    
    public void setTextColor(int color)
    {
        this.textField.setTextColor(color);
    }
    
    public void setListItemFont(AbstractTnFont font)
    {
        this.listItemFont = font;
    }
    
    public void setListForegroundColor(int focusedColor, int unFocusedColor)
    {
        this.listFocusedColor = focusedColor;
        this.listUnFocusedColor = unFocusedColor;
    }
    
    public void setDropDownPosition(TnUiArgs args)
    {
        this.textField.setDropDownListPostionArgs(args);
    }
    
    public void setCursorVisible(boolean visible)
    {
        this.textField.setCursorVisible(visible);
    }
}

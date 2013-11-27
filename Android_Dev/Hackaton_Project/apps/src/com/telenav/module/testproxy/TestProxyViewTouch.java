/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * TestProxyViewTouch.java
 *
 */
package com.telenav.module.testproxy;

import java.util.Vector;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.j2me.framework.util.ToStringUtils;
import com.telenav.module.upsell.UpsellOption;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.tnui.widget.TnTextField;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.CitizenAddressListItem;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.CitizenTextField;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogLabel;
import com.telenav.ui.frogui.widget.FrogMultiLine;
import com.telenav.ui.frogui.widget.FrogNullField;

/**
 *@author yning
 *@date 2012-11-21
 */
public class TestProxyViewTouch extends AbstractCommonView implements ITestProxyConstants
{

    public TestProxyViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected boolean updateScreen(int state, TnScreen screen)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popupContainer = null;
        switch(state)
        {
            case STATE_REQUESTING:
            {
                popupContainer = createRequestingProgress(state);
                break;
            }
        }
        return popupContainer;
    }
    
    protected TnPopupContainer createRequestingProgress(int state)
    {
        TnPopupContainer popupContainer = UiFactory.getInstance().createProgressBox(0, "requesting...");
        return popupContainer;
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        TnScreen screen = null;
        switch(state)
        {
            case STATE_MAIN:
            {
                screen = createMainScreen(state);
                break;
            }
            case STATE_RESULT:
            {
                screen = createResultScreen(state);
                break;
            }
            case STATE_UPSELL_MAIN:
            {
                screen = createUpsellMainPage(state);
                break;
            }
            case STATE_UPSELL_SHOW_OPTIONS:
            {
                screen = createUpsellInfoPage(state);
                break;
            }
        }
        return screen;
    }
    
    protected TnScreen createMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        final View contentView = AndroidCitizenUiHelper.addContentView(screen, R.layout.test_proxy_main);
        
        Spinner spinner = (Spinner) contentView.findViewById(R.id.id_spinner);
        
        String[] options = (String[])model.get(KEY_A_STRING_OPTIONS);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AndroidPersistentContext.getInstance().getContext(), android.R.layout.simple_spinner_item, options);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adapter);
        
        Button sendRequestBtn = (Button) contentView.findViewById(R.id.sendRequest);
        
        sendRequestBtn.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Spinner spinner = (Spinner) contentView.findViewById(R.id.id_spinner);
                int index = spinner.getSelectedItemPosition();
                int[] cmds = (int[]) model.get(KEY_A_INT_CMDS);
                if(cmds != null && index > -1 && index < cmds.length)
                {
                    TestProxyViewTouch.this.handleViewEvent(cmds[index]);
                }
            }
        });
        return screen;
    }

    protected TnScreen createResultScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        Vector requestResult = model.fetchVector(KEY_V_REQUEST_RESULT);
        
        StringBuffer sb = new StringBuffer();
        
        if(requestResult != null)
        {
            for(int i = 0; i < requestResult.size(); i++)
            {
                String pbString = ToStringUtils.toString(requestResult.elementAt(i));
                
                sb.append(pbString).append("\n");
            }
        }
        
        View contentView = AndroidCitizenUiHelper.addContentView(screen, R.layout.test_proxy_result);
        TextView textView = (TextView)contentView.findViewById(R.id.tv_result);
        textView.setText(sb.toString());
        
        return screen;
    }
    
    protected TnScreen createUpsellMainPage(int state)
    {
        model.put(KEY_B_IS_IN_UPSELL_SESSION, true);
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        FrogLabel title = UiFactory.getInstance().createLabel(0, "Upsell Test");
        
        screen.getTitleContainer().add(title);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Please input the PTN you want to use for upsell. \n Note: please prepare this account via BMS");
        
        screen.getContentContainer().add(multiLine);
        
        CitizenTextField tf = UiFactory.getInstance().createCitizenTextField("", 10, TnTextField.PHONENUMBER, "", ID_PTN_INPUT, null);
        tf.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((TestProxyUiDecorator)uiDecorator).PTN_INPUT_WIDTH);
        tf.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TestProxyUiDecorator)uiDecorator).PTN_INPUT_HEIGHT);
        screen.getContentContainer().add(tf);
        
        FrogButton button = UiFactory.getInstance().createButton(0, "submit");
        screen.getContentContainer().add(button);
        
        TnMenu menu = new TnMenu();
        menu.add("", CMD_UPSELL_SUBMIT);
        
        button.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        
        screen.getContentContainer().setCommandEventListener(this);
        
        return screen;
    }
    
    protected TnScreen createUpsellInfoPage(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        
        FrogLabel title = UiFactory.getInstance().createLabel(0, "Upsell");
        
        screen.getTitleContainer().add(title);
        
        FrogMultiLine multiLine = UiFactory.getInstance().createMultiLine(0, "Please select the offer you want to test");
        
        screen.getContentContainer().add(multiLine);
        
        Vector options = (Vector) model.get(KEY_V_UPSELL_OPTIONS);
        
        for(int i = 0; i < options.size(); i++)
        {
            UpsellOption op = (UpsellOption) options.elementAt(i);
            CitizenAddressListItem item = UiFactory.getInstance().createCitizenAddressListItem(ID_OPTIONS_BASE + i);
            item.setTitle(op.getDisplayName());
            item.setAddress(op.getPriceUnit() + op.getPriceValue());
            TnMenu menu = new TnMenu();
            menu.add("", CMD_UPSELL_PURCHASE);
            item.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
            screen.getContentContainer().add(item);
            FrogNullField nullField = UiFactory.getInstance().createNullField(0);
            nullField.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, ((TestProxyUiDecorator)uiDecorator).NULL_FIELD_HEIGHT);
            screen.getContentContainer().add(nullField);
        }
        
        screen.getContentContainer().setCommandEventListener(this);
        
        return screen;
    }
    
    @Override
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int type = tnUiEvent.getType();
        if(type == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            if(command == CMD_UPSELL_PURCHASE)
            {
                AbstractTnComponent component = tnUiEvent.getComponent();
                int id = component.getId();
                
                int index = id - ID_OPTIONS_BASE;
                
                model.put(KEY_I_OPTION_INDEX, index);
            }
        }
        return false;
    }
    
    @Override
    protected boolean prepareModelData(int state, int commandId)
    {
        
        switch(state)
        {
            case STATE_UPSELL_MAIN:
            {
                if(commandId == CMD_UPSELL_SUBMIT)
                {
                    TnScreen screen = getScreenByState(state);
                    if(screen != null)
                    {
                        CitizenTextField tf = (CitizenTextField)screen.getComponentById(ID_PTN_INPUT);
                        if(tf != null)
                        {
                            String ptn = tf.getText();
                            if(ptn == null || ptn.length() < 10)
                            {
                                return false;
                            }
                            model.put(KEY_S_PTN, ptn);
                        }
                    }
                }
                else if(commandId == CMD_COMMON_BACK)
                {
                    model.put(KEY_B_IS_IN_UPSELL_SESSION, false);
                }
                break;
            }
        }
        return true;
    }
    
    @Override
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // TODO Auto-generated method stub
        return false;
    }

}

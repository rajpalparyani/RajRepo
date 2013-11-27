/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * DashboardViewTouch.java
 *
 */
package com.telenav.module.drivingshare;


import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.AbstractCommonView;
import com.telenav.mvc.ICommonConstants;
import com.telenav.res.IStringPreference;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.CitizenProfileSwitcher;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author yren
 *@date 2013-6-3
 */
public class DrivingShareViewTouch extends AbstractCommonView implements IDrivingShareConstants
{
    ViewGroup rootViewGroup;
    
    CitizenProfileSwitcher switcher;
    
    public DrivingShareViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }
    
    protected TnPopupContainer createPopup(int state)
    {
        return null;
    }
    
    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_MAIN:
            {
                return createMainScreen(state);
            }
        }
        return null;
    }

    protected boolean updateScreen(int state, TnScreen screen)
    {
        return false;
    }

    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        return CMD_NONE;
    }
    
    protected TnScreen createMainScreen(int state)
    {
        CitizenScreen screen = UiFactory.getInstance().createScreen(state);
        removeMenuById(screen, ICommonConstants.CMD_COMMON_PREFERENCE);
        rootViewGroup = (ViewGroup) screen.getContentContainer().getNativeUiComponent();
        screen.getRootContainer().setSizeChangeListener(this);
        screen.getRootContainer().setCommandEventListener(this);
        screen.getRootContainer().setKeyEventListener(this);
        screen.getRootContainer().setBackgroundColor(UiStyleManager.getInstance().getColor(UiStyleManager.SUMMARY_PROFILE_CONTAINER_BG_COLOR));
        View contentView = AndroidCitizenUiHelper.addContentView(screen, R.layout.share_driving_setting);
        TextView titleView = (TextView) contentView.findViewById(R.id.commonTitle0TextView);
        String sharingTitle = ResourceManager.getInstance().getText(IStringPreference.PREFERENCE_SHARING,
            IStringPreference.FAMILY_PREFERENCE);
        titleView.setText(sharingTitle);
        boolean boolDefalutSwitchValue = this.model.getBool(KEY_B_SHARE_REAL_TIME_ENABLE);
        final ViewGroup switcherContainer = (ViewGroup) rootViewGroup.findViewById(R.id.drivingShare0switcherContainer);
        switcher = UiFactory.getInstance().createCitizenProfileSwitcher(ID_DRIVING_SHARE_SWITCHER,
            boolDefalutSwitchValue, "");
        switcher.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
        switcher.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        switcher.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int width = ImageDecorator.PROFILE_SWITCH_BG.getImage().getWidth();
                    return PrimitiveTypeCache.valueOf(width);
                }
            }));
        switcher.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int height = 0;
                    if (switcherContainer.getParent() instanceof View)
                    {
                        height = ImageDecorator.PROFILE_SWITCH_BG.getImage().getHeight();
                    }
                    return PrimitiveTypeCache.valueOf(height);
                }
            }));
        switcher.setCommandEventListener(this);
        switcherContainer.addView((View) switcher.getNativeUiComponent());
        setTexViewWidth();
        return screen;
    }
    
    protected void setTexViewWidth()
    {
        if(rootViewGroup == null)
        {
            return;
        }
        TextView switcherTextView = (TextView) rootViewGroup.findViewById(R.id.drivingShare0TextView);
        Context context = AndroidPersistentContext.getInstance().getContext();
        Resources resource = context.getResources();
        float parentLeftPadding = resource.getDimension(R.dimen.drivingshare0ContentLeftPadding);
        float parentRightPadding = resource.getDimension(R.dimen.drivingshare0ContentRigntPadding);
        float linearLeftPadding = resource.getDimension(R.dimen.drivingshare0SubTitleHorizontalMargin);
        float linearRightPadding = resource.getDimension(R.dimen.drivingshare0SubTitleHorizontalMargin);
        float gapPadding = resource.getDimension(R.dimen.drivingshare0TextViewSwitcherGap);
        float fPx = parentLeftPadding + parentRightPadding + linearLeftPadding + linearRightPadding + gapPadding;
        int iPx = (int) (fPx + 0.5f);
        switcherTextView.setWidth(AppConfigHelper.getDisplayWidth() - switcher.getPreferredWidth() - iPx);
    }
    
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        int state = model.getState();

        switch (state)
        {
            case STATE_MAIN:
            {
                AbstractTnComponent component = tnUiEvent.getComponent();
                if (component instanceof CitizenProfileSwitcher)
                {
                    CitizenProfileSwitcher switcher = (CitizenProfileSwitcher) component;
                    TnCommandEvent event = tnUiEvent.getCommandEvent();
                    if (event != null && event.getCommand() == CMD_CHANGE_SWITCH)
                    {
                        model.put(KEY_B_SHARE_REAL_TIME_ENABLE, switcher.isSwitchOn());
                    }
                }
                break;
            }
        }
        return false;
    }

    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    protected void activate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
    }

    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
    }
    
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        switch (model.getState())
        {
            case STATE_MAIN:
            {
                setTexViewWidth();
                break;
            }
        }
    }
}

/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * AndroidCitizenUiHelper.java
 *
 */
package com.telenav.ui.citizen.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.module.AppConfigHelper;
import com.telenav.mvc.AbstractBaseView;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnContainer;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author hchai
 * @date 2012-2-15
 */
public class AndroidCitizenUiHelper
{
    public static final LayoutParams FILL_PARENT_PARAMS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    public static final LayoutParams WRAP_CONTENT_PARAMS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    public static View inflateLayout(int resource)
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();

        View view = View.inflate(context, resource, null);
        return view;
    }

    public static Drawable inflateDrawable(int resource)
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
        Drawable drawable = context.getResources().getDrawable(resource);
        
        return drawable;
    }
    
    public static View inflateLayout(int layoutId, ViewGroup root)
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
        View view = View.inflate(context, layoutId, root);

        return view;
    }

    public static void setOnClickCommand(final AbstractBaseView mvcView, final View view, final int command)
    {
        view.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                triggerCommand(mvcView, view, command);
            }
        });
    }
    
    public static void setOnClickCommand(final AbstractBaseView mvcView, final View view, final int[][]commandTable)
    {
        for(int i = 0; i < commandTable.length; i++ )
        {
             AndroidCitizenUiHelper.setOnClickCommand(mvcView, (View)view.findViewById(commandTable[i][0]),  commandTable[i][1]);   
        }
    }

    public static void triggerCommand(final AbstractBaseView mvcView, View view, final int command)
    {
        AbstractTnComponent mockComponent = new AbstractTnComponent(view.getId()){
            @Override
            protected void paint(AbstractTnGraphics graphics)
            {
                
            }};
        mockComponent.setCookie(view);
        TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, mockComponent);
        uiEvent.setCommandEvent(new TnCommandEvent(command));
        mvcView.handleUiEvent(uiEvent);
    }
    
    public static View addContentView(CitizenScreen screen, int layoutId,LayoutParams params)

    {
        View view = inflateLayout(layoutId);
        decorateScreen(screen);
        addContentView(screen.getContentContainer(),view,params);
        return view;

    }
    
    public static View addContentView(CitizenScreen screen, int layoutId)

    {
        return addContentView(screen,layoutId,FILL_PARENT_PARAMS);
    }

    public static void removeContentView(CitizenScreen screen)
    {
        AbstractTnContainer container = screen.getContentContainer();
        ((ViewGroup) container.getNativeUiComponent()).removeAllViews();
    }
    
    private static void decorateScreen(CitizenScreen screen)
    {
        TnUiArgs contentUiArgs = screen.getContentContainer().getTnUiArgs();
        contentUiArgs.remove(TnUiArgs.KEY_PREFER_WIDTH);
        contentUiArgs.remove(TnUiArgs.KEY_PREFER_HEIGHT);
        contentUiArgs.put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(
                PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
                {
                    public Object decorate(TnUiArgAdapter args)
                    {
                        return PrimitiveTypeCache.valueOf(AppConfigHelper
                                .getDisplayWidth());
                    }
                }));
        contentUiArgs.put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(
            PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    return PrimitiveTypeCache.valueOf(AppConfigHelper
                            .getDisplayHeight());
                }
            }));
    }


    public static View addContentView(AbstractTnContainer container, View view,LayoutParams params)
    {
        ((ViewGroup) container.getNativeUiComponent()).addView(view,params);
        return view;
    }

    public static View addContentView(AbstractTnContainer container, int layoutId)
    {
        ViewGroup viewGroup = (ViewGroup) container.getNativeUiComponent();
        View view = inflateLayout(layoutId, viewGroup);

        return view;
    }

    public static Resources getResources()
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
        return (null != context) ? context.getResources() : null;
    }

    /**
     * get the color define in colors.xml file
     * @param id
     * @return
     */
    public static int getResourceColor(int id)
    {
        int color = 0xFFFFFFFF;
        Resources res = getResources();
        if (null != res)
        {
            color = res.getColor(id);
        }
        return color;
    }
    /**
     * get the dimension define in dimens.xml file
     * @param id
     * @return
     */
    public static float getResourceDimension(int id)
    {
        float dimension = -1;
        Resources res = getResources();
        if (null != res)
        {
            dimension = res.getDimension(id);
        }
        return dimension;
    }
    /**
     * get drawable from resource folder,return null when no image with the given id
     * @param id
     * @return
     */
    public static Drawable getResourceImageDrawable(int id)
    {
        Drawable image = null;
        Resources res = getResources();
        if (null != res)
        {
            image = res.getDrawable(id);
        }
        return image;
    }
    /**
     * get string define in resource
     * @param id
     * @return
     */
    public static String getRecourceString(int id)
    {
        String str = "";
        Resources res = getResources();
        if (null != res)
        {
            str = res.getString(id);
        }
        return str;
    }
    
    
    public static int getPixelsByDip(int dipValue)
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
        
         // returns the number of pixels for 123.4dip
        int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                             (float) dipValue, context.getResources().getDisplayMetrics());
        
        return value;
    }
    
    public static int getPixelsByDensity(int pixel)
    {
        float ratio = ((AndroidUiHelper)AbstractTnUiHelper.getInstance()).getVisionRatio() / 2;
        if (ratio > 1)
        {
            ratio = 1;
        }
        return (int) (pixel * ratio);
    }
    
}

/**
 * 
 */
package com.telenav.ui.citizen;

import com.telenav.htmlsdk.IHtmlSdkServiceHandler;
import com.telenav.module.AppConfigHelper;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnPrivateEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.TnUiTimer;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.widget.TnWebBrowserField;
import com.telenav.ui.UiStyleManager;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author jbtian
 * 
 */
public class CitizenWebComponent extends TnWebBrowserField implements ITnUiArgsDecorator
{
    public final static int LOAD_FAILED = 1;

    public final static int LOAD_SUCCESSFULLY = 2;
    
    public static final int CMD_LOAD_STATUS_CHANGE = 20001;

    public final static int SET_HANDLER_LOAD_URL = 50000030;
    public final static int RESET = 50000032;
    public final static int RESET_LOAD_STATUS = 50000033;
    public final static int ENABLE_TEMP_CACHE = 50000034;
    public final static int SET_DATA_TO_TEMP_CACHE = 50000035;
    public final static int GET_DATA_TO_TEMP_CACHE = 50000036;
    public final static int INIT_DEFAULT_ZOOM_DENSITY = 50000037;
    public final static int SET_GESTURE_LISTENER = 50000038;
    public final static int DESTROY = 50000039;
    public final static int SET_BACKGROUND_COLOR = 50000040;
    public final static int SET_SCROLLABLE = 50000041;
    public final static int FREE_MEMORY = 50000042;
    public final static int SET_ERROR_MESSAGE = 50000043;
    public final static int SET_LAYER_TYPE = 50000044;
    public final static int GET_DEVICE_USER_AGENT = 50000045;
    public final static int IS_UPDATE_CONTENT = 50000046;
    public final static int SET_DEVICE_USER_AGENT = 50000047;
    public final static int WEB_VIEW_SHOW = 50000048;
    
    public final static int LAYER_TYPE_NONE = 0;
    public final static int LAYER_TYPE_SOFTWARE = 1;
    public final static int LAYER_TYPE_HARDWARE = 2;
    

    protected CitizenCircleAnimation animation;

    protected long lastNotifyTimestamp;
    protected boolean needWaitProgress;
    
    protected int status = -1;
    protected boolean hasInitAnimation;
    
    protected boolean disableLongClick;
    
    /**
     * @param id
     */
    public CitizenWebComponent(int id, CitizenCircleAnimation animation, boolean needWaitProgress)
    {
        super(id);

        this.animation = animation;
        this.needWaitProgress = needWaitProgress;
    }
    
    public void setDisableLongClick(boolean disableLongClick)
    {
        this.disableLongClick = disableLongClick;
    }
    
    public boolean isDisableLongClick()
    {
        return this.disableLongClick;
    }
    
    public void setGestureListener(IGestureListener listener)
    {
        this.getNativeUiComponent().callUiMethod(SET_GESTURE_LISTENER, new Object[] {listener});
    }

    public boolean needWaitProgress()
    {
        return this.needWaitProgress;
    }
    
    public void setHtmlSdkServiceHandler(IHtmlSdkServiceHandler handler)
    {
        this.nativeUiComponent.callUiMethod(SET_HANDLER_LOAD_URL, new Object[]
        { handler });
    }
    
    public void initDefaultZoomDensity(){
    	this.nativeUiComponent.callUiMethod(INIT_DEFAULT_ZOOM_DENSITY, new Object[0]);
    }
    
    public void reset(){
    	this.nativeUiComponent.callUiMethod(RESET, new Object[0]);
    }
    
    public void enableTempCache(boolean isEnable){
    	this.nativeUiComponent.callUiMethod(ENABLE_TEMP_CACHE, new Object[]{PrimitiveTypeCache.valueOf(isEnable)});
    }
    
    public void setDataToTempCache(String key, String value){
    	this.nativeUiComponent.callUiMethod(SET_DATA_TO_TEMP_CACHE, new Object[]{key, value});
    }
    
    public void destroy(){
    	this.nativeUiComponent.callUiMethod(DESTROY, new Object[0]);
    }
    
    public void freeMemory()
    {
        this.nativeUiComponent.callUiMethod(FREE_MEMORY, new Object[0]);
    }
    
    public String getDataFromTempCache(String key)
    {
       Object strObject = this.nativeUiComponent.callUiMethod(GET_DATA_TO_TEMP_CACHE, new Object[]{key});
       if(strObject instanceof String)
       {
           String value = (String)strObject;
           return value;
       }
       else
       {
           return null;
       }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.telenav.tnui.core.AbstractTnComponent#paint(com.telenav.tnui.graphics.AbstractTnGraphics)
     */
    protected void paint(AbstractTnGraphics graphics)
    {
        if (this.animation != null)
        {
            final int minComponentValue = Math.min(this.getWidth(), this.getHeight());
            
            animation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int pw = PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 50 / 100) / 2;
                    if(pw >= minComponentValue)
                    {
                        return minComponentValue - 20;
                    }
                    else
                    {
                        return pw;
                    }
                }
            }));

            animation.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT, new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int ph = PrimitiveTypeCache.valueOf(AppConfigHelper.getMinDisplaySize() * 60 / 100) / 2;
                    if(ph >= minComponentValue)
                    {
                        return minComponentValue - 20;
                    }
                    else
                    {
                        return ph;
                    }
                }
            }));
            
            int minAnimationValue = Math.min(animation.getPreferredWidth(), animation.getPreferredHeight());
            //float stretchRatio = ISpecialImageRes.denominator==0?1:An
            int[] dropSizes = new int[4];
             
            int maxDropSize =  minAnimationValue * 3 / 40;
            dropSizes[0] = maxDropSize;
            dropSizes[1] = maxDropSize-1>0?maxDropSize-1:1;
            dropSizes[2] = maxDropSize-2>0?maxDropSize-2:1;
            dropSizes[3] = maxDropSize-3>0?maxDropSize-3:1;
            
//            if(minAnimationValue <= 80)
//            {
//                dropSizes[0] = (int)Math.floor(6 * stretchRatio);
//                dropSizes[1] = (int)Math.floor(5 * stretchRatio);
//                dropSizes[2] = (int)Math.floor(4 * stretchRatio);
//                dropSizes[3] = (int)Math.floor(3 * stretchRatio);
//            }
            animation.setDropSizes(dropSizes);
            int oldColor = graphics.getColor();
            graphics.setColor(UiStyleManager.getInstance().getColor(UiStyleManager.BG_COLOR_WH));
            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            graphics.setColor(oldColor);
            int tx = (this.getWidth() - this.animation.getPreferredWidth()) / 2;
            int ty = (this.getHeight() - this.animation.getPreferredHeight()) / 2;
            graphics.translate(tx, ty);
            this.animation.draw(graphics);
            graphics.translate(-tx, -ty);
        }
    }

    protected void initDefaultStyle()
    {

    }

    public void enableAnimation(boolean isEnabled){
        if(this.animation != null)
        {
            this.animation.enable(isEnabled);
            if (isEnabled)
            {
                TnUiTimer.getInstance().addReceiver(this, 100);
            }
            else
            {
                TnUiTimer.getInstance().removeReceiver(this);
            }
        }
    }
    
    public void loadUrl(String url)
    {
        // isUpdateContent if it's true, it means current webview has already loaded this url before. 
        // Thus it won't load the whole page again, but only call javascript to update the content.
        // So there is no need to reset the status. The status will cause paint animation.
        // But in this case, the animation will not be enabled. Only a blank white page.
        Boolean isUpdateContentBoolean = (Boolean)this.nativeUiComponent.callUiMethod(IS_UPDATE_CONTENT, new Object[]{url});
        boolean isUpdateContent = isUpdateContentBoolean.booleanValue();
        
        if(!isUpdateContent)
        {
            status = -1;

            // --------------------------------------------------
            String errorMessage = ResourceManager.getInstance().getText(IStringCommon.RES_SERVER_ERROR,
                IStringCommon.FAMILY_COMMON);
            String okBtnStr = ResourceManager.getInstance().getText(IStringCommon.RES_BTTN_OK, IStringCommon.FAMILY_COMMON);

            setErrorMessage(okBtnStr, errorMessage);

            this.nativeUiComponent.callUiMethod(RESET_LOAD_STATUS, new Object[0]);
        }
        
        this.nativeUiComponent.callUiMethod(METHOD_LOAD_URL, new Object[]
        { url });
        
        if(!isUpdateContent)
        {
            TnUiTimer.getInstance().addReceiver(this, 100);
        }
    }

    public Object decorate(TnUiArgAdapter args)
    {
        if (args.getKey().equals(new Integer(TnUiArgs.KEY_PREFER_WIDTH)))
        {
            return PrimitiveTypeCache.valueOf(this.getWidth());
        }
        else if (args.getKey().equals(new Integer(TnUiArgs.KEY_PREFER_HEIGHT)))
        {
            return PrimitiveTypeCache.valueOf(this.getHeight());
        }

        return null;
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if(animation != null)
        {
            animation.requestLayout();
            animation.reset();
        }
    }
    
    protected boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_PRIVATE_EVENT:
            {
                if (tnUiEvent.getPrivateEvent().getAction() == TnPrivateEvent.ACTION_TIMER)
                {
                    if(this.animation != null)
                    {
                        this.animation.dispatchUiEvent(tnUiEvent);
                    }
                    this.requestPaint();
                    return true;
                } 
                break;
            }
        }
        return false;
    }

    public void updateLoadStatus(int status)
    {
        this.status = status;
        if (status == LOAD_FAILED || status == LOAD_SUCCESSFULLY)
        {
            TnUiTimer.getInstance().removeReceiver(this);
            if(commandListener != null)
            {
                TnUiEvent loadStatusUpdate = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
                loadStatusUpdate.setCommandEvent(new TnCommandEvent(CMD_LOAD_STATUS_CHANGE));
                commandListener.handleUiEvent(loadStatusUpdate);
            }
        }
    }
    
    public void setBackgroundColor(int backgroundColor)
    {
        this.nativeUiComponent.callUiMethod(SET_BACKGROUND_COLOR, new Object[]{PrimitiveTypeCache.valueOf(backgroundColor)});
    }
    
    public void setScrollable(boolean scrollable)
    {
        this.nativeUiComponent.callUiMethod(SET_SCROLLABLE, new Object[]{PrimitiveTypeCache.valueOf(scrollable)});
    }
    
    public int getStatus()
    {
        return this.status;
    }
    
    protected void onUndisplay()
    {
        super.onUndisplay();
        TnUiTimer.getInstance().removeReceiver(this);
    }
    
    public void setErrorMessage(String okButtonStr, String errorMessage)
    {
        this.nativeUiComponent.callUiMethod(SET_ERROR_MESSAGE, new Object[]{okButtonStr, errorMessage});
    }
    
    public void setLayerType(int LayerType)
    {
        this.nativeUiComponent.callUiMethod(SET_LAYER_TYPE, new Object[]{PrimitiveTypeCache.valueOf(LayerType)});
    }
    
    public String getUserAgent()
    {
		Object device_ua = this.nativeUiComponent.callUiMethod(GET_DEVICE_USER_AGENT, null);
		if (device_ua != null)
		{
			return String.valueOf(device_ua);
		}
        return "";
    }
    
    public boolean isUpdateContent(String url)
    {
        Boolean isUpdateContent = (Boolean)this.nativeUiComponent.callUiMethod(IS_UPDATE_CONTENT, new Object[]{url});
        return isUpdateContent.booleanValue();
    }
    
    public void setUserAgent(String ua)
    {
        this.nativeUiComponent.callUiMethod(SET_DEVICE_USER_AGENT, new Object[]{ua});
    }
    
    public void notifyWebViewShow()
    {
        this.nativeUiComponent.callUiMethod(WEB_VIEW_SHOW, null);
    }
}

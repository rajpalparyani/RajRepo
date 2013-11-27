/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * FriendAnnotation.java
 *
 */
package com.telenav.ui.citizen.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.dwf.aidl.Friend;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewData.AnnotationType;
import com.telenav.navsdk.events.MapViewEvents.MapViewAddAnnotationRequest;
import com.telenav.navsdk.events.MapViewEvents.MapViewUpdateAnnotationRequest;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.graphics.AbstractTnFont;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.graphics.TnRect;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 * @author fangquanm
 * @date Jul 11, 2013
 */
public class FriendAnnotation extends AbstractAnnotation
{
    private static final int POI_POPUP_ANNOTATION_LAYER = IMapContainerConstants.NAV_SDK_Z_ORDER_USER_DEFINED;

    private Friend friend;

    private long annotationId;

    private int layer;

    private long viewId;

    private double lat;

    private double lon;

    private int unfocusedHeight;

    private int unfocusedWidth;

    private long graphicId = -1;
    
    private TnRect bubbleRectSrc;

    private TnRect bubbleRectDest;
    
    private String friendKey;
    
    private Bitmap bubbleBm;
    
    private AnnotationType annotationType;
    
    private String annotationStyle;
    
    public final static String STYLE_POPUP_POI_ANNOTATION = "screen_annotations.poipopup_screen_annotation";
    public final static String STYLE_FLAG_SCREEN_ANNOTATION = "screen_annotations.flag_screen_annotation";

    public FriendAnnotation(Friend friend, AnnotationType annotationType, String annotationStyle)
    {
        super((int) ICommonConstants.ANOTATION_COMPONENT_TRANSLATE);

        this.friend = friend;

        this.viewId = MapContainer.getInstance().getViewId();

        this.lat = this.friend.getLat();
        this.lon = this.friend.getLon();
        
        this.friendKey = this.friend.getKey();
        
        this.annotationType = annotationType;
        this.annotationStyle = annotationStyle;
        
        bubbleBm = BitmapFactory.decodeResource(AndroidPersistentContext.getInstance().getContext().getResources(),
            R.drawable.drive_with_friends_map_bubble_unfocused);
        
        int bubbleWidth = bubbleBm.getWidth();
        int bubbleHeight = bubbleBm.getHeight();
        
        int realWidth;
        int realHeight;
        
        if(isSpriteAnnotation())
        {
            realWidth = AndroidCitizenUiHelper.getPixelsByDensity(52);
            realHeight= AndroidCitizenUiHelper.getPixelsByDensity(60);
        }
        else
        {
            realWidth = AndroidCitizenUiHelper.getPixelsByDensity(bubbleWidth);
            realHeight= AndroidCitizenUiHelper.getPixelsByDensity(bubbleHeight);
        }
        
        bubbleRectSrc = new TnRect(0, 0, bubbleWidth, bubbleHeight);
        bubbleRectDest = new TnRect(0, 0, realWidth, realHeight);
        
        unfocusedWidth = realWidth;
        unfocusedHeight = realHeight;
        
    }
    
    protected boolean isSpriteAnnotation()
    {
        if (annotationType != null && annotationType.getNumber() == AnnotationType.AnnotationType_Sprite_VALUE)
        {
            return true;
        }

        return false;
    }
    
    protected void paint(AbstractTnGraphics g)
    {
        AbstractTnImage bubbleImage = AbstractTnUiHelper.getInstance().createImage(bubbleBm);
        
        g.drawImage(bubbleImage, bubbleRectSrc, bubbleRectDest);
        
        Bitmap bm = BitmapFactory.decodeResource(AndroidPersistentContext.getInstance().getContext().getResources(),
            R.drawable.drive_with_friends_default_portrait_icon_unfocused);
        
        AbstractTnImage image = AbstractTnUiHelper.getInstance().createImage(bm);
        if (this.friend.getImage() != null)
        {
            image = AbstractTnUiHelper.getInstance().createImage(this.friend.getImage());
        }
        TnRect src = new TnRect();
        src.left = 0;
        src.top = 0;
        src.right = image.getWidth();
        src.bottom = image.getHeight();
        TnRect dst = new TnRect();
        
        int iconWidth;
        
        if(isSpriteAnnotation())
        {
            iconWidth = AndroidCitizenUiHelper.getPixelsByDensity(25);
        }
        else
        {
            iconWidth = AndroidCitizenUiHelper.getPixelsByDensity(48);
        }
        
        dst.left = 0;
        dst.top = 0;
        dst.right = iconWidth;
        dst.bottom = iconWidth;

        int translateX = (this.getWidth() - dst.width()) / 2;
        int translateY = AndroidCitizenUiHelper.getPixelsByDensity(15);
        if(isSpriteAnnotation())
        {
            translateY = AndroidCitizenUiHelper.getPixelsByDensity(7);
        }

        g.translate(translateX, translateY);
        g.drawImage(image, src, dst);
        g.translate(-translateX, -translateY);

        AbstractTnFont contentFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_FRIEND_BUBBLE_TEXT);
        if(isSpriteAnnotation())
        {
            contentFont = UiStyleManager.getInstance().getFont(UiStyleManager.FONT_FRIEND_BUBBLE_TEXT_MOVING_MAP);
        }
        g.setFont(contentFont);
        g.setColor(0xffffffff);

        int padding = AndroidCitizenUiHelper.getPixelsByDensity(4);
        if(isSpriteAnnotation())
        {
            padding = AndroidCitizenUiHelper.getPixelsByDensity(2);
        }
        int paddingHorizontal = AndroidCitizenUiHelper.getPixelsByDensity(4);
        if(isSpriteAnnotation())
        {
            paddingHorizontal = AndroidCitizenUiHelper.getPixelsByDensity(2);
        }
        String label = getLabelForPaint(this.friend.getName(), this.getWidth() - paddingHorizontal * 2, contentFont, false);
        g.drawString(label, (this.getWidth() - g.getFont().stringWidth(label)) / 2, translateY + dst.height() + padding,
            AbstractTnGraphics.LEFT | AbstractTnGraphics.TOP);
    }

    public boolean handleDownEvent(int insideX, int insideY)
    {
        return true;
    }

    public boolean handleUpEvent(int insideX, int insideY)
    {
        return true;
    }

    public boolean handleMoveEvent(int x, int y)
    {
        return false;
    }

    public boolean handleClickEvent(int insideX, int insideY)
    {
        return true;
    }

    public AbstractTnImage createAnnotationImage()
    {
        AbstractTnImage image = AbstractTnUiHelper.getInstance().createImage(getWidth(), getHeight());
        image.clear(0x0);
        AbstractTnGraphics g = image.getGraphics();
        paint(g);

        return image;
    }

    public int getWidth()
    {
        return unfocusedWidth;
    }

    public int getHeight()
    {
        return unfocusedHeight;
    }

    public MapViewUpdateAnnotationRequest.Builder updateGraphic()
    {
        layer = POI_POPUP_ANNOTATION_LAYER;

        MapViewUpdateAnnotationRequest.Builder builder = MapViewUpdateAnnotationRequest.newBuilder();
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        graphicId = generateGraphicId();
        mapContainer.loadAsset(getViewName(), getWidth(), getHeight(), image, "" + graphicId);

        builder.setViewName(getViewName());
        builder.setAnnotationName("" + this.annotationId);
        builder.setPivotX(0.5f);
        builder.setPivotY(0);
        builder.setStyle(annotationStyle);
        builder.setType(annotationType);
        builder.setZorder(layer);
        builder.setAssetName("" + this.graphicId);
        return builder;
    }

    public long addToMap()
    {
        MapContainer mapContainer = MapContainer.getInstance();
        AbstractTnImage image = createAnnotationImage();
        if (graphicId != -1)
        {
            mapContainer.unloadAsset("" + viewId, "" + graphicId);
        }
        graphicId = generateGraphicId();
        mapContainer.loadAsset("" + viewId, getWidth(), getHeight(), image, "" + graphicId);

        annotationId = this.createAnnotation();
        return annotationId;
    }

    public boolean isSetFocused()
    {
        return false;
    }

    public double getLat()
    {
        return lat;
    }

    public double getLon()
    {
        return lon;
    }

    public long getGraphicId()
    {
        return this.graphicId;
    }

    public long getPickableIdNum()
    {
        return this.hashCode();
    }

    AbstractTnImage getImage()
    {
        return createAnnotationImage();
    }

    MapViewAddAnnotationRequest.Builder generaterBuilder()
    {
        layer = POI_POPUP_ANNOTATION_LAYER;
        MapViewAddAnnotationRequest.Builder builder = MapViewAddAnnotationRequest.newBuilder();
        com.telenav.navsdk.events.PointOfInterestData.Location.Builder locationBuilder = com.telenav.navsdk.events.PointOfInterestData.Location
                .newBuilder();
        locationBuilder.setLatitude(lat);
        locationBuilder.setLongitude(lon);
        builder.setVisible(true);
        builder.setLocation(locationBuilder.build());
        builder.setPivotX(0.5f);
        builder.setPivotY(0);
        builder.setStyle(annotationStyle);
        builder.setZorder(layer);
        builder.setClickable(false);
        
        builder.setWidth(getWidth());
        builder.setHeight(getHeight());
        builder.setType(annotationType);
        builder.setAssetName("" + this.graphicId);
        return builder;
    }

    String getViewName()
    {
        return "" + this.viewId;
    }

    public String getLabelForPaint(String label, int labelW, AbstractTnFont font, boolean isScrolling)
    {
        String tempLabel = label;
        if (!isScrolling)
        {
            if (font.stringWidth(tempLabel) > labelW)
            {
                int len = font.stringWidth("...");
                int i;
                for (i = 0; i < label.length(); i++)
                {
                    len += font.charWidth(label.charAt(i));
                    if (len >= labelW)
                    {
                        break;
                    }
                }
                tempLabel = label.substring(0, i);
                tempLabel += "...";
            }
        }

        return tempLabel;
    }
    
    public String getUserKey()
    {
        return friendKey;
    }
}

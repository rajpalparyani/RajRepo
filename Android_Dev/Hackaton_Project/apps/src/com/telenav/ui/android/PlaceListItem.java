/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * PoiResultItem.java
 *
 */
/**
 * 
 */
package com.telenav.ui.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 * @author jpwang
 * @date 2013-2-17
 */
public class PlaceListItem extends LinearLayout
{
    // Normal item without selected
    protected TextView indicator;

    protected TextView brandName;
    
    protected TextView badge;

    protected TextView address;
    
    protected TextView category;

    protected TextView lastLine;

    protected TextView distance;
    
    protected TextView ads;

    // Normal item with selected
    protected TextView selectedIndicator;

    protected TextView selectedBrandName;

    protected TextView selectedAddress;

    protected TextView selectedLastLine;

    protected TextView selectedDistance;

    protected TextView selectedDesc;

    protected ImageView selectedCoupon;

    protected ImageView selectedDeals;
    
    protected TextView selectedAds;
    
    // Sponsor item
    protected TextView desc;

    protected ImageView coupon;

    protected ImageView deals;

    protected TnMenu tnMenu;
    
    public static final int TYPE_SPONSOR = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_CATEGORY = 2;
    public static final int TYPE_GROUP = 3;
    
    private boolean isIndicatorSizeInitialed = false;

    public void setTnMenu(TnMenu tnMenu)
    {
        this.tnMenu = tnMenu;
    }

    public PlaceListItem(Context context)
    {
        super(context);
    }

    public PlaceListItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    protected void onFinishInflate()
    {
        super.onFinishInflate();
    }
    
    public void init(int type)
    {
        switch (type)
        {
            case TYPE_SPONSOR:
            {
                indicator=initTextView(R.id.placeList0AdsItemNumberIndicator);
                brandName=initTextView(R.id.placeList0AdsItemBrandName);
                address=initTextView(R.id.placeList0AdsItemAddress);
                lastLine=initTextView(R.id.placeList0AdsItemCategory);
                distance=initTextView(R.id.placeList0AdsItemDistance);
                desc=initTextView(R.id.placeList0AdsItemDesc);
                coupon=initImageView(R.id.placeList0AdsItemCoupon);
                deals=initImageView(R.id.placeList0AdsItemDeals);
                ads=initTextView(R.id.placeList0AdsItemAds);
                break;
            }
            case TYPE_NORMAL:
            {
                indicator = initTextView(R.id.placeList0NormalItemNumberIndicator);
                brandName = initTextView(R.id.placeList0NormalItemBrandName);
                address = initTextView(R.id.placeList0NormalItemAddress);
                lastLine = initTextView(R.id.placeList0NormalItemCategory);
                distance = initTextView(R.id.placeList0NormalItemDistance);
                desc = initTextView(R.id.placeList0NormalItemDesc);
                coupon = initImageView(R.id.placeList0NormalItemCoupon);
                deals = initImageView(R.id.placeList0NormalItemDeals);
                //ads = initTextView(R.id.placeList0NormalItemAds);

                selectedIndicator = initTextView(R.id.placeList0NormalSelectedItemNumberIndicator);
                selectedBrandName = initTextView(R.id.placeList0NormalSelectedItemBrandName);
                selectedAddress = initTextView(R.id.placeList0NormalSelectedItemAddress);
                selectedLastLine = initTextView(R.id.placeList0NormalSelectedItemCategory);
                selectedDistance = initTextView(R.id.placeList0NormalSelectedItemDistance);
                selectedDesc = initTextView(R.id.placeList0NormalSelectedItemDesc);
                selectedCoupon = initImageView(R.id.placeList0NormalSelectedItemCoupon);
                selectedDeals = initImageView(R.id.placeList0NormalSelectedItemDeals);
                selectedAds = initTextView(R.id.placeList0NormalSelectedItemAds);
                break;
            }
            case TYPE_CATEGORY:
            {
                indicator = initTextView(R.id.placeList0CategoryItemNumberIndicator);
                brandName = initTextView(R.id.placeList0CategoryItemBrandName);
                break;
            }
            case TYPE_GROUP:
            {
                brandName = initTextView(R.id.placeList0GroupItemBrandName);
                break;
            }
            default:
            {

            }
        }

        if (indicator != null && !isIndicatorSizeInitialed)
        {
            indicator.getLayoutParams().width = AndroidCitizenUiHelper.getPixelsByDensity(indicator.getLayoutParams().width);
            indicator.getLayoutParams().height = AndroidCitizenUiHelper.getPixelsByDensity(indicator.getLayoutParams().height);
            indicator.setPadding(AndroidCitizenUiHelper.getPixelsByDensity(indicator.getPaddingLeft()),
                AndroidCitizenUiHelper.getPixelsByDensity(indicator.getPaddingTop()),
                AndroidCitizenUiHelper.getPixelsByDensity(indicator.getPaddingRight()),
                AndroidCitizenUiHelper.getPixelsByDensity(indicator.getPaddingBottom()));
            isIndicatorSizeInitialed = true;
        }
    }
    
    protected TextView initTextView(int id)
    {
        View view = findViewById(id);
        if (view instanceof TextView)
        {
            return (TextView) view;
        }
        else
        {
            return null;
        }
    }
    
    protected ImageView initImageView(int id)
    {
        View view = findViewById(id);
        if (view instanceof ImageView)
        {
            return (ImageView) view;
        }
        else
        {
            return null;
        }
    }

    protected void onCreateContextMenu(ContextMenu menu)
    {
        super.onCreateContextMenu(menu);
        AndroidUiEventHandler.createMenu(tnMenu, menu);
    }

    public void setDescription(String shortMessage)
    {
        if (desc != null)
        {
            if (shortMessage != null)
            {
                desc.setText(shortMessage);
                desc.setVisibility(View.VISIBLE);
            }
            else
            {
                desc.setVisibility(View.GONE);
            }
        }

        if (selectedDesc != null)
        {
            if (shortMessage != null)
            {
                selectedDesc.setText(shortMessage);
                selectedDesc.setVisibility(View.VISIBLE);
            }
            else
            {
                selectedDesc.setVisibility(View.GONE);
            }
        }
    }

    public void setIsShowCouponImage(boolean isShowing)
    {
        if (coupon != null)
        {
            coupon.setVisibility(isShowing ? View.VISIBLE : View.GONE);
        }

        if (selectedCoupon != null)
        {
            selectedCoupon.setVisibility(isShowing ? View.VISIBLE : View.GONE);
        }
    }

    public void setIsShowMenuImage(boolean isShowing)
    {
        if (deals != null)
        {
            deals.setVisibility(isShowing? View.VISIBLE : View.GONE);
        }
        
        if (selectedDeals != null)
        {
            selectedDeals.setVisibility(isShowing? View.VISIBLE : View.GONE);
        }
    }
    
    public void setIsShowAds(boolean isShowing)
    {
        if (ads != null)
        {
            ads.setVisibility(isShowing? View.VISIBLE : View.GONE);
        }
        
        if (selectedAds != null)
        {
            selectedAds.setVisibility(isShowing? View.VISIBLE : View.GONE);
        }
    }

    public void setAddress(String addrText)
    {
        if (address != null)
        {
            address.setText(addrText);
        }

        if (selectedAddress != null)
        {
            selectedAddress.setText(addrText);
        }
    }

    public void setDistance(String distStr)
    {
        if (distance != null)
        {
            distance.setText(distStr);
            distance.setVisibility(View.VISIBLE);
        }

        if (selectedDistance != null)
        {
            selectedDistance.setText(distStr);
            selectedDistance.setVisibility(View.VISIBLE);
        }
    }
    
    public void setDistanceVisibility(int visibility)
    {
        if(distance!=null)
        {
            distance.setVisibility(visibility);
        }
    }
    
    public void setAddressVisibility(int visibility)
    {
        if(address!=null)
        {
            address.setVisibility(visibility);
        }
    }
    
    public void setLastLineVisibility(int visibility)
    {
        if(lastLine!=null)
        {
            lastLine.setVisibility(visibility);
        }
    }
    
    public void setBrandName(String title)
    {
        if (brandName != null)
        {
            brandName.setText(title);
        }

        if (selectedBrandName != null)
        {
            selectedBrandName.setText(title);
        }
    }

    public void setIndicator(int indicator)
    {
        if (this.indicator != null && this.indicator.getId() != R.id.placeList0AdsItemNumberIndicator)
        {
            this.indicator.setText(indicator + "");
            this.indicator.setVisibility(View.VISIBLE);
            this.findViewById(R.id.placeList0NormalItemIndicatorRightMarginView).setVisibility(View.VISIBLE);
        }

        if (selectedIndicator != null)
        {
            selectedIndicator.setText(indicator + "");
            this.selectedIndicator.setVisibility(View.VISIBLE);
            this.findViewById(R.id.placeList0NormalSelectedItemIndicatorRightMarginView).setVisibility(View.VISIBLE);
        }
    }

    public void setLastLine(String lastLine)
    {
        if (this.lastLine != null)
        {
            this.lastLine.setText(lastLine);
            this.lastLine.setVisibility(View.VISIBLE);
        }

        if (selectedLastLine != null)
        {
            selectedLastLine.setText(lastLine);
            selectedLastLine.setVisibility(View.VISIBLE);
        }
    }

    public String getLastLine()
    {
        String lastLineText = "";
        if (this.lastLine != null)
        {
            lastLineText = lastLine.getText().toString();
        }
        return lastLineText;
    }
    
    public void setIndicatorIcon(int id)
    {
        Drawable icon = this.getContext().getResources().getDrawable(id);
        if (indicator != null)
        {
            this.indicator.setVisibility(View.VISIBLE);
            indicator.getLayoutParams().width = icon.getIntrinsicWidth();
            indicator.getLayoutParams().height = icon.getIntrinsicHeight();
            this.indicator.setBackgroundResource(id);
            View rightMargin = this.findViewById(R.id.placeList0NormalItemIndicatorRightMarginView);
            if (rightMargin != null)
            {
                rightMargin.setVisibility(View.VISIBLE);
            }
        }
        
        if (selectedIndicator != null)
        {
            this.selectedIndicator.setVisibility(View.VISIBLE);
            selectedIndicator.getLayoutParams().width = icon.getIntrinsicWidth();
            selectedIndicator.getLayoutParams().height = icon.getIntrinsicHeight();
            this.selectedIndicator.setBackgroundResource(id);
            View rightMargin2 = this.findViewById(R.id.placeList0NormalSelectedItemIndicatorRightMarginView);
            if (rightMargin2 != null)
            {
                rightMargin2.setVisibility(View.VISIBLE);
            }
        }
    }
    
    public void setBadge(String badge)
    {
        if (this.badge != null)
        {
            this.badge.setText(badge);
        }
    }
}

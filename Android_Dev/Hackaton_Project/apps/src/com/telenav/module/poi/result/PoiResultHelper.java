package com.telenav.module.poi.result;

import com.telenav.data.serverproxy.impl.IPoiSearchProxy;
import com.telenav.res.IStringPoi;
import com.telenav.res.ResourceManager;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.ui.frogui.widget.FrogLabel;

public class PoiResultHelper  implements INotifierListener
{
    protected static final int MAX_UPDATE_STEPS = 3;
    protected static final int NOTIFY_INTERVAL = 400;
    protected static final String DOT_CHAR = ".";
    protected long lastNotifyTimeStamp = -1L;
    protected int updateStep = 0;
    protected FrogLabel label;
    protected String lodingMore = "";

    private static int[] bestMatchCategoryId = new int[]
    { 2041, 221, 446, 595, 641, 904090, 181, 165, 163, 900163, 231, 240, 253, 261, 229, 232, 236, 241, 243, 269, 263, 254, 256,
            257, 264, 267, 588 };

    private static int[] bypriceCategoryId = new int[]
    { IPoiResultConstants.SEARCH_BY_PRICE_ANY, IPoiResultConstants.SEARCH_BY_PRICE_DIESEL,
            IPoiResultConstants.SEARCH_BY_PRICE_PLUS, IPoiResultConstants.SEARCH_BY_PRICE_PREMIUM,
            IPoiResultConstants.SEARCH_BY_PRICE_REGULAR };
    
    public PoiResultHelper()
    {
        lodingMore = ResourceManager.getInstance().getCurrentBundle().getString(IStringPoi.LABEL_LOADING, IStringPoi.FAMILY_POI);
    }
    
    public long getNotifyInterval() 
    {
        return NOTIFY_INTERVAL;
    }

    public long getLastNotifyTimestamp() 
    {
        return lastNotifyTimeStamp;
    }

    public void setLastNotifyTimestamp(long timestamp) 
    {
        lastNotifyTimeStamp = timestamp;
    }

    public void notify(long timestamp) 
    {
        updateStep++;
        updateStep %= MAX_UPDATE_STEPS;
        StringBuffer sb = new StringBuffer(lodingMore);
        sb.append(DOT_CHAR);
        for (int i = 0; i < updateStep; i ++)
        {
            sb.append(DOT_CHAR);
        }     
        label.setText(sb.toString());
    }
    
    public void addToNotifier()
    {
        Notifier.getInstance().addListener(this);
    }
    
    public void removeNotifierListener()
    {
        Notifier.getInstance().removeListener(this);
    }
    
    public void add(FrogLabel label)
    {
        this.label = label;
    }
    
    public static int getDefaultSortType(int categoryId)
    {
        for (int i = 0; i < bypriceCategoryId.length; i++)
        {
            if (bypriceCategoryId[i] == categoryId)
            {
                return IPoiSearchProxy.TYPE_SORT_BY_PRICE;
            }
        }
        
        for (int i = 0; i < bestMatchCategoryId.length; i++)
        {
            if (bestMatchCategoryId[i] == categoryId)
            {
                return IPoiSearchProxy.TYPE_SORT_BY_RELEVANCE;
            }
        }

        //From PM's confirmation, default sort type is distance for those category ids aren't listed.
        return IPoiSearchProxy.TYPE_SORT_BY_DISTANCE;
    }
    
}

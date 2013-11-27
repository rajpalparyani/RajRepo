/*
 * (c) Copyright 2010 by TeleNav, Inc.
 * All Rights Reserved.
 *
 */
package com.telenav.ui.citizen.map;

import android.view.View;
import android.view.View.OnClickListener;

import com.telenav.app.android.scout_us.R;
import com.telenav.logger.Logger;
import com.telenav.module.map.IMapConstants;
import com.telenav.module.poi.PoiDataRequester;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.ui.android.CommonBottomButton;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;

/**
 * 
 * @author jpwang
 */
public class MapPoiIndicator extends TnLinearContainer implements OnClickListener
{
    private boolean hasPreviousPage;
    
    private int currentPage;
    
    private int curentPageCount;
    
    private int totalPage;
    
    private int localPage;
    
    protected long lastRequestTimeStamp = 0;
    
    protected static final int REQUEST_VALID_TIME = 500;
    
    private CommonBottomButton preButton;
    
    private CommonBottomButton nextButton;
    
    public MapPoiIndicator(int id, boolean isVertical, int currentPage, int currentPoiCount, int totalCount)
    {
        super(id, isVertical);
        this.currentPage = currentPage;
        
        this.curentPageCount = currentPoiCount / PoiDataRequester.DEFAULT_PAGE_SIZE + (currentPoiCount % PoiDataRequester.DEFAULT_PAGE_SIZE == 0 ? 0: 1);
        this.totalPage = totalCount / PoiDataRequester.DEFAULT_PAGE_SIZE + (totalCount % PoiDataRequester.DEFAULT_PAGE_SIZE == 0 ? 0: 1);
        
        hasPreviousPage = currentPage > 0;
        
        View mapBottomContainer = AndroidCitizenUiHelper.addContentView(this, R.layout.map_bottom_container);
        preButton = (CommonBottomButton) mapBottomContainer.findViewById(R.id.preButton);
        nextButton = (CommonBottomButton) mapBottomContainer.findViewById(R.id.nextButton);
        preButton.setButton(R.string.map0Previous, R.drawable.map_pre_button_icon, true);
        nextButton.setButton(R.string.map0Next, R.drawable.map_next_button_icon, false);
        
        preButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        
        changeButtonEnable();
    }

    private void changeButtonEnable()
    {
        preButton.setEnabled(hasPreviousPage);
        nextButton.setEnabled(this.currentPage < totalPage - 1);
    }

    public void increaseCurrentPage()
    {
        this.currentPage++;
        hasPreviousPage = true;
        changeButtonEnable();
        localPage =currentPage;
    }
    
    public void onClick(View v)
    {
        if ((System.currentTimeMillis() - lastRequestTimeStamp) < REQUEST_VALID_TIME)
        {
            if (Logger.DEBUG)
            {
                Logger.log(Logger.INFO, this.getClass().getName(), " multiple onClick");
            }
            return ;
        }
        
        int command = IMapConstants.CMD_MAP_POI_PAGE_PREV;
        if (v.getId() == R.id.nextButton)
        {
            if((this.currentPage+1) >= curentPageCount && currentPage >= localPage )
            {
                command = IMapConstants.CMD_MAP_POI_PAGE_NEXT_NETWORK;
            }
            else
            {
                hasPreviousPage = true;
                this.currentPage ++;
                command = IMapConstants.CMD_MAP_POI_PAGE_NEXT;
            }
        }
        else
        {
            this.currentPage --;
            hasPreviousPage = currentPage > 0;
        }
        
        changeButtonEnable();
        TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this);
        tnUiEvent.setCommandEvent(new TnCommandEvent(command));
        this.commandListener.handleUiEvent(tnUiEvent);  
        
        lastRequestTimeStamp = System.currentTimeMillis();
    }
}

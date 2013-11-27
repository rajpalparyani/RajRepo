package com.telenav.module.upsell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.telenav.app.android.scout_us.R;
import com.telenav.res.IStringTouring;
import com.telenav.res.ResourceManager;
import com.telenav.tnui.core.ITnUiEventListener;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiStyleManager;
import com.telenav.ui.android.AssetsImageDrawable;


public class LearningListAdapter extends BaseAdapter implements View.OnTouchListener
{
    int[] titleIds;
    int[] contentIds;
    TnUiArgs.TnUiArgAdapter[] unfocusedAdapters;
    TnUiArgs.TnUiArgAdapter[] focusedAdapters;
    boolean hasCarConnectFeature;
	private LayoutInflater mInflater;
	ITnUiEventListener commandListener;
	
	public LearningListAdapter(Context context, int[] titleIds, int[] contentIds, TnUiArgs.TnUiArgAdapter[] unfocusedAdapters, 
	        TnUiArgs.TnUiArgAdapter[] focusedAdapters, ITnUiEventListener commandListener, boolean hasCarConnectFeature)
	{
		this.mInflater = LayoutInflater.from(context);
		this.titleIds = titleIds;
		this.contentIds = contentIds;
		this.unfocusedAdapters = unfocusedAdapters;
		this.focusedAdapters = focusedAdapters;
		this.commandListener = commandListener;
		this.hasCarConnectFeature = hasCarConnectFeature;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
	    if(hasCarConnectFeature)
	    {
	        return titleIds.length;
	    }
	    else
	    {
	        return titleIds.length-1;
	    }
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    LearnItem item  = null; 
		String title = ResourceManager.getInstance().getCurrentBundle().getString(titleIds[position], IStringTouring.FAMILY_TOURING);
		String content = ResourceManager.getInstance().getCurrentBundle().getString(contentIds[position], IStringTouring.FAMILY_TOURING);
		TnUiArgAdapter unfocusedAdapter = unfocusedAdapters[position];
		
	    if(convertView==null){  
	    		item = new LearnItem();  

	            convertView = mInflater.inflate(R.layout.upsell_learning_list_item, null);  
	            item.titleView = (TextView) convertView.findViewById(R.id.item_title);  
	            item.contentView = (TextView) convertView.findViewById(R.id.item_content); 
	            item.iconView = (ImageView) convertView.findViewById(R.id.item_icon); 
	            item.arrowImageView = (ImageView) convertView.findViewById(R.id.right_arrow); 
	            convertView.setTag(item);  
	        }  
	        else {  
	        	item = (LearnItem) convertView.getTag();  
	        }  
	    	
//		    item.imageView.setImageBitmap(bitmap);
	        item.index = position;
		    item.titleView.setText(title); 
		    item.contentView.setText(content);
		    item.iconView.setImageDrawable(new AssetsImageDrawable(unfocusedAdapter.getImage()));
		    item.titleView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
            item.contentView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.UPSELL_LEARNING_ITEM_UNFOCUSED_COLOR));
		    item.arrowImageView.setImageDrawable(new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_UNFOCUSED));
		    convertView.setOnTouchListener(this);
		    convertView.setId(position);
	        return convertView;  
	}
	
	final static class LearnItem 
	{
	     public int index;
		 public ImageView iconView;
	     public TextView titleView;
	     public TextView contentView;
	     public ImageView arrowImageView;
	}

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        LearnItem item = (LearnItem) v.getTag(); 
        int index = item.index;
        int action = event.getAction();
        boolean handled = false;
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                item.arrowImageView.setImageDrawable(new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_FOCUSED));
                item.titleView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.UPSELL_LEARNING_ITEM_FOCUSED_COLOR));
                item.contentView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.UPSELL_LEARNING_ITEM_FOCUSED_COLOR));
                item.iconView.setImageDrawable(new AssetsImageDrawable(focusedAdapters[index].getImage()));
                v.invalidate();
                handled = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                item.arrowImageView.setImageDrawable(new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_UNFOCUSED));
                item.titleView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
                item.contentView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.UPSELL_LEARNING_ITEM_UNFOCUSED_COLOR));
                item.iconView.setImageDrawable(new AssetsImageDrawable(unfocusedAdapters[index].getImage()));
                v.invalidate();
                handled = true;
                break;
            case MotionEvent.ACTION_UP:
                item.arrowImageView.setImageDrawable(new AssetsImageDrawable(ImageDecorator.UPSELL_ARROW_UNFOCUSED));
                item.titleView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.TEXT_COLOR_WH));
                item.contentView.setTextColor(UiStyleManager.getInstance().getColor(UiStyleManager.UPSELL_LEARNING_ITEM_UNFOCUSED_COLOR));
                item.iconView.setImageDrawable(new AssetsImageDrawable(unfocusedAdapters[index].getImage()));
                v.invalidate();
                
                TnUiEvent tnUiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, null);
                tnUiEvent.setCommandEvent(new TnCommandEvent(IUpSellConstants.CMD_SELECT_LEARN_LIST + v.getId()));
                this.commandListener.handleUiEvent(tnUiEvent);
                
                handled = true;
                break;
                
        }
        return handled;
    }
}

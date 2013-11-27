/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DwfSliderPopup.java
 *
 */
package com.telenav.module.dwf;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.DwfUpdateListener;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.module.ModuleFactory;
import com.telenav.module.UserProfileProvider;
import com.telenav.mvc.AbstractCommonController;
import com.telenav.sdk.kontagent.IKontagentConstants;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.android.AndroidUiHelper;

/**
 * @author fangquanm
 * @date Jul 16, 2013
 */
@SuppressWarnings("deprecation")
public class DwfSliderPopup extends DwfUpdateListener.Stub implements OnClickListener
{
    private TnUiArgAdapter expandHeightAdapter, locationYAdapter;
    private DwfSliderPopupListAdapter popupListAdapter;

    private View parent;
    
    private View popupContentView;

    private PopupWindow popupWindow;

    private static DwfSliderPopup instance = null;

    private DwfSliderPopup()
    {

    }
    
    public void initPopupWindow()
    {
        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
        
        this.popupContentView = View.inflate(context, R.layout.dwf_slider, null);

        this.popupContentView.measure(0, 0);

        this.popupWindow = new PopupWindow(this.popupContentView, this.popupContentView.getMeasuredWidth(),
                this.popupContentView.getMeasuredHeight(), false);

        Method[] methods = PopupWindow.class.getMethods();
        for(Method m: methods){
           if(m.getName().equals("setWindowLayoutType")) {
              try{
                 m.invoke(this.popupWindow, WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);
              }catch(Exception e){
                 e.printStackTrace();
              }
              break;
           }
        }
        
        this.popupWindow.setOutsideTouchable(true);
        
        this.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.popupWindow.setTouchInterceptor(new OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    View listComponent = (View) DwfSliderPopup.this.popupContentView.findViewById(R.id.dwfSliderListWithEDot);

                    if (listComponent.getVisibility() == View.VISIBLE)
                    {
                        reduce();
                    }

                    return true;
                }

                return false;
            }
        });
        View emptyListArea = this.popupContentView.findViewById(R.id.dwfSliderEmptyArea);
        emptyListArea.setOnClickListener(this);
        View buttonView = this.popupContentView.findViewById(R.id.dwfSliderButton);
        buttonView.setOnClickListener(this);

        ListView listView = (ListView) this.popupContentView.findViewById(R.id.dwfSliderList);
        this.popupListAdapter = new DwfSliderPopupListAdapter();
        listView.setAdapter(this.popupListAdapter);
    }

    public static DwfSliderPopup getInstance()
    {
        if(instance == null)
        {
            instance = new DwfSliderPopup();
        }
        return instance;
    }

    public boolean isShowing()
    {
        return popupWindow != null && popupWindow.isShowing();
    }
    
    public void show(final View parent, TnUiArgAdapter expandHeightAdapter, final TnUiArgAdapter locationYAdapter)
    {
        this.parent = parent;
        this.expandHeightAdapter = expandHeightAdapter;
        this.locationYAdapter = locationYAdapter;
        if (popupWindow == null)
        {
            ((AndroidUiHelper) AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    initPopupWindow();
                }
            });
        }
        if (popupWindow != null && !popupWindow.isShowing())
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            if (dwfAidl != null)
            {
                try
                {
                    if (dwfAidl.getSharingIntent() == null)
                        return;

                    dwfAidl.addUpdateListener(this, this.toString());
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();

                    return;
                }
            }

            ((AndroidUiHelper) AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    int statusBarHeight = ((AndroidUiHelper) AndroidUiHelper.getInstance()).getStatusBarHeight(0);
                    popupWindow.showAtLocation(parent, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, statusBarHeight);
                }
            });
            
            this.expand();
        }
    }

    public void hide()
    {
        if(popupWindow == null)
        {
            return;
        }
        if (popupWindow.isShowing())
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            if (dwfAidl != null)
            {
                try
                {
                    dwfAidl.removeUpdateListener(this.toString());
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }

            ((AndroidUiHelper) AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
            {

                @Override
                public void run()
                {
                    popupWindow.dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.dwfSlider0ItemMainContainer || v.getId() == R.id.dwfSliderEmptyArea)
        {
            DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
            if (dwfAidl != null)
            {
                reduce();
                
                IUserProfileProvider userProfileProvider = new UserProfileProvider();
                try
                {
                    if(!(AbstractCommonController.getCurrentController() instanceof DriveWithFriendsController))
                    {
                        String sessionId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_SESSION_ID);
                        String userKey = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
                        String userId = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_ID);
                        String addressDt = dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);

                        ModuleFactory.getInstance().startDriveWithFriendsController(
                            (AbstractCommonController) AbstractCommonController.getCurrentController(), userProfileProvider,
                            sessionId, userKey, userId, addressDt, false);
                        KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_SIDEPANEL_FRIEND_CLICKED);
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if(v.getId() == R.id.dwfSliderButton)
        {
            View listComponent = (View) this.popupContentView.findViewById(R.id.dwfSliderListWithEDot);

            if (listComponent.getVisibility() == View.VISIBLE)
            {
                reduce();
            }
            else
            {
                expand();
            }
        }
    }
    
    public void update()
    {
        if(popupWindow == null)
        {
            return;
        }
        popupContentView.measure(0, 0);

        int height = expandHeightAdapter != null ? this.expandHeightAdapter.getInt() : ((AndroidUiHelper) AndroidUiHelper
                .getInstance()).getDisplayHeight();

//        this.popupWindow.update(this.popupContentView.getMeasuredWidth(), height);
        this.popupWindow.update(0, locationYAdapter.getInt(), this.popupContentView.getMeasuredWidth(), height);
    }

    private void expand()
    {
        ImageView buttonView = (ImageView) this.popupContentView.findViewById(R.id.dwfSliderButton);
        buttonView.setImageResource(R.drawable.drive_with_friends_nav_icon_fold_unfocused);
        buttonView.setVisibility(View.GONE);
        View listComponent = (View) this.popupContentView.findViewById(R.id.dwfSliderListWithEDot);

        listComponent.setVisibility(View.VISIBLE);
        buttonView.setVisibility(View.VISIBLE);
        
        this.popupContentView.measure(0, 0);

        int height = expandHeightAdapter != null ? this.expandHeightAdapter.getInt() : ((AndroidUiHelper) AndroidUiHelper
                .getInstance()).getDisplayHeight();

        int statusBarHeight = ((AndroidUiHelper) AndroidUiHelper.getInstance()).getStatusBarHeight(0);
        
        this.popupWindow.dismiss();
        this.popupWindow.showAtLocation(parent, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, statusBarHeight);
        this.popupWindow.update(0, locationYAdapter.getInt(), this.popupContentView.getMeasuredWidth(), height);
    }

    private void reduce()
    {
        ImageView buttonView = (ImageView) this.popupContentView.findViewById(R.id.dwfSliderButton);
        buttonView.setImageResource(R.drawable.drive_with_friends_nav_icon_open_unfocused);
        buttonView.setVisibility(View.GONE);
        View listComponent = (View) this.popupContentView.findViewById(R.id.dwfSliderListWithEDot);
        listComponent.setVisibility(View.GONE);
        buttonView.setVisibility(View.VISIBLE);
        this.popupContentView.measure(0, 0);

        int statusBarHeight = ((AndroidUiHelper) AndroidUiHelper.getInstance()).getStatusBarHeight(0);
        
        this.popupWindow.dismiss();
        this.popupWindow.showAtLocation(parent, Gravity.RIGHT | Gravity.CENTER_VERTICAL, 0, statusBarHeight);
        this.popupWindow.update(this.popupContentView.getMeasuredWidth(), this.popupContentView.getMeasuredHeight());
        KontagentLogger.getInstance().addCustomEvent(IKontagentConstants.CATEGORY_DWF, IKontagentConstants.DWF_SIDEPANEL_CLOSED);
    }

    @Override
    public void updateDwf(final List<Friend> friends) throws RemoteException
    {
        DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();
        if (dwfAidl == null)
        {
            return;
        }
            
        final String userKey = dwfAidl.getSharingIntent() == null?
                "" : dwfAidl.getSharingIntent().getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
        ((AndroidUiHelper) AndroidUiHelper.getInstance()).runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                if (popupListAdapter != null)
                {
                    popupListAdapter.setFriends(friends, userKey);
                }
            }
        });
    }

    public ArrayList<Friend> getFriends()
    {
        if (popupListAdapter != null)
        {
            return popupListAdapter.getFriends();
        }
        return null;
    }
    
    public class DwfSliderPopupListAdapter extends BaseAdapter implements ListAdapter
    {
        private ArrayList<Friend> friends;
        
        private String userKey;

        public DwfSliderPopupListAdapter()
        {
            friends = new ArrayList<Friend>();
        }

        public void setFriends(List<Friend> fs, String userKey)
        {
            this.friends.clear();
            this.friends.addAll(fs);
            this.userKey = userKey;

            if (isShowing())
            {
                this.notifyDataSetChanged();
            }
        }
        
        public ArrayList<Friend> getFriends()
        {
            return friends;
        }
        
        private static final int TYPE_EMPTY = 0;
        
        private static final int TYPE_DEST = 1;
        
        private static final int TYPE_ITEM = 2;
        
        private static final int TYPE_ITEM_ARRIVED = 3;
        
        private final int[] types = new int[]{TYPE_EMPTY, TYPE_DEST, TYPE_ITEM, TYPE_ITEM_ARRIVED};

        @Override
        public int getItemViewType(int position)
        {
            int type = TYPE_ITEM;
            if (friends == null || friends.isEmpty())
            {
                type = TYPE_EMPTY;
            }
            else if(position == 0)
            {
                type = TYPE_DEST;
            }
            else if (friends.get(position - 1).getStatus() == FriendStatus.ARRIVED)
            {
                type = TYPE_ITEM_ARRIVED;
            }
            return type;
        }

        @Override
        public int getViewTypeCount()
        {
            return types.length;
        }

        @Override
        public int getCount()
        {
            return friends == null || friends.isEmpty() ? 1 : friends.size() + 1;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (friends == null || friends.isEmpty())
            {
                Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                convertView = LayoutInflater.from(context).inflate(R.layout.dwf_slider0item_empty, null);
            }
            else if (position == 0)
            {
                Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                convertView = LayoutInflater.from(context).inflate(R.layout.dwf_slider0dest, null);
            }
            else
            {
                Friend f = this.friends.get(position - 1);

                if (convertView == null || convertView.findViewById(R.id.dwfListListItemIndicatorView) == null)
                {
                    Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                    if (f.getStatus() == FriendStatus.ARRIVED)
                    {
                        convertView = LayoutInflater.from(context).inflate( R.layout.dwf_slider0item_arrived, null);
                    }
                    else
                    {
                        convertView = LayoutInflater.from(context).inflate(R.layout.dwf_slider0item, null);
                    }
                }

                convertView.setSelected(false);
                convertView.setClickable(true);
                convertView.setOnClickListener(DwfSliderPopup.this);
                
                View indicatorView = convertView.findViewById(R.id.dwfSlider0ItemStatusIndicator);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.dwfSlider0ItemImageView);
                TextView nameView = (TextView) convertView.findViewById(R.id.dwfSlider0ItemNameView);
                TextView etaView = (TextView) convertView.findViewById(R.id.dwfSlider0ItemEtaView);

                if (f.getImage() != null && f.getImage().length > 0)
                {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(f.getImage(), 0, f.getImage().length));
                }

                switch (f.getStatus())
                {
                    case ARRIVED:
                    {
                        indicatorView.setEnabled(true);
                        etaView.setText(R.string.dwfFriendArrived);
                        break;
                    }
                    case DRIVING:
                    {
                        indicatorView.setEnabled(true);
                        break;
                    }
                    case END:
                    {
                        indicatorView.setEnabled(false);
                        break;
                    }
                    case INIT:
                    {
                        indicatorView.setEnabled(false);
                        break;
                    }
                    case JOINED:
                    {
                        indicatorView.setEnabled(true);
                        break;
                    }
                    default:
                    {
                        indicatorView.setEnabled(false);
                        break;
                    }
                }

                if (f.getKey().equals(userKey))
                {
                    nameView.setText(AndroidPersistentContext.getInstance().getContext().getString(R.string.dwfMe));
                }
                else
                {
                    nameView.setText(f.getName());
                }

                if(f.getStatus() != FriendStatus.ARRIVED)
                {
                    if (f.getEta() >= 0)
                    {
                        etaView.setText(parseEtaDisplayTime(f.getEta()));
                    }
                    else
                    {
                        etaView.setText(" - - ");
                    }
                }
            }

            return convertView;
        }

        public String parseEtaDisplayTime(long second)
        {
            String etaString = "00:";
            int hour = (int) (second / 3600);
            if (hour > 0 && hour < 10)
            {
                etaString = "0" + Integer.toString(hour) + ":";
            }
            else if (hour >= 10)
            {
                etaString = Integer.toString(hour) + ":";
            }
            int minute = (int) ((second % 3600) / 60);
            if (minute > 0 && minute < 10)
            {
                etaString += "0" + Integer.toString(minute);
            }
            else if (minute >= 10)
            {
                etaString += Integer.toString(minute);
            }
            else
            {
                etaString += "00";
            }
            return etaString;
        }
    }
}

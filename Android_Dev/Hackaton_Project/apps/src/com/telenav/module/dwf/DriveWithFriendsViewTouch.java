/**
 *
 * Copyright 2013 TeleNav, Inc. All rights reserved.
 * DriveWithFriendsViewTouch.java
 *
 */
package com.telenav.module.dwf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.telenav.app.AbstractContactProvider.TnContact;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.scout_us.R;
import com.telenav.audio.IRecorderListener;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkMapProxy;
import com.telenav.dwf.aidl.Friend;
import com.telenav.dwf.aidl.Friend.FriendType;
import com.telenav.location.TnLocation;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.media.MediaManager;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.AbstractCommonMapView;
import com.telenav.mvc.AbstractCommonUiDecorator;
import com.telenav.mvc.ICommonConstants;
import com.telenav.navsdk.events.MapViewData.AnnotationType;
import com.telenav.res.IStringCommon;
import com.telenav.res.IStringDashboard;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.ITnScreenAttachedListener;
import com.telenav.tnui.core.TnKeyEvent;
import com.telenav.tnui.core.TnMenu;
import com.telenav.tnui.core.TnMotionEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiArgs;
import com.telenav.tnui.core.TnUiArgs.ITnUiArgsDecorator;
import com.telenav.tnui.core.TnUiArgs.TnUiArgAdapter;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphics;
import com.telenav.tnui.graphics.AbstractTnImage;
import com.telenav.tnui.widget.TnLinearContainer;
import com.telenav.tnui.widget.TnPopupContainer;
import com.telenav.ui.ImageDecorator;
import com.telenav.ui.UiFactory;
import com.telenav.ui.android.DwfReceiverGridContainer;
import com.telenav.ui.android.DwfReceiverGridContainer.IDwfReceiverGridListener;
import com.telenav.ui.android.TnAutoCompleteTextView;
import com.telenav.ui.android.TnAutoCompleteTextView.TnSuggestionBean;
import com.telenav.ui.citizen.CitizenScreen;
import com.telenav.ui.citizen.android.AndroidCitizenUiHelper;
import com.telenav.ui.citizen.map.AbstractAnnotation;
import com.telenav.ui.citizen.map.DayNightService;
import com.telenav.ui.citizen.map.FriendAnnotation;
import com.telenav.ui.citizen.map.IMapContainerConstants;
import com.telenav.ui.citizen.map.ImageAnnotation;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.ui.frogui.widget.FrogButton;
import com.telenav.ui.frogui.widget.FrogMessageBox;
import com.telenav.ui.frogui.widget.FrogNullField;
import com.telenav.util.PrimitiveTypeCache;

/**
 * @author fangquanm
 * @date Jul 1, 2013
 */
class DriveWithFriendsViewTouch extends AbstractCommonMapView implements IDriveWithFriendsConstants, IDwfReceiverGridListener, TextWatcher
{
    private TextView addressTextView;

    private EditText smsTextView;

    private DwfReceiverGridContainer receiverGridContainer;

    private View dwfInviteButton;

    private View driveInfoNativeView;

    private ListView dwfListListView;

    private FrogNullField moreContextField;

    private AbstractAnnotation destAnnotation;

    private Vector<FriendAnnotation> currentFriendAnnotations = new Vector<FriendAnnotation>();

    private boolean hasOperationOnMap = false;
    
    private MediaRecorder recorder = null;
    
    public DriveWithFriendsViewTouch(AbstractCommonUiDecorator uiDecorator)
    {
        super(uiDecorator);
    }

    @Override
    public boolean handleUiEvent(TnUiEvent tnUiEvent)
    {
        logKtUiEvent(tnUiEvent);
        switch (tnUiEvent.getType())
        {
            case TnUiEvent.TYPE_COMMAND_EVENT:
            {
                if (tnUiEvent.getCommandEvent() != null)
                {
                    int command = tnUiEvent.getCommandEvent().getCommand();
                    if (command == CMD_INVITE)
                    {
                        TeleNavDelegate.getInstance().closeVirtualKeyBoard(null);
                    }
                }
                break;
            }
        }
        return super.handleUiEvent(tnUiEvent);
    }
    
    protected void deactivateDelegate()
    {
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(true);
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);
        super.deactivateDelegate();
    }
    

    public void onScreenUiEngineAttached(TnScreen screen, int attached)
    {
        if (screen != null && screen.getId() == STATE_SESSION_LIST)
        {
            if(attached == ITnScreenAttachedListener.BEFORE_ATTACHED)
            {
                hasOperationOnMap = false;
            }
            else if (attached == ITnScreenAttachedListener.DETTACHED)
            {
                hasOperationOnMap = false;
            }
            
        }
    }
    
    @Override
    protected boolean updateScreen(int state, TnScreen screen)
    {
        switch (state)
        {
            case STATE_NEW_DWF:
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
                updateNewDwf();
                return true;
            }
            case STATE_SESSION_LIST:
            {
                TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);

                MapContainer mapContainer = MapContainer.getInstance();
                MapContainer.getInstance().resume();
                
                updateMapFeature(mapContainer);

                mapContainer.setMapRect(((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_X.getInt(),
                    ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_Y.getInt(),
                    ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_WIDTH.getInt(),
                    ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_HEIGHT.getInt());
                break;
            }
        }
        return false;
    }
    
    private void logKtUiEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent != null && tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT && (tnUiEvent.getCommandEvent() != null))
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_SHOW_MENU:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
                        KontagentLogger.DWF_MENU_CLICKED);
                    break;
                }
                case CMD_LEAVE_GROUP:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
                        KontagentLogger.DWF_LEAVE_GROUP_CLICKED);
                    break;
                }
                case CMD_INVITE_NEW:
                {
                    KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
                        KontagentLogger.DWF_ADD_NEWMEMBER_CLICKED);
                    break;
                }
                case CMD_INVITE:
                {
                    ArrayList<TnContact> contacts = (ArrayList<TnContact>) model.get(KEY_V_CONTACTS);
                    if (contacts != null && contacts.size() > 0)
                    {
                        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_DWF,
                            KontagentLogger.DWF_INVITE_CLICKED);
                    }
                    break;
                }
            }
        }
    }
        
    @Override
    public void onSizeChanged(AbstractTnComponent tnComponent, int w, int h, int oldw, int oldh)
    {
        if (this.model.getState() == STATE_SESSION_LIST)
        {
            updateDropButton(model.getBool(KEY_B_IS_FULL_MAP));
            MapContainer.getInstance().setMapRect(((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_X.getInt(),
                ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_Y.getInt(),
                ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_WIDTH.getInt(),
                ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_HEIGHT.getInt());
        }
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
    }

    @Override
    protected boolean preProcessUIEvent(TnUiEvent tnUiEvent)
    {
        if (tnUiEvent.getType() == TnUiEvent.TYPE_COMMAND_EVENT)
        {
            int command = tnUiEvent.getCommandEvent().getCommand();
            switch (command)
            {
                case CMD_COMMON_GOTO_ONEBOX:
                {
                    this.model.put(KEY_B_IS_CHOOSING_LOCATION, true);
                    this.model.put(KEY_B_NEED_CURRENT_LOCATION, true);
                    if (this.model.getBool(KEY_B_INVITE_NEW))
                    {
                        return true;
                    }
                    break;
                }
                case CMD_INVITE:
                case CMD_LEAVE_GROUP:
                {
                    if (!NetworkStatusManager.getInstance().isConnected())
                    {
                        Context context = (Context) AndroidPersistentContext.getInstance().getContext();
                        Toast.makeText(context, R.string.dwfNetworkErrorMessage, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    if (command == CMD_INVITE)
                    {
                        this.model.put(KEY_S_SMS, this.smsTextView.getText().toString());
                    }
                    break;
                }
                case CMD_SHOW_MENU:
                {
                    AndroidUiEventHandler.onLongClick(moreContextField);
                    break;
                }
                case CMD_DROP_MAP:
                {
                    boolean isFullMap = !this.model.getBool(KEY_B_IS_FULL_MAP);
                    this.model.put(KEY_B_IS_FULL_MAP, isFullMap);

                    TnScreen screen = this.getScreenByState(STATE_SESSION_LIST);

                    if (screen != null)
                    {
                        if (MapContainer.getInstance().getFeature(ID_LIST_INFO_CONTAINER) != null)
                        {
                            AbstractTnComponent listContainer = MapContainer.getInstance().getFeature(ID_LIST_INFO_CONTAINER);
                            listContainer.setVisible(!isFullMap);
                            updateDropButton(isFullMap);
                        }
                        hasOperationOnMap = false;
                        screen.getRootContainer().requestLayout();
                    }
                    break;
                }
            }
        }
        return super.preProcessUIEvent(tnUiEvent);
    }

    @Override
    protected int transformCommandDelegate(int state, TnUiEvent tnUiEvent)
    {
        int cmd = CMD_NONE;

        if (tnUiEvent.getKeyEvent() != null && tnUiEvent.getKeyEvent().getAction() == TnKeyEvent.ACTION_DOWN
                && tnUiEvent.getKeyEvent().getCode() == TnKeyEvent.KEYCODE_BACK)
        {
            if (state == STATE_SESSION_LIST)
            {
                if (NavRunningStatusProvider.getInstance().isNavRunning())
                {
                    if (NavSdkNavEngine.getInstance().isRunning())
                    {
                        cmd = CMD_BACK_TO_MOVING_MAP;
                    }
                    else
                    {
                        cmd = CMD_START_MAIN;
                    }
                }
            }
        }

        return cmd;
    }

    @Override
    protected TnPopupContainer createPopup(int state)
    {
        TnPopupContainer popup = null;
        switch (state)
        {
//            case STATE_REQUEST_SESSION_LIST:
//            {
//                boolean isInviteNew = this.model.getBool(KEY_B_INVITE_NEW);
//                if (isInviteNew)
//                {
//                    Context context = AndroidPersistentContext.getInstance().getContext();
//                    FrogProgressBox progressBox = UiFactory.getInstance().createProgressBox(0,
//                        context.getString(R.string.dwfRequsting));
//                    popup = progressBox;
//                }
//                break;
//            }
            case STATE_CHECK_EXPIRATION:
            {
                popup = createMessageBox(state);
            }
                
        }
        return popup;
    }

    private TnPopupContainer createMessageBox(int state)
    {
        TnMenu menu = UiFactory.getInstance().createMenu();
        menu.add(ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_BTTN_OK,
             IStringCommon.FAMILY_COMMON), CMD_OK_EXPIRATION);
        String errorMessage = ResourceManager.getInstance().getCurrentBundle()
                .getString(IStringCommon.RES_DWF_SESSION_EXPIRED, IStringCommon.FAMILY_COMMON);
        FrogMessageBox messageBox = UiFactory.getInstance().createMessageBox(state, errorMessage, menu);
        return messageBox;
    }

    @Override
    protected TnScreen createScreen(int state)
    {
        switch (state)
        {
            case STATE_NEW_DWF:
            {
                return createDriveWithFriendsMainScreen(state);
            }
            case STATE_SESSION_LIST:
            {
                return createSessionListScreen(state);
            }

        }

        return null;
    }

    @Override
    protected boolean updatePopup(int state, TnPopupContainer popup)
    {
        return false;
    }

    @Override
    protected void popAllViews()
    {
        super.popAllViews();

        MapContainer.getInstance().removeAllAnnotations();
        
        destAnnotation = null;
    }

    @Override
    public void updateGridItems(ArrayList<TnSuggestionBean> items)
    {
        ArrayList<TnContact> contacts = new ArrayList<TnContact>();

        for (TnSuggestionBean item : items)
        {
            TnContact contact = new TnContact();
            contact.name = item.label;
            contact.phoneNumber = item.desc;
            contact.b = item.image;

            contacts.add(contact);
        }

        if (contacts.isEmpty())
        {
            this.model.remove(KEY_V_CONTACTS);
        }
        else
        {
            this.model.put(KEY_V_CONTACTS, contacts);
        }

        if (!contacts.isEmpty() && this.model.get(KEY_O_SELECTED_ADDRESS) != null 
                && smsTextView.getText().toString() != null
                && !smsTextView.getText().toString().trim().equals(""))
        {
            dwfInviteButton.setEnabled(true);
        }
        else
        {
            dwfInviteButton.setEnabled(false);
        }
    }
    
    private String convertAddress(Stop stop)
    {
        if(stop == null)
        {
            return "";
        }
        StringBuffer displayString = new StringBuffer(32);
        if (stop.getLabel() != null && stop.getLabel().length() > 0 && (stop.getFirstLine() == null || !stop.getFirstLine().startsWith(stop.getLabel()))
                && (stop.getCity() != null && !stop.getLabel().trim().equals(stop.getCity().trim())))
        {
            displayString.append(stop.getLabel());
        }

        if (stop.getFirstLine() != null && stop.getFirstLine().length() > 0)
        {
            if (displayString.length() > 0)
            {
                displayString.append(", ");
            }
            displayString.append(stop.getFirstLine());
        }

        String city = stop.getCity();
        if (city != null)
        {
            city = city.trim();
            if (city.length() > 0)
            {
                if (displayString.length() > 0)
                {
                    displayString.append(" @ ");
                }
                displayString.append(city);
            }
        }
        return displayString.toString();
    }

    private void updateNewDwf()
    {
        if (this.model.get(KEY_O_SELECTED_ADDRESS) != null)
        {
            Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
            boolean isCurrentLocation = isCurrentLocation(address);

            String label = convertAddress(address.getStop());
            addressTextView.setText(label);

            Context context = AndroidPersistentContext.getInstance().getContext();
            String msg = context.getString(R.string.dwfMeetAt, label);
            smsTextView.setVisibility(View.VISIBLE);
            smsTextView.setText(Html.fromHtml(msg));

            if (this.model.get(KEY_V_CONTACTS) != null
                    && smsTextView.getText().toString() != null
                    && !smsTextView.getText().toString().trim().equals(""))
            {
                dwfInviteButton.setEnabled(true);
            }
            else
            {
                dwfInviteButton.setEnabled(false);
            }
            
            TextView inviteButtonTextView = (TextView)dwfInviteButton.findViewById(R.id.dwfInviteButtonText);
            inviteButtonTextView.setText(isCurrentLocation ? R.string.dwfMeetButton : R.string.dwfLetGoButton);
        }

        if (this.model.getBool(KEY_B_INVITE_NEW))
        {
            addressTextView.setClickable(false);

            if (this.model.get(KEY_V_CONTACTS) == null)
            {
                receiverGridContainer.removeAllViews();
            }
        }

        TnContact contact = (TnContact) this.model.fetch(KEY_O_CONTACT);
        if (contact != null)
        {
            ContactUtil.trimPhone(contact);
            receiverGridContainer.addItem(contact.name, contact.phoneNumber,
                ContactUtil.loadContactPhoto(Long.parseLong(contact.id), AndroidPersistentContext.getInstance().getContext()));
        }
    }

    private TnScreen createDriveWithFriendsMainScreen(int state)
    {
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_PORTRAIT);
        CitizenScreen mainScreen = UiFactory.getInstance().createScreen(state);

        View mainNativeView = AndroidCitizenUiHelper.addContentView(mainScreen, R.layout.dwf);

        TextView loginTitleTextView = (TextView) mainNativeView.findViewById(R.id.commonTitle0TextView);
        loginTitleTextView.setText(R.string.dwfTitle);

        addressTextView = (TextView) mainNativeView.findViewById(R.id.dwfChoosePlace);
        final TnAutoCompleteTextView autoCompleteTextView = (TnAutoCompleteTextView) mainNativeView
                .findViewById(R.id.dwfAutoCompleteTextView);
        autoCompleteTextView.setHint(R.string.dwfInviteFriendsHint);
        autoCompleteTextView.setHintTextColor(R.color.dwfHintTextColor);
        receiverGridContainer = (DwfReceiverGridContainer) mainNativeView.findViewById(R.id.dwfReceiverGridContainer);
        receiverGridContainer.setListener(this);

        smsTextView = (EditText) mainNativeView.findViewById(R.id.dwfSms);

        autoCompleteTextView.setAdapter(new ContactSuggestionAdapter(autoCompleteTextView.getContext()));

        autoCompleteTextView.setDropDownAnchor(R.id.dwfReceiverBottomSeparator);
        
        final View dwfContact = (View) mainNativeView
                .findViewById(R.id.dwfContact);
        final View dwfContactContainer = (View) mainNativeView
                .findViewById(R.id.dwfContactContainer);
        autoCompleteTextView.setDropDownWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        autoCompleteTextView.setDropDownHorizontalOffset((int)(-AndroidCitizenUiHelper.getResourceDimension(R.dimen.dwfMargin)));
        autoCompleteTextView.setDropDownVerticalOffset(autoCompleteTextView.getPaddingBottom() + dwfContact.getPaddingBottom() + dwfContactContainer.getPaddingBottom());
        autoCompleteTextView.setDropDownBackgroundDrawable(null);
        AndroidCitizenUiHelper.setOnClickCommand(this, mainNativeView, new int[][]
                {
                { R.id.dwfChoosePlace, CMD_COMMON_GOTO_ONEBOX },
                { R.id.dwfContactButton, CMD_SELECT_FROM_CONTACTS },
                { R.id.dwfInviteButton, CMD_INVITE }, });
        
        Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
        if (address != null)
        {
            boolean isCurrentLocation = isCurrentLocation(address);

            String label = convertAddress(address.getStop());
            addressTextView.setText(label);

            if (this.model.getBool(KEY_B_INVITE_NEW))
            {
                addressTextView.setClickable(false);
            }

            Context context = AndroidPersistentContext.getInstance().getContext();
            String msg = context.getString(R.string.dwfMeetAt, label);
            smsTextView.setVisibility(View.VISIBLE);
            smsTextView.setText(Html.fromHtml(msg));
        }
        else
        {
            smsTextView.setVisibility(View.GONE);
        }
        smsTextView.addTextChangedListener(this);

        dwfInviteButton = (View) mainNativeView.findViewById(R.id.dwfInviteButton);
        dwfInviteButton.setEnabled(false);

        TextView inviteButtonTextView = (TextView)mainNativeView.findViewById(R.id.dwfInviteButtonText);
        boolean isCurrentLocation = isCurrentLocation(address);
        inviteButtonTextView.setText(isCurrentLocation ? R.string.dwfMeetButton : R.string.dwfLetGoButton);

        return mainScreen;
    }

    private TnScreen createSessionListScreen(int state)
    {
        TeleNavDelegate.getInstance().setOrientation(TeleNavDelegate.ORIENTATION_UNSPECIFIED);

        CitizenScreen sessionListScreen = UiFactory.getInstance().createScreen(state);

        MapContainer mapContainer = UiFactory.getInstance().getCleanMapContainer(sessionListScreen, ID_MAP_CONTAINER);
        
        addMapFeature(mapContainer);

        return sessionListScreen;
    }

    private void addMapFeature(MapContainer mapContainer)
    {
        if (MapContainer.getInstance().getFeature(ID_TITLE_CONTAINER) != null)
            return;

        if (mapContainer == null)
        {
            mapContainer = MapContainer.getInstance();
        }

        mapContainer.enableGPSCoarse(true);
        mapContainer.setMapRect(((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_X.getInt(),
            ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_Y.getInt(),
            ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_WIDTH.getInt(),
            ((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_HEIGHT.getInt());

        mapContainer.changeToSpriteVehicleAnnotation();

        mapContainer.setRenderingMode(NavSdkMapProxy.RENDERING_MODE_2D_NORTH_UP);

        mapContainer.clearRoute();
        
        this.addDestination();

        this.addFriends();

        this.lookAtAnnotations();

        this.addTitleContainer();

        this.addDriveInfoContainer();

        this.addListInfoContainer();

        MapContainer.getInstance().addFeature(
            createMapCompanyLogo(((DriveWithFriendsUiDecorator) uiDecorator).STATIC_MAP_X,
                ((DriveWithFriendsUiDecorator) uiDecorator).MAP_LOGO_WITH_BOTTOM_CONTAINER_Y));
        MapContainer.getInstance().addFeature(
            createMapProvider(((DriveWithFriendsUiDecorator) uiDecorator).MAP_LOGO_WITH_BOTTOM_CONTAINER_Y));

        this.addDropButton();
    }

    @SuppressWarnings("unchecked")
    private void lookAtAnnotations()
    {
        if (currentFriendAnnotations == null)
        {
            currentFriendAnnotations = new Vector<FriendAnnotation>();
        }

        @SuppressWarnings("rawtypes")
        Vector annotationNames = new Vector();
        if (destAnnotation != null)
        {
            annotationNames.addElement(destAnnotation.getAnnotationId() + "");
        }

        for (int i = 0; i < currentFriendAnnotations.size(); i++)
        {
            AbstractAnnotation friendAnnotation = currentFriendAnnotations.elementAt(i);
            annotationNames.addElement(friendAnnotation.getAnnotationId() + "");
        }

        annotationNames
                .addElement(MapContainer.getInstance().isDisplayingModelVehicle() ? MapContainer.MODEL_VEHICLE_ANNOTATION_NAME
                        : MapContainer.SPRITE_VEHICLE_ANNOTATION_NAME);

        MapContainer.getInstance().lookAtAnnotationsByName(annotationNames);
    }

    private void updateMapFeature(MapContainer mapContainer)
    {
        boolean friendsUpdated = model.fetchBool(KEY_B_FRIENDS_UPDATED);
        if (MapContainer.getInstance().getFeature(ID_TITLE_CONTAINER) != null)
        {
            if (mapContainer == null)
            {
                mapContainer = MapContainer.getInstance();
            }

            if (friendsUpdated)
            {
                updateFriendsPosition();
            }

            boolean isDestinationUpdated = false;
            Address currentAddress = (Address)model.get(KEY_O_SELECTED_ADDRESS);
            if (currentAddress != null && destAnnotation == null)
            {
                isDestinationUpdated = true;
            }
            else if(currentAddress == null && destAnnotation != null)
            {
                isDestinationUpdated = true;
            }
            else if(currentAddress != null && destAnnotation != null)
            {
                Stop stop = currentAddress.getStop();
                if(stop != null)
                {
                    int currentLat = stop.getLat();
                    int currentLon = stop.getLon();
                    
                    double lat = destAnnotation.getLat();
                    double lon = destAnnotation.getLon();
                    
                    double currentLatD = (double) currentLat / 100000.0;
                    double currentLonD = (double) currentLon / 100000.0;
                    
                    if(currentLatD != lat || currentLonD != lon)
                    {
                        isDestinationUpdated = true;
                    }
                }
            }

            if (isDestinationUpdated)
            {
                addDestination();
            }
            
            if ((friendsUpdated || isDestinationUpdated) && !hasOperationOnMap)
            {
                lookAtAnnotations();
            }
            
            updateDriveInfoContainer();
            updateListInfoContainer();
        }
        else
        {
            addMapFeature(mapContainer);
        }
        boolean isFullMap = this.model.getBool(KEY_B_IS_FULL_MAP);
        AbstractTnComponent listContainer = MapContainer.getInstance().getFeature(ID_LIST_INFO_CONTAINER);
        listContainer.setVisible(!isFullMap);
    }

    public void handleTouchEventOnMap(final MapContainer container, TnUiEvent uiEvent)
    {
        hasOperationOnMap = true;
    }
    
    private void addDestination()
    {
        if (destAnnotation != null)
        {
            MapContainer.getInstance().removeFeature(destAnnotation);
            destAnnotation = null;
        }
        
        float pivotX = 0.5f;
        float pivotY = 0;

        AbstractTnImage destFlag = ImageDecorator.DESTINATION_ICON_UNFOCUSED.getImage();

        Address dest = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);

        if (dest == null)
        {
            return;
        }

        Stop stop = dest.getStop();
        if (stop == null && dest.getPoi() != null && dest.getPoi().getStop() != null)
        {
            stop = dest.getPoi().getStop();
        }

        ImageAnnotation destFlagAnnotation = new ImageAnnotation(destFlag, stop.getLat() / 100000.0d,
                stop.getLon() / 100000.0d, pivotX, pivotY, ImageAnnotation.STYLE_FLAG_SCREEN_ANNOTATION);
        MapContainer.getInstance().addFeature(destFlagAnnotation);

        this.destAnnotation = destFlagAnnotation;
    }

    @SuppressWarnings("unchecked")
    private void addFriends()
    {
        currentFriendAnnotations.removeAllElements();

        ArrayList<Friend> friends = (ArrayList<Friend>) this.model.get(KEY_V_FRIENDS);
        addFriends(friends);
    }

    private void addFriends(ArrayList<Friend> friends)
    {
        if (friends == null)
            return;

        for (Friend f : friends)
        {
            if ((f.getKey() != null && f.getKey().equals(model.getString(KEY_S_DWF_USER_KEY))) || (f.getLat() == 0 && f.getLon() == 0))
            {
                continue;
            }

            FriendAnnotation friendAnnotation = new FriendAnnotation(f, AnnotationType.AnnotationType_Screen, FriendAnnotation.STYLE_POPUP_POI_ANNOTATION);
            MapContainer.getInstance().addFeature(friendAnnotation);

            currentFriendAnnotations.addElement(friendAnnotation);
        }
    }

    private void addTitleContainer()
    {
        TnLinearContainer titleContainer = UiFactory.getInstance().createLinearContainer(ID_TITLE_CONTAINER, false,
            AbstractTnGraphics.VCENTER);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).SCREEN_WIDTH);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).LABEL_TITLE_HEIGHT);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((DriveWithFriendsUiDecorator) uiDecorator).ZERO_VALUE);
        titleContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((DriveWithFriendsUiDecorator) uiDecorator).ZERO_VALUE);
        View titlebarView = AndroidCitizenUiHelper.addContentView(titleContainer, R.layout.common_title);
        if (titlebarView != null)
        {
            TextView titleTextView = (TextView) titlebarView.findViewById(R.id.commonTitle0TextView);
            ImageView titleImageView = (ImageView) titlebarView.findViewById(R.id.commonTitle0IconButton);
            final Button titleImageViewDsr = (Button) titlebarView.findViewById(R.id.commonTitle0IconButton1);
            
            titleTextView.setText(R.string.dwfTitle);

            //titleImageViewDsr.setImageResource(R.drawable.inputbox_mic_icon);
            titleImageViewDsr.setVisibility(View.VISIBLE);
            titleImageViewDsr.setText("Hold to talk");
            titleImageViewDsr.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            titleImageViewDsr.setTextColor(R.color.label_white);
            
            titleImageView.setImageResource(R.drawable.title_icon_more_unfocused);
            titleImageView.setVisibility(View.VISIBLE);
            
            titleImageViewDsr.setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            titleImageViewDsr.setText("Release to send");
                            titleImageViewDsr.setTextColor(R.color.dwf_focus_text_color);
                            startRecording();
                            break;

                        case MotionEvent.ACTION_UP:
                            titleImageViewDsr.setText("Hold to talk");
                            titleImageViewDsr.setTextColor(R.color.label_white);
                            finishRecording();
                            break;
                    }
                    return false;
                }
            });
            
            TnMenu contextMenu = new TnMenu();
            Context context = AndroidPersistentContext.getInstance().getContext();
            contextMenu.add(context.getString(R.string.dwfFriendMenuLeaveGroup), CMD_LEAVE_GROUP);
            contextMenu.add(context.getString(R.string.dwfFriendMenuAddNew), CMD_INVITE_NEW);
            moreContextField = UiFactory.getInstance().createNullField(0);
            moreContextField.setMenu(contextMenu, AbstractTnComponent.TYPE_CONTEXT);
            moreContextField.setCommandEventListener(this);
            titleContainer.add(moreContextField);
            
            AndroidCitizenUiHelper.setOnClickCommand(this, titleImageView, CMD_SHOW_MENU);
        }

        MapContainer.getInstance().addFeature(titleContainer);
    }
    
    private void startRecording()
    {
        // MediaManager.getInstance().getRecordPlayer().record(15, os, this);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        recorder.setOutputFile(mFilePath + "/temp.3ga");
        try
        {
            recorder.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        recorder.start();
    }
    
    private void finishRecording()
    {
//        OutputStream oos = MediaManager.getInstance().getRecordPlayer().finish();
        if(null != recorder)
        {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    private void addDriveInfoContainer()
    {
        TnLinearContainer driveInfoContainer = UiFactory.getInstance().createLinearContainer(ID_DRIVE_INFO_CONTAINER, false,
            AbstractTnGraphics.VCENTER | AbstractTnGraphics.HCENTER);
        driveInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).DRIVE_INFO_WIDTH);
        driveInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).DRIVE_INFO_HEIGHT);
        driveInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((DriveWithFriendsUiDecorator) uiDecorator).DRIVE_INFO_X);
        driveInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((DriveWithFriendsUiDecorator) uiDecorator).DRIVE_INFO_Y);

        this.driveInfoNativeView = AndroidCitizenUiHelper.addContentView(driveInfoContainer, R.layout.dwf_list_drive);

        updateDriveInfoContainer();

        MapContainer.getInstance().addFeature(driveInfoContainer);

        AndroidCitizenUiHelper.setOnClickCommand(this, driveInfoNativeView, new int[][]
        {
        { R.id.dwfListDriveButton, CMD_DRIVE }, });

        Address address = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);
        if (address != null)
        {
            boolean isCurrentLocation = isCurrentLocation(address);
            if (isCurrentLocation)
            {
                View driveButton = driveInfoNativeView.findViewById(R.id.dwfListDriveButton);
                driveButton.setVisibility(View.GONE);
            }
        }
    }

    public static boolean isCurrentLocation(Address address)
    {
        if (address == null)
            return false;
        Stop stop = address.getStop();
        if (stop == null)
        {
            stop = address.getPoi().getStop();
        }

        int distance = UiFactory.getInstance().getDistance(stop, null);

        if (distance < 250)
            return true;

        return false;
    }

    private void updateDriveInfoContainer()
    {
        TextView labelView = (TextView) driveInfoNativeView.findViewById(R.id.dwfListDriveLabel);
        TextView streetView = (TextView) driveInfoNativeView.findViewById(R.id.dwfListDriveStreet);
        TextView cityView = (TextView) driveInfoNativeView.findViewById(R.id.dwfListDriveCity);
        Address dest = (Address) this.model.get(KEY_O_SELECTED_ADDRESS);

        if (dest != null)
        {

            Stop stop = dest.getStop();
            if (stop == null && dest.getPoi() != null && dest.getPoi().getStop() != null)
            {
                stop = dest.getPoi().getStop();
            }

            String label = stop.getLabel();
            Stop work = DaoManager.getInstance().getAddressDao().getOfficeAddress();
            Stop home = DaoManager.getInstance().getAddressDao().getHomeAddress();
            if (DaoManager.getInstance().getAddressDao().isSameStop(stop, work))
            {
                label = ResourceManager.getInstance().getCurrentBundle()
                                .getString(IStringDashboard.RES_STRING_WORK, IStringDashboard.FAMILY_DASHBOARD);
            }
            else if (DaoManager.getInstance().getAddressDao().isSameStop(stop, home))
            {
                label = ResourceManager.getInstance().getCurrentBundle()
                        .getString(IStringDashboard.RES_STRING_HOME, IStringDashboard.FAMILY_DASHBOARD);
            }
            labelView.setText(label);
            streetView.setText(stop.getFirstLine());
            cityView.setText(getCity(stop));

            TextView distanceView = (TextView) driveInfoNativeView.findViewById(R.id.dwfListDriveDistance);
            
            int dis = (int)NavRunningStatusProvider.getInstance().getDistanceToDestination();
            if (!NavRunningStatusProvider.getInstance().isNavRunning() || dis == -1 || !isCurrentDestination(dest))
            {
                distanceView.setText(UiFactory.getInstance()
                        .getDisplayDistance(UiFactory.getInstance().getDistance(stop, null)));
            }
            else
            {
                distanceView.setText(UiFactory.getInstance().getDisplayDistance(dis));
            }
            View driveButton = driveInfoNativeView.findViewById(R.id.dwfListDriveButton);
            driveButton.setVisibility(isCurrentLocation(dest) ? View.GONE : View.VISIBLE);

            if (NavRunningStatusProvider.getInstance().isNavRunning() && isCurrentDestination(dest))
            {
                driveButton.setVisibility(View.GONE);
            }
        }
        else
        {
            View driveButton = driveInfoNativeView.findViewById(R.id.dwfListDriveButton);
            driveButton.setVisibility(View.GONE);
        }
    }

    private boolean isCurrentDestination(Address address)
    {
        if (address == null)
            return false;
        Stop stop = address.getStop();
        if (stop == null)
        {
            stop = address.getPoi().getStop();
        }

        Address oldDestAddress =  NavRunningStatusProvider.getInstance().getDestination();
        if(oldDestAddress == null)
        {
            return false;
        }
        Stop oldDestStop = oldDestAddress.getStop();
        int distance = UiFactory.getInstance().getDistance(stop, oldDestStop);

        if (distance < 250)
            return true;

        return false;
    }

    private void addListInfoContainer()
    {
        TnLinearContainer listInfoContainer = UiFactory.getInstance().createLinearContainer(ID_LIST_INFO_CONTAINER, false,
            AbstractTnGraphics.TOP | AbstractTnGraphics.HCENTER);
        listInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).LIST_INFO_WIDTH);
        listInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_PREFER_HEIGHT,
            ((DriveWithFriendsUiDecorator) this.uiDecorator).LIST_INFO_HEIGHT);
        listInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((DriveWithFriendsUiDecorator) uiDecorator).ZERO_VALUE);
        listInfoContainer.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((DriveWithFriendsUiDecorator) uiDecorator).LIST_INFO_Y);

        View listInfoNativeView = AndroidCitizenUiHelper.addContentView(listInfoContainer, R.layout.dwf_list_list);

        View listTitle = listInfoNativeView.findViewById(R.id.dwf0ListTitle);
        listTitle.setPadding(
            AndroidCitizenUiHelper.getPixelsByDensity(listTitle.getPaddingLeft()),
            AndroidCitizenUiHelper.getPixelsByDensity(listTitle.getPaddingTop()),
            AndroidCitizenUiHelper.getPixelsByDensity(listTitle.getPaddingRight()),
            AndroidCitizenUiHelper.getPixelsByDensity(listTitle.getPaddingBottom()));
        this.dwfListListView = (ListView) listInfoNativeView.findViewById(R.id.dwfListListView);
        dwfListListView.setDivider(null);
        DriveWithFriendsListAdapter adapter = new DriveWithFriendsListAdapter();
        dwfListListView.setAdapter(adapter);

        updateListInfoContainer();

        MapContainer.getInstance().addFeature(listInfoContainer);
    }

    private void updateListInfoContainer()
    {
        DriveWithFriendsListAdapter adapter = (DriveWithFriendsListAdapter) this.dwfListListView.getAdapter();

        @SuppressWarnings("unchecked")
        ArrayList<Friend> friends = (ArrayList<Friend>) this.model.get(KEY_V_FRIENDS);
        ArrayList<Friend> adjustedFriends =  new ArrayList<Friend>();
        if (friends != null && friends.size() > 0)
        {
            for(Friend f : friends)
            {
                if (f.getKey().equals(model.getString(KEY_S_DWF_USER_KEY))
                        || (model.getString(KEY_S_DWF_USER_KEY) == null && f.getType().name().equals(FriendType.HOST.name())))
                {
                    adjustedFriends.add(0, f);
                }
                else
                {
                    adjustedFriends.add(f);
                }
            }
            adapter.setFriends(adjustedFriends, model.getString(KEY_S_DWF_USER_KEY));
        }
    }

    @SuppressWarnings("unchecked")
    private void updateFriendsPosition()
    {
        if (currentFriendAnnotations == null)
        {
            return;
        }

        List<Friend> friends = (List<Friend>) model.get(KEY_V_FRIENDS);

        ArrayList<Friend> newFriends = new ArrayList<Friend>();
        Hashtable<FriendAnnotation, Friend> updatedAnnotations = new Hashtable<FriendAnnotation, Friend>();

        // There are 3 types of friends:
        // 1. new added ones (exists in friends list but not in annotation vector).
        // 2. updated ones (exists both in friends list and annotation vector).
        // 3. deleted ones (only exist in annotation vector).
        for (int i = 0; i < friends.size(); i++)
        {
            Friend friend = friends.get(i);
            String key = friend.getKey();

            boolean isFound = false;
            for (int j = 0; j < currentFriendAnnotations.size(); j++)
            {
                FriendAnnotation friendAnnotation = (FriendAnnotation) currentFriendAnnotations.elementAt(j);
                String addedKey = friendAnnotation.getUserKey();

                if (key.equals(addedKey))
                {
                    isFound = true;
                    updatedAnnotations.put(friendAnnotation, friend);
                    currentFriendAnnotations.removeElement(friendAnnotation);
                    break;
                }
            }

            if (!isFound)
            {
                newFriends.add(friend);
            }
        }

        // The rest annotations don't exist in latest friend list.
        // Delete them.
        if (!currentFriendAnnotations.isEmpty())
        {
            for (int i = 0; i < currentFriendAnnotations.size(); i++)
            {
                FriendAnnotation friendAnnotation = currentFriendAnnotations.elementAt(i);

                MapContainer.getInstance().removeFeature(friendAnnotation);
            }

            currentFriendAnnotations.clear();
        }

        // add new friends
        if (!newFriends.isEmpty())
        {
            addFriends(newFriends);
        }

        // update existing friends
        if (!updatedAnnotations.isEmpty())
        {
            Hashtable<AbstractAnnotation, TnLocation> table = new Hashtable<AbstractAnnotation, TnLocation>();
            Enumeration<FriendAnnotation> keys = updatedAnnotations.keys();
            while (keys.hasMoreElements())
            {
                FriendAnnotation annotation = keys.nextElement();
                Friend newFriend = updatedAnnotations.get(annotation);

                if (newFriend.getLat() == 0 && newFriend.getLon() == 0)
                {
                    continue;
                }
                TnLocation location = new TnLocation("");
                int lat = (int) (newFriend.getLat() * 100000);
                int lon = (int) (newFriend.getLon() * 100000);
                location.setLatitude(lat);
                location.setLongitude(lon);

                table.put(annotation, location);

                currentFriendAnnotations.add(annotation);
            }

            if (!table.isEmpty())
            {
                MapContainer.getInstance().moveAnnotations(table, MapContainer.getInstance().getViewId() + "");
            }
        }
    }

    private void addDropButton()
    {
        FrogButton dropButton = UiFactory.getInstance().createButton(ID_DROP_BUTTON, "");
        dropButton.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_FOCUS);
        dropButton.getTnUiArgs().remove(TnUiArgs.KEY_BACKGROUND_IMAGE_UNFOCUS);
        dropButton.getTnUiArgs().put(TnUiArgs.KEY_PREFER_WIDTH, ((DriveWithFriendsUiDecorator) uiDecorator).DROP_BUTTON_WIDTH);
        dropButton.getTnUiArgs()
                .put(TnUiArgs.KEY_PREFER_HEIGHT, ((DriveWithFriendsUiDecorator) uiDecorator).DROP_BUTTON_HEIGHT);
        dropButton.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, ((DriveWithFriendsUiDecorator) uiDecorator).DROP_BUTTON_Y);
        dropButton.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X, ((DriveWithFriendsUiDecorator) uiDecorator).DROP_BUTTON_X);
        TnMenu menu = new TnMenu();
        menu.add("", CMD_DROP_MAP);
        dropButton.setMenu(menu, AbstractTnComponent.TYPE_CLICK);
        dropButton.setCommandEventListener(this);

        MapContainer.getInstance().addFeature(dropButton);
        updateDropButton(false);
    }

    private void updateDropButton(boolean isFullMap)
    {
        if (MapContainer.getInstance().getComponentById(ID_DROP_BUTTON) == null)
        {
            return;
        }
        FrogButton dropButton = (FrogButton) MapContainer.getInstance().getComponentById(ID_DROP_BUTTON);
        boolean isPortrait = ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).getOrientation() == AbstractTnUiHelper.ORIENTATION_PORTRAIT;
        if (isFullMap)
        {
            if (isPortrait)
            {
                dropButton.setIcon(ImageDecorator.DWF_MAP_FOLD_FOCUSED.getImage(),
                    ImageDecorator.DWF_MAP_FOLD_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
            else
            {
                dropButton.setIcon(ImageDecorator.DWF_MAP_FOLD_LANDSCAPE_FOCUSED.getImage(),
                    ImageDecorator.DWF_MAP_FOLD_LANDSCAPE_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER
                            | AbstractTnGraphics.VCENTER);
            }
        }
        else
        {
            if (isPortrait)
            {
                dropButton.setIcon(ImageDecorator.DWF_MAP_OPEN_FOCUSED.getImage(),
                    ImageDecorator.DWF_MAP_OPEN_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER | AbstractTnGraphics.VCENTER);
            }
            else
            {
                dropButton.setIcon(ImageDecorator.DWF_MAP_OPEN_LANDSCAPE_FOCUSED.getImage(),
                    ImageDecorator.DWF_MAP_OPEN_LANDSCAPE_UNFOCUSED.getImage(), AbstractTnGraphics.HCENTER
                            | AbstractTnGraphics.VCENTER);
            }
        }
    }

    private String getCity(Stop stop)
    {
        String city = stop.getCity() == null ? "" : stop.getCity();
        if (stop.getProvince() != null && stop.getProvince().length() > 0)
        {
            city += ", " + stop.getProvince();
        }
        if (stop.getPostalCode() != null && stop.getPostalCode().length() > 0)
        {
            city += ", " + stop.getPostalCode();
        }

        return city;
    }

    private AbstractTnComponent createMapCompanyLogo(final TnUiArgAdapter mapXAdapter, final TnUiArgAdapter yAdapter)
    {
        AbstractTnImage logo = DayNightService.getInstance().getMapColor() == IMapContainerConstants.MAP_NIGHT_COLOR ? ImageDecorator.IMG_NIGHT_LOGO_ON_MAP
                .getImage() : ImageDecorator.IMG_DAY_LOGO_ON_MAP.getImage();
        AbstractTnComponent mapCompanyLogo = UiFactory.getInstance().createFrogImageComponent(
            ICommonConstants.ID_MAP_COMPANY_LOGO, logo);

        mapCompanyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_X,
            new TnUiArgAdapter(PrimitiveTypeCache.valueOf(0), new ITnUiArgsDecorator()
            {
                public Object decorate(TnUiArgAdapter args)
                {
                    int padding = AppConfigHelper.getMinDisplaySize() * 315 / 10000;
                    ;
                    return mapXAdapter.getInt() + PrimitiveTypeCache.valueOf(padding);
                }
            }));
        mapCompanyLogo.getTnUiArgs().put(TnUiArgs.KEY_POSITION_Y, yAdapter);
        return mapCompanyLogo;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (smsTextView.getText().toString() != null 
                && !smsTextView.getText().toString().trim().equals("")
                && this.model.get(KEY_V_CONTACTS) != null
                && this.model.get(KEY_O_SELECTED_ADDRESS) != null)
        {
            dwfInviteButton.setEnabled(true);
        }
        else
        {
            dwfInviteButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    protected void activate()
    {
        // FIXME: adapter the font size of native XML configuration
        ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).setFontDensity(false);
        super.activate();
    }

}

///**
// *
// * Copyright 2011 TeleNav, Inc. All rights reserved.
// * SetAddressActivity.java
// *
// */
//package com.telenav.searchwidget.android;
//
//import java.util.Vector;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout.LayoutParams;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//
//import com.telenav.app.android.cingular.R;
//import com.telenav.i18n.ResourceBundle;
//import com.telenav.searchwidget.app.android.SearchApp;
//import com.telenav.searchwidget.data.AddressDao;
//import com.telenav.searchwidget.data.datatypes.address.Stop;
//import com.telenav.searchwidget.flow.android.GeocloudHandler;
//import com.telenav.searchwidget.flow.android.IWidgetActionHandler;
//import com.telenav.searchwidget.flow.android.IWidgetConstants;
//import com.telenav.searchwidget.flow.android.SearchController;
//import com.telenav.searchwidget.framework.android.WidgetParameter;
//import com.telenav.searchwidget.res.IStringSearchWidget;
//import com.telenav.searchwidget.res.ResUtil;
//import com.telenav.searchwidget.res.ResourceManager;
//import com.telenav.searchwidget.serverproxy.data.GeocodeBean;
//
///**
// * @author xinrongl (xinrongl@telenav.com)
// * @date Jul 29, 2011
// */
//
//public class SetAddressActivity extends Activity 
//                    implements OnClickListener, IWidgetActionHandler, OnCancelListener, BackEventListener, OnEditorActionListener
//{
//    private int widgetId;
//    private int layoutId;
//    private boolean isHome;
//    
//    private GeocloudHandler handler;
//    
//    private ProgressDialog dialog;
//    
//    private AlertDialog alertDialog;
//    
//    private Vector addrList;
//    
//    private ResourceBundle bundle;
//    
//    private boolean isInSetAddressPage;
//    
//    private EditText firstLineView;
//    private EditText lastLineView;
//    private ImageView firstlineCancelButton;
//    private ImageView lastlineCancelButton;
//    
//    
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        
//        SearchApp.getInstance().init(getApplicationContext());
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);   
//        
//        bundle = ResourceManager.getInstance().getCurrentBundle();
//        
//        showSetAddressScreen();
//    }
//    
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            if (isInSetAddressPage)
//            {
//                finish();
//            }
//            else
//            {
//                this.showSetAddressScreen();
//            }
//            return true;
//        }
//        
//        return false;
//    }
//
//    public void onClick(View v)
//    {
//        if (v.getId() == R.id.setup)
//        {
//            handleClickOnSetup();
//        }
//        else if (v.getId() == R.id.firstline_cancel)
//        {
//            firstLineView.setText("");
//            firstLineView.requestFocus();
//        }
//        else if (v.getId() == R.id.lastline_cancel)
//        {
//            lastLineView.setText("");
//            lastLineView.requestFocus();
//        }
//        else
//        {
//            handleClickOnAddress(v.getId());
//        }
//    }
//
//    public void handleWidgetAction(WidgetParameter wp)
//    {
//        dialog.dismiss();
//        
//        Vector addressList = (Vector)wp.get(IWidgetConstants.KEY_MATCHED_ADDRESSES);
//        
//        String warning = bundle.getString(IStringSearchWidget.RES_VALIDATE_ADDR_FAIL, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);        
//        if (addressList == null || addressList.size() == 0)
//        {
////            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
//            showAlert(warning);
//        }
//        else if (addressList.size() == 1)
//        {
//            GeocodeBean.MatchedAddress addr = (GeocodeBean.MatchedAddress)addressList.elementAt(0);
//            saveAddress(addr);
//            
//            finish();
//            
//            refreshAddress();
//        }
//        else //multiple matches
//        {
//            this.addrList = addressList;
//            showMultipleAddresses(addressList);
//        }
//    }
//
//    public void onCancel(DialogInterface dialog)
//    {
//        if (handler != null)
//        {
//            handler.cancel();
//        }
//    }
//    
//    public void handleBackEvent(boolean isUp)
//    {
//        if(isUp)
//        {
//            finish();
//        }
//        else
//        {
//            this.hideInputMethod();
//        }
//    }
//    
//    protected void hideInputMethod()
//    {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        if (imm != null)
//        {
//            imm.hideSoftInputFromWindow(firstLineView.getWindowToken(), 0);
//            imm.hideSoftInputFromWindow(lastLineView.getWindowToken(), 0);
//        }
//    }
//    
//    private void showAlert(final String message)
//    {
//    	final Context context = this;
//    	
//        this.runOnUiThread(new Runnable()
//        {
//            public void run()
//            {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setCancelable(true);
//                builder.setTitle("");
//
//                builder.setMessage(message);
//                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
//                {
//                    public void onClick(DialogInterface dialog, int id)
//                    {
//                        alertDialog.dismiss();
//                    }
//                });
//                
//                alertDialog = builder.show();
//            }
//        });        
//    }
//
//    
//    private void handleClickOnSetup()
//    {
//        String firstline = firstLineView.getText().toString();
//        String lastline = lastLineView.getText().toString();
//        
//        if (firstline == null || firstline.trim().length() == 0)
//        {
//            String warning = bundle.getString(IStringSearchWidget.RES_INPUT_FIRSTLINE, 
//                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
////            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
//            showAlert(warning);
//            return;
//        }
//        if (lastline == null || lastline.trim().length() == 0)
//        {
//            String warning = bundle.getString(IStringSearchWidget.RES_INPUT_LASTLINE, 
//                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
////            Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
//            showAlert(warning);
//            return;
//        }
//
//        this.hideInputMethod();
//        
//        String title = bundle.getString(IStringSearchWidget.RES_TITLE_VALIDATE_ADDR, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);        
//        dialog = ProgressDialog.show(this, "", title, true, true, this);
//        
//        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_REQUEST_GEOCODE);
//        wp.putString(IWidgetConstants.KEY_FIRSTLINE, firstline);
//        wp.putString(IWidgetConstants.KEY_LASTLINE, lastline);
//        handler = new GeocloudHandler(this, null);
//        handler.execute(wp);
//    }
//    
//    private void handleClickOnAddress(int position)
//    {
//        GeocodeBean.MatchedAddress addr = (GeocodeBean.MatchedAddress)addrList.elementAt(position);
//        
//        saveAddress(addr);
//        
//        finish();
//        
//        refreshAddress();
//    }
//    
//    private void saveAddress(GeocodeBean.MatchedAddress addr)
//    {
//        Stop stop = new Stop();
//        stop.setLat(addr.lat);
//        stop.setLon(addr.lon);
//        stop.setStreetNumber(addr.streetNumber);
//        stop.setStreetName(addr.streetName);
//        stop.setCrossStreetName(addr.crossStreetName);
//        stop.setCity(addr.city);
//        stop.setProvince(addr.state);
//        stop.setPostalCode(addr.postalCode);
//        stop.setCountry(addr.country);
//        
//        if (isHome)
//        {
//            AddressDao.getInstance().setHomeStop(stop);
//        }
//        else
//        {
//            AddressDao.getInstance().setOfficeStop(stop);
//        }
//    }
//    
//    private void showSetAddressScreen()
//    {
//        isInSetAddressPage = true;
//        
//        this.setContentView(R.layout.searchwidget_set_home);
//        
//        CatchBackLineaLayout catchBackLineaLayout = (CatchBackLineaLayout)this.findViewById(R.id.layout);
//        catchBackLineaLayout.setBackEventListener(this);
//
//        Bundle bundle1 = getIntent().getExtras();        
//        WidgetParameter wp = WidgetParameter.fromBundle(bundle1);
//        
//        widgetId = wp.getWidgetId();
//        layoutId = wp.getLayoutId();
//        isHome = wp.getBoolean(IWidgetConstants.KEY_IS_HOME);
//        
//        String addrTitle = bundle.getString(IStringSearchWidget.RES_HOME_TITLE, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        if (!isHome)
//        {
//            addrTitle = bundle.getString(IStringSearchWidget.RES_WORK_TITLE, 
//                    IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        }
//        String firstlineHint = bundle.getString(IStringSearchWidget.RES_FIRSTLINE_HINT, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        String lastlineHint = bundle.getString(IStringSearchWidget.RES_LASTLINE_HINT, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        String setup = bundle.getString(IStringSearchWidget.RES_SETUP, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        
//        TextView titleView = (TextView)this.findViewById(R.id.addrTitle);
//        titleView.setText(addrTitle);
//        firstLineView = (EditText)this.findViewById(R.id.firstline);
//        firstLineView.setHint(firstlineHint);
//        firstLineView.setOnEditorActionListener(this);
//        lastLineView = (EditText)this.findViewById(R.id.lastline);
//        lastLineView.setHint(lastlineHint);
//        lastLineView.setOnEditorActionListener(this);
//        Button setupButton = (Button)this.findViewById(R.id.setup);
//        setupButton.setText(setup);
//        
//        setupButton.setOnClickListener(this);  
//        
//        firstlineCancelButton = (ImageView)this.findViewById(R.id.firstline_cancel);
//        firstlineCancelButton.setOnClickListener(this);
//        firstLineView.addTextChangedListener(new AdressTextWatcher(firstLineView, firstlineCancelButton));
//        
//        lastlineCancelButton = (ImageView)this.findViewById(R.id.lastline_cancel);
//        lastlineCancelButton.setOnClickListener(this);
//        lastLineView.addTextChangedListener(new AdressTextWatcher(lastLineView, lastlineCancelButton));
//    }
//    
//    private void showMultipleAddresses(final Vector addrList)
//    {
//        isInSetAddressPage = false;
//        
//        this.runOnUiThread(new Runnable()
//        {
//            public void run()
//            {
//                showMultipleAddressesDelegate(addrList);                
//            }
//        });        
//    }
//    
//    private void showMultipleAddressesDelegate(Vector addrList)
//    {
//        setContentView(R.layout.searchwidget_multiple_addr);
//
//        LinearLayout layout = (LinearLayout)this.findViewById(R.id.addrList);
//        
//        String title = bundle.getString(IStringSearchWidget.RES_TITLE_ADDR_SELECT, 
//                IStringSearchWidget.FAMILY_SEARCHWIDGET);        
//        TextView titleView = (TextView)this.findViewById(R.id.title);
//        titleView.setText(title);
//        
//        int size = addrList.size();
//        
//        ScrollView scrollView = new ScrollView(this);
//        layout.addView(scrollView);
//        LinearLayout view = new LinearLayout(this);
//        view.setOrientation(LinearLayout.VERTICAL);
//        scrollView.addView(view);
//        
//        for (int i = 0; i < size; i ++)
//        {
//            GeocodeBean.MatchedAddress addr = (GeocodeBean.MatchedAddress)addrList.elementAt(i);
//            String addrStr = ResUtil.getOnelineAddress(addr);
//            
//            Button btn = new Button(this);
//            btn.setId(i);
//            btn.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//            btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            btn.setBackgroundResource(R.drawable.widgets_list_bg_1_unfocused);
//            btn.setText(addrStr);
//            btn.setOnClickListener(this);
//            view.addView(btn);
//        }
//    }
//
//    private void refreshAddress()
//    {
//        WidgetParameter wp = new WidgetParameter(widgetId, layoutId, IWidgetConstants.ACTION_REFRESH_ADDRESS);
//        SearchController.getInstance().handleWidgetAction(wp);
//    }
//
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
//    {
//        if ((null != event && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ||
//                actionId == EditorInfo.IME_ACTION_DONE)
//        {
//            String firstline = firstLineView.getText().toString();
//            String lastline = lastLineView.getText().toString();
//            if (firstline == null || firstline.trim().length() == 0 || lastline == null || lastline.trim().length() == 0)
//            {
//                return false;
//            }
//            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);;
//            if (imm != null) {
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//            }
//            handleClickOnSetup();
//        }
//        return false;
//    }
//
//    private static class AdressTextWatcher implements TextWatcher
//    {
//        private EditText textView;
//        private ImageView button; 
//        
//        /**
//         * @param textView
//         */
//        public AdressTextWatcher(EditText textView, ImageView button)
//        {
//            super();
//            this.textView = textView;
//            this.button = button;
//        }
//        
//        public void beforeTextChanged(CharSequence s, int start, int count, int after)
//        {
//            // TODO Auto-generated method stub
//            
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count)
//        {
//            // TODO Auto-generated method stub
//            
//        }
//        
//        public void afterTextChanged(Editable s)
//        {
//            String line = textView.getText().toString();
//            if (line == null || line.trim().length() == 0)
//            {
//                button.setVisibility(View.GONE);
//            }
//            else
//            {
//                button.setVisibility(View.VISIBLE);
//            }
//        } 
//	}
//}
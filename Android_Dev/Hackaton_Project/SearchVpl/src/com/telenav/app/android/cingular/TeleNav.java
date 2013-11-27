package com.telenav.app.android.cingular;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

public class TeleNav extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        setContentView(layout);
        
        String packName = this.getPackageName();
        String str = "market://details?id=" + packName;
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
        startActivity(myIntent);
        finish();
        
//        testInitRes();
    }
    
//    public void testInitRes()
//    {
//        AndroidPersistentContext.getInstance().init(this);
//        
//        if (AndroidPersistentContext.getInstance().getApplicationSQLiteDatabase() == null)
//        {
//            AndroidPersistentContext.getInstance().openSQLiteDatabase();
//        }
//        
//        if(AbstractPlatformIniter.getInstance() == null)
//        {
//            AbstractPlatformIniter.init(new AndroidPlatformIniter());
//        }
//        
//        AbstractPlatformIniter.getInstance().initUi();
//        AbstractPlatformIniter.getInstance().initIo();
//        AbstractPlatformIniter.getInstance().initNio();
//        AbstractPlatformIniter.getInstance().initNetwork();
//        AbstractPlatformIniter.getInstance().initPersistent();
//        
//        AppConfigHelper.load();
//        AddressDao.getInstance().setRootPath(this.getFilesDir().getAbsolutePath());
//        Stop homeStop = AddressDao.getInstance().getHomeStop();
//        Stop OfficeStop = AddressDao.getInstance().getOfficeStop();
//        
//        String str = ResourceManager.getInstance().getCurrentBundle().getString(5000, IStringSearchWidget.FAMILY_SEARCHWIDGET);
//        System.out.println("=========================== " + str);
//        
////        Object obj = NinePatchImageDecorator.instance.decorate(NinePatchImageDecorator.COMMON_BUTTON);
////        System.out.println("=========================== " + obj);
//        
//        class GeocloudProxyCallback implements IServerProxyListener
//        {
//
//            public boolean isAllowNetworkRequest(AbstractServerProxy proxy)
//            {
//                return true;
//            }
//
//            public void networkError(AbstractServerProxy proxy, byte statusCode)
//            {
//                
//            }
//
//            public void transactionError(AbstractServerProxy proxy)
//            {
//            }
//
//            public void transactionFinished(AbstractServerProxy proxy)
//            {
//                if (proxy instanceof GeocodeProxy)
//                {
//                    System.out.println("transactionFinished...");
//                    GeocodeProxy geocodeProxy = (GeocodeProxy) proxy;
//                    if (IServerProxyConstants.ACTION_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
//                    {
//                        GeocodeBean bean = geocodeProxy.getGeocodeBean();
//                        System.out.println(bean);
//                    }
//                    else if (IServerProxyConstants.ACTION_REVERSE_GEOCODE.equalsIgnoreCase(proxy.getRequestAction()))
//                    {
//                        ReverseGeocodeBean bean = geocodeProxy.getReverseGeocodeBean();
//                        System.out.println(bean);
//                    }
//                    else if (IServerProxyConstants.ACTION_GET_MAP.equalsIgnoreCase(proxy.getRequestAction()))
//                    {
//                        MapBean bean = geocodeProxy.getMapBean();
//                        System.out.println(bean);
//                    }
//                    else if (IServerProxyConstants.ACTION_GET_ETA.equalsIgnoreCase(proxy.getRequestAction()))
//                    {
//                        EtaBean bean = geocodeProxy.getEtaBean();
//                        System.out.println(bean);
//                    }
//                }
//                
//            }
//
//            public void updateTransactionStatus(AbstractServerProxy proxy, byte progress)
//            {
//            }
//            
//        }
//        
//        GeocloudProxyCallback proxyCallback = new GeocloudProxyCallback();
//        
//        GeocodeProvider.getInstance().requestReverseGeocode(37.3f, -122.3f, proxyCallback);
//        GeocodeProvider.getInstance().requestGeocode("1130 kifer rd, sunnyvale, ca", "", proxyCallback);
//        GeocodeProvider.getInstance().requestTrafficMap(37.23f, -122f,8, 256, false, proxyCallback);
//        GeocodeProvider.getInstance().requestEta("37.775234,-122.419221,37.3739,-121.9993", proxyCallback);
//    }
    
}
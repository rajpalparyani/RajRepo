/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TeleNav.java
 *
 */
package com.telenav.app.android.scout_us;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.telenav.ads.ThinkNearConversionTracker;
import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.CommManager;
import com.telenav.app.IApplication;
import com.telenav.app.INativeAppCallBack;
import com.telenav.app.NavServiceManager;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.TnSdLogCollector;
import com.telenav.app.android.AndroidLoggerListener;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.AndroidPlatformIniter;
import com.telenav.app.android.AndroidSdLogCollector;
import com.telenav.app.android.ExitNavSessionConfirmer;
import com.telenav.app.android.ExitNavSessionConfirmer.INavSessionEndListener;
import com.telenav.app.android.ShortcutManager;
import com.telenav.carconnect.host.CarConnectHostManager;
import com.telenav.carconnect.host.CarConnectRestartService;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.FileStoreManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.database.TnDatabaseManager;
import com.telenav.data.database.android.AndroidDatabaseManager;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfLogger;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.MainAppStatus;
import com.telenav.i18n.ResourceBundle;
import com.telenav.log.TnLoggerFilter;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.entry.SplashScreenInflater;
import com.telenav.module.entry.secretkey.DumpFileManager;
import com.telenav.module.marketbilling.MarketBillingHandler;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.nav.NavSdkNavEngine;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.IMvcUiThreadHelper;
import com.telenav.mvc.MvcContext;
import com.telenav.navservice.NavServiceApi;
import com.telenav.navservice.NavServiceParameter;
import com.telenav.navservice.android.AndroidNavServiceApi;
import com.telenav.navservice.android.NavServiceAN;
import com.telenav.persistent.TnFileConnection;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.maitai.impl.MaiTaiHandler;
import com.telenav.sdk.maitai.impl.MaiTaiManager;
import com.telenav.sdk.plugin.PluginAddress;
import com.telenav.sdk.plugin.PluginDataProvider;
import com.telenav.sdk.plugin.PluginManager;
import com.telenav.telephony.TnTelephonyManager;
import com.telenav.threadpool.INotifierListener;
import com.telenav.threadpool.Notifier;
import com.telenav.tnui.core.AbstractTnComponent;
import com.telenav.tnui.core.AbstractTnUiHelper;
import com.telenav.tnui.core.TnCommandEvent;
import com.telenav.tnui.core.TnScreen;
import com.telenav.tnui.core.TnUiEvent;
import com.telenav.tnui.core.android.AndroidActivity;
import com.telenav.tnui.core.android.AndroidUiEventHandler;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;
import com.telenav.ui.UiFactory;
import com.telenav.ui.citizen.map.MapContainer;
import com.telenav.util.PrimitiveTypeCache;
//import com.telenav.app.android.c2dm.C2DMessaging;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-7-1
 */
public class TeleNav extends AndroidActivity implements IMvcUiThreadHelper, IApplication, com.telenav.tnui.widget.android.IGLSnapBitmapCallBack
{
    public static boolean isDebugVerison = true;
    public static boolean isStarted = false;
    public boolean isLazyLoadFinished = false;
    protected static final String ACTION_OWNER = TeleNav.class.getName().toString();
    protected static final String ACTION_MAITAI = ACTION_OWNER + ".MAITAI";
    protected static final String ACTION_WIDGET = ACTION_OWNER + ".WIDGET";
    protected static final String ACTION_MAITAI_RESPONSE = ACTION_OWNER + ".RESPONSE";
    protected static final String ACTION_PLUGIN = ACTION_OWNER + ".PLUGIN_ACTION";
    protected static final String CATEGORY_MAITAI = "com.telenav.intent.category.MaiTai";
    protected static final String CATEGORY_TELENAV = "com.telenav.intent.category.TELENAV";
    protected static final String CATEGORY_PLUGIN = "com.telenav.intent.category.PLUGIN_LAUNCH";
    protected static final String MAITAI_RESPONSE= "com.telenav.maitai.response";
    protected static final String PLUGIN_REQUEST = "com.telenav.plugin.data";
    protected static final String PLUGIN_LAUNCH_NAV = "com.telenav.intent.action.DRIVE_TO";
    protected static final String PLUGIN_LAUNCH_MAP = "com.telenav.intent.action.VIEW_MAP";
    protected static final String PLUGIN_LAUNCH_BIZ = "com.telenav.intent.action.BIZ_FIND";
    protected static final String PLUGIN_LAUNCH_SHARE = "com.telenav.intent.action.SHARE_ADDRESS";
    protected static final String PLUGIN_LAUNCH_DWF_LIST = "com.telenav.intent.action.DWF_LIST";
    protected static final String FLAG_RESTARTAPP = "com.telenav.intent.flag.restartapp";
    protected static final String FLAG_C2DM = "com.telenav.intent.flag.isC2DM";
    protected static final String FLAG_AUTOWAKEUP = "com.telenav.intent.flag.autowakeup";
    protected static final String ACTION_REFRESH_WIDGET = "com.telenav.searchwidget.action.refreshwidget";

    protected static final String THINKNEAR_CONVERSION_TOKEN = "ZT0zNyZ2PTE3MiZvPTQxMQ";
    private String exitMsg = null;
    
    private BroadcastReceiver screenStateReceiver;
    private boolean isStopped;
    private boolean isActionBarHeightAdded = false;
    private BroadcastReceiver navserviceBroadcastReceiver;
    
    private INativeAppCallBack nativeAppCallback;
    private boolean isFinishOnCreate = false;
    
    private String issue_path = null;
    private String issue_send_out_path = null;
    private String issue_time_stamp = null;
    private Bitmap snappedBitmap;
    
    private INotifierListener switchAirplaneModeNotiferListener;
    
    boolean isShowingExitNavConfirm = false;
    
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        
        if (isStarted)
        {
            isFinishOnCreate = true;
            this.finish();
            return;
        }
        
        try
        {
            Class.forName("android.os.AsyncTask");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        AndroidPersistentContext.getInstance().init(this);
        AbstractPlatformIniter.init(new AndroidPlatformIniter());
        AbstractPlatformIniter.getInstance().initUi();
        AbstractPlatformIniter.getInstance().initIo();
        AbstractPlatformIniter.getInstance().initPersistent();
        AbstractPlatformIniter.getInstance().initNetwork();
        AbstractDaoManager.init(new DaoManager());
        TeleNavDelegate.getInstance().setApplication(this);
        
        DwfServiceConnection.getInstance().startDwfService(this);
        
        TnScreen splashScreen = SplashScreenInflater.getInstance().getSplashScreenView();
        showScreen(splashScreen);
        
        String phoneNumber = "";
        try
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            phoneNumber = tm.getLine1Number();
        }
        catch (Exception e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        Crashlytics.setString(ICrashlyticsConstants.KEY_PTN, phoneNumber);
        Crashlytics.start(this);
    }
    
    public void onStart()
    {
        super.onStart();
        if(isFinishOnCreate)
        {
            return;
        }
        
        if (!isStarted)
        {
            isStarted = true;
            boolean enableLogInFile = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_KONTAGENT_GENERATE_EVENT_LOG_FILE);
            boolean isUsingTestServer = ((DaoManager) DaoManager.getInstance()).getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_USING_KONTAGENT_TEST_SERVER);
			
            //This should be changed when release
            String apiKey = isDebugVerison ? KontagentLogger.QA_API_KEY : KontagentLogger.PRODUCTION_API_KEY;
            KontagentLogger.getInstance().start(this, apiKey, isUsingTestServer ? KontagentLogger.TEST_MODE : KontagentLogger.PRODUCTION_MODE, enableLogInFile);
           
            isLazyLoadFinished = false;
            Thread lazyLoader = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    synchronized (this)
                    {
                        try
                        {
                            this.wait(400);
                        }
                        catch (InterruptedException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    
                    runInUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            startApp();
                        }
                    });                    
                }
            });
            lazyLoader.start();
        }
    }
    
    protected boolean handleKeyDown(int keyCode, KeyEvent event)
    {
        boolean isHandled = false;
        if(currentScreen != null && currentScreen.getCurrentPopup() != null)
        {
            isHandled = AndroidUiEventHandler.onKeyDown(this.currentScreen.getCurrentPopup(), keyCode, event);
        }
        
        if(!isHandled)
        {
            isHandled = super.handleKeyDown(keyCode, event);
        }
        
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            isHandled = true;
        }
        
        return isHandled;
    }
    
    protected boolean handleKeyUp(int keyCode, KeyEvent event)
    {
        boolean isHandled = super.handleKeyUp(keyCode, event);
        
        if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH)
        {
            isHandled = true;
        }
        
        return isHandled;
    }
    
    public void runInUiThread(Runnable run)
    {
        this.runOnUiThread(run);
    }
    
    private void startApp()
    {
        try
        {
            ThinkNearConversionTracker tracker = new ThinkNearConversionTracker(THINKNEAR_CONVERSION_TOKEN);
            tracker.reportAppOpen(getApplicationContext());
        }
        catch(Exception e)
        {
            Logger.log(Logger.WARNING, this.getClass().getName(), "ThinkNear tracker Exception:" + e.getMessage());
        }
        
        BluetoothAdapter.getDefaultAdapter(); // This is only to force the BluetoothAdapter singleton to instantiate in the UI thread
        
        registerScreenStateReceiver();
        
        catchMaiTaiOrPlugin();
        
        if(AbstractTnGraphicsHelper.getInstance() == null)
        {
            AbstractTnGraphicsHelper.init(new AndroidUiHelper());
            
            AbstractTnGraphicsHelper.getInstance().init(this);
        }        
        if(AppConfigHelper.isTabletSize())
        {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
        if(NavServiceManager.getNavService() == null)
        {
            NavServiceManager.init(new AndroidNavServiceApi(this, NavServiceAN.class));
        }
        
        if(TeleNavDelegate.getInstance().getBillingHandler() == null)
        {
            MarketBillingHandler handler = new MarketBillingHandler();
            TeleNavDelegate.getInstance().setBillingHandler(handler);
            TeleNavDelegate.getInstance().getBillingHandler().setBilingServiceClass(
                BillingService.class);
        }
        
        // Fix bug http://jira.telenav.com:8080/browse/TN-1977
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        AbstractPlatformIniter.getInstance().initIo();
        
        AppConfigHelper.load();
        
        if (AndroidPersistentContext.getInstance().getApplicationSQLiteDatabase() == null)
        {
            AndroidPersistentContext.getInstance().openSQLiteDatabase();
        }
        
        MvcContext.getInstance().init(this);
        
        Logger.add(new AndroidLoggerListener(), new TnLoggerFilter());
        
        if (com.telenav.app.NotificationManager.getInstance() == null)
        {
        	com.telenav.app.NotificationManager.init(new AndroidNotificationManager(this));
        }
        //XXX better be moved to platformIniter
        if(TnDatabaseManager.getInstance() == null)
        {
            TnDatabaseManager.init(new AndroidDatabaseManager(this));
        }
        //should not add anymore codes here, please add it at TeleNavDelegate.java
        TeleNavDelegate.getInstance().startApp(null, this);
        
        try{
            if( AbstractDaoManager.getInstance().getBillingAccountDao().isLogin() &&
                    NavServiceManager.getNavService() != null )
            {
                startNavService();
            }
        }
        catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
        
        isLazyLoadFinished = true;
    }
    
    public void onNewIntent(final Intent intent)
    {
        super.onNewIntent(intent);

        if(isFinishOnCreate || !isLazyLoadFinished)
        {
            return;
        }
        
        boolean restartFlag = intent.getBooleanExtra(FLAG_RESTARTAPP, false);
        boolean isC2DM = intent.getBooleanExtra(FLAG_C2DM, false);
        boolean isNavRunning = NavSdkNavEngine.getInstance().isRunning();
        
        if (isC2DM && isNavRunning)
        {
            restartFlag = false;
        }
        
        if (restartFlag)
        {
            intent.setClassName(this, RestartService.class.getName());
            startService(intent);            
        }
        else
        {
            if (isC2DM && isNavRunning)
            {
                ExitNavSessionConfirmer.getInstance().showExitNavSessionConfirm(this, new INavSessionEndListener()
                {
                    @Override
                    public void onNavSessionEnd()
                    {
                        intent.setClassName(TeleNav.this, RestartService.class.getName());
                        startService(intent);
                    }
                    
                    @Override
                    public void onCancel()
                    {
                        
                    }
                });
            }
            else
            {
                this.setIntent(intent);
                catchMaiTaiOrPlugin();
            }
        }
    }
    
    public void onRestart()
    {
        super.onRestart();
        
        if(isFinishOnCreate || !isLazyLoadFinished)
        {
            return;
        }
        
        isStopped = false;
        if(TeleNavDelegate.getInstance().isExiting())
        {
            handleExit();
            return;
        }
        
        TeleNavDelegate.getInstance().activateApp(TeleNavDelegate.ACTIVIATE_TYPE_RESTART);
        
        try{
            if( AbstractDaoManager.getInstance().getBillingAccountDao().isLogin() &&
                    NavServiceManager.getNavService() != null )
            {
                NavServiceManager.getNavService().setForeground(true);
            }
        }
        catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }
    
    public void onStop()
    {
        super.onStop();
        KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_GLOBAL, KontagentLogger.GLOBAL_APP_BACKGROUND);
        if(isFinishOnCreate || !isLazyLoadFinished)
        {
            return;
        }
        
        isStopped = true;

        TeleNavDelegate.getInstance().deactivateApp(TeleNavDelegate.DEACTIVIATE_TYPE_STOP);
    
        if( AbstractDaoManager.getInstance().getBillingAccountDao().isLogin() &&
                NavServiceManager.getNavService() != null )
        {
            NavServiceManager.getNavService().setForeground(false);
        }
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        
        if(isFinishOnCreate)
        {
            return;
        }
        
        try
        {
            AndroidPersistentContext.getInstance().closeSQLiteDatabase();
            
            unregisterScreenStateReceiver();
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        
        if(TeleNavDelegate.getInstance().isExiting())
        {
            TeleNavDelegate.getInstance().cancelExitWaiting();
        }
        else
        {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if (null == currentScreen) 
        {
            menu.add(""); //add empty item for action bar
            return false;
        }
        super.onPrepareOptionsMenu(menu);
        if (AppConfigHelper.minSdkVersion >= 11 && menu.size() == 0)
        {
            menu.add(""); //add empty item for action bar
        }
        return true;
    }
    
    //Comment onOptionsItemSelected Start
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        Logger.log(Logger.INFO, this.getClass().getName(), "menu itemId : " + itemId);
        if (itemId == android.R.id.home)
        {   //add logic for home icno on action bar.
            boolean isHomeLauched = DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_HOME_LAUCHED);
            if (!isHomeLauched)
            {
                return super.onOptionsItemSelected(item); 
            }
            TnCommandEvent commandEvent = new TnCommandEvent(ICommonConstants.CMD_COMMON_HOME);
            TnUiEvent uiEvent = new TnUiEvent(TnUiEvent.TYPE_COMMAND_EVENT, this.currentScreen.getRootContainer());
            uiEvent.setCommandEvent(commandEvent);
            return this.currentScreen.getRootContainer().dispatchUiEvent(uiEvent);
        }
        else if(itemId == ICommonConstants.CMD_COMMON_REPORT_AN_ISSUE)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "Start Dumping files");
            startDumpLogfiles();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    //Comment onOptionsItemSelected end
    
    //Comment setContentView start
    protected void setContentView(View view, View leafFocusedView, View backgroundView)
    {
        super.setContentView(view, leafFocusedView, backgroundView);
        try
        { 
            if (!isActionBarHeightAdded && AppConfigHelper.minSdkVersion >= 11 && getActionBar() != null)
            {
                AppConfigHelper.setActionBarHeight(getActionBar().getHeight());
                isActionBarHeightAdded = true;
            }
        }
        catch(Throwable throwable)
        {
            Logger.log(this.getClass().getName(), throwable);
        }
    }
   //Comment setContentView end
    
    protected void startDumpLogfiles()
    {
        final ProgressDialog progressDialog = new ProgressDialog(TeleNav.this);
        
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Dumping logs ...");
                progressDialog.show();
            }
        });
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    createLogDirectory();
                    
                    dumpFiles();
                    
                    TeleNav.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                            
                            recordAudio();
                        }

                    });
                    
                    
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
            
        });
        t.start();
    }
    
    protected void recordAudio()
    {
        Intent result = new Intent();
        
        File logFoler = new File(issue_send_out_path);
        Uri uri = Uri.fromFile(logFoler);
        
        result.setData(uri);
        result.setClassName(RecordActivity.class.getPackage().getName(), RecordActivity.class.getName());
        startActivityForResult(result, RecordActivity.REQUEST_RECORD_AUDIO);
    }
    
    protected void createLogDirectory() throws IOException
    {
        File root = Environment.getExternalStorageDirectory();

        String path = root.getAbsolutePath() + "/" + FileStoreManager.TELENAV_DIRECTORY_PATH;
        path = path + "/feedback_logs";
        
        File logs_direcotry = new File(path);
        if (!logs_direcotry.exists())
        {
            boolean isCreated = logs_direcotry.mkdirs();
            if(!isCreated)
            {
                throw new IOException("create failed.");
            }
        }
        
        Time time = new Time();
        time.setToNow();
        
        issue_time_stamp = time.format("%Y%m%d%H%M%S");
        issue_path = path + "/logs_" + issue_time_stamp;
        
        File specific_issue_direcotry = new File(issue_path);
        if (!specific_issue_direcotry.exists())
        {
            boolean isCreated = specific_issue_direcotry.mkdir();
            if(!isCreated)
            {
                throw new IOException("create failed.");
            }
        }
        
        issue_send_out_path = issue_path + "/send_out_" + issue_time_stamp;
        
        File send_directory = new File(issue_send_out_path);
        if (!send_directory.exists())
        {
            boolean isCreated = send_directory.mkdir();
            if(!isCreated)
            {
                throw new IOException("create failed.");
            }
        }
    }
    
    protected void dumpFiles()
    {
        doScreenShot();
        dumpCacheLogs(false);
        dumpLogcat();
        dumpTnLogs();
    }
    
    protected void zipSendoutFolder()
    {
        final ProgressDialog progressDialog = new ProgressDialog(TeleNav.this);
        
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Compressing logs ...");
                progressDialog.show();
            }
        });
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    File logFoler = new File(issue_send_out_path);
                    File zipFile = new File(issue_path + "/issue_" + issue_time_stamp + ".zip");
                    ZipUtility.zipDirectory(logFoler, zipFile);
                    
                    Intent intent = getSendEmailIntent(zipFile);
                    
                    try
                    {
                        startActivityForResult(Intent.createChooser(intent, "Send mail..."), TeleNavDelegate.LAUNCH_EMAIL_SENDING_REQUESTCODE);
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        Toast.makeText(TeleNav.this,
                            "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                    
                    TeleNav.this.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            if (progressDialog.isShowing())
                            {
                                progressDialog.dismiss();
                            }
                        }

                    });
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
            
        });
        t.start();
    }
    
    protected Intent getSendEmailIntent(File file)
    {
        String title = "issue_" + issue_time_stamp;
        
        String ptn = TnTelephonyManager.getInstance().getPhoneNumber();
        if (ptn != null && ptn.length() > 0)
        {
            title = title + "_PTN_" + ptn;
        }
        
        title = title + "_Device_" + DaoManager.getInstance().getMandatoryNodeDao().getMandatoryNode().getClientInfo().device;
        
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]
        { "scoutandroidtest@gmail.com" });
        
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, "something to say");

        if (file != null && file.exists())
        {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }

        return intent;
    }
    
    protected void doScreenShot()
    {
        if( MapContainer.getInstance().hasMapUIEventListener() )
        {
            MapContainer.getInstance().requestOpenGLScreenShot(this);

            synchronized (this)
            {
                try
                {
                    this.wait(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }

        String mPath = issue_send_out_path + "/" + issue_time_stamp + ".png";

        // create bitmap screen capture
        Bitmap bitmap;
        View v1 = getWindow().getDecorView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        if(null == bitmap)
        {
            return;
        }
        v1.setDrawingCacheEnabled(false);

        Bitmap bmOverlay = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        Canvas canvas = new Canvas(bmOverlay);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        if(snappedBitmap != null)
        {
            canvas.drawBitmap(snappedBitmap, 0, bitmap.getHeight() - snappedBitmap.getHeight(), null);
            paint.setAlpha(125); // 50% translucent
        }

        canvas.drawBitmap(bitmap, 0, 0, paint);
        ByteArrayOutputStream snapshotbytes = new ByteArrayOutputStream();
        bmOverlay.compress(Bitmap.CompressFormat.PNG, 50, snapshotbytes);

        File shot = new File(mPath);

        try
        {
            boolean isCreated = shot.createNewFile();
            if(!isCreated)
            {
                throw new IOException("create failed.");
            }
            FileOutputStream sf = new FileOutputStream(shot);
            sf.write(snapshotbytes.toByteArray());
            sf.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        if (snappedBitmap != null)
        {
            snappedBitmap.recycle();
            snappedBitmap = null;
        }

        bitmap.recycle();
        bitmap = null;
 
    }
    
    protected void dumpTnLogs()
    {
        if (TnSdLogCollector.getInstance() instanceof AndroidSdLogCollector)
        {
            AndroidSdLogCollector androidSdLogCollector = (AndroidSdLogCollector) TnSdLogCollector.getInstance();
            String[] sentLogName = new String[2];
            TnFileConnection[] files = androidSdLogCollector.getTwoOfTwoDayLatestLog(sentLogName);
            
            if (files != null && files.length > 0)
            {
                for (int i = 0; i < files.length; i++)
                {
                    TnFileConnection file = files[i];
                    if (file != null)
                    {
                        File sourceFile = new File(file.getPath());
                        File targetFile = new File(issue_send_out_path + "/" + file.getName());
                        DumpFileManager.getInstance().copyFile(sourceFile, targetFile);
                    }
                }
            }
        }
    }
    
    protected void dumpLogcat()
    {
        try
        {
            java.lang.Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            File parentFolder = new File(issue_send_out_path);
            File file = new File(parentFolder, "logcat.txt");
            
            if(!file.exists())
            {
                boolean isCreated = file.createNewFile();
                if(!isCreated)
                {
                    Log.e("TeleNav", "Create New File Failed! " );
                }
            }
            
            String line = null;
            
            FileWriter writer = new FileWriter(file);
            
            while ((line = bufferedReader.readLine()) != null)
            {
                writer.write(line + "\n");
            }
            writer.flush();
            writer.close();
            bufferedReader.close();
        }
        catch (Exception e)
        {
            
        }
    }

    private void dumpCacheLogs(final boolean isNavSDKLogEnable)
    {
        DumpFileManager manager = DumpFileManager.getInstance();

        File sourceDirectory = new File(manager.getAppPath() + "/cache");
        File[] files = sourceDirectory.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File file)
            {
                String name = file.getName();
                if (name == null || name.trim().length() == 0)
                {
                    return false;
                }

                if (isNavSDKLogEnable && name.startsWith("navSDK") && name.endsWith(".log"))
                {
                    return true;
                }
                else if (name.endsWith(".dmp"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });

        if (files != null && files.length > 0)
        {
            for (int i = 0; i < files.length; i++)
            {
                File tmpFile = files[i];

                String fileName = tmpFile.getName();
                String tmpPath = null;
                if (fileName.startsWith("navSDK") && fileName.endsWith(".log"))
                {
                    tmpPath = issue_path;
                }
                else
                {
                    tmpPath = issue_send_out_path;
                }

                File targetFile = new File(tmpPath + "/" + fileName);
                DumpFileManager.getInstance().copyFile(tmpFile, targetFile);
            }
        }
    }

    protected void startRecordActivity()
    {
        
    }
    
    public void exit(Object params, String exitMsg)
    {
        this.exitMsg = exitMsg;
        stopNavService();
        
        try
        {
            exitMaiTai(params);
        }
        catch (Throwable e)
        {
            Logger.log(this.getClass().getName(), e);
        }
        finally
        {
            try
            {
                AndroidPersistentContext.getInstance().closeSQLiteDatabase();
            }
            catch (Throwable e)
            {
                Logger.log(this.getClass().getName(), e);
            }
            
            if (CarConnectHostManager.getInstance().getState() >= CarConnectHostManager.STATE_MONITOR
                    && CarConnectHostManager.getInstance().isBluetoothConnected())
            {
                Intent restartServiceIntent = new Intent(this.getApplicationContext(), CarConnectRestartService.class);
                restartServiceIntent.putExtra(CarConnectRestartService.EXTRA_NAME, CarConnectRestartService.RESTART);
                this.getApplicationContext().startService(restartServiceIntent);
            }
            this.finish();
            
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void jumpToBackground(Object params)
    {
        exitMaiTai(params);
        
        this.moveTaskToBack(true);
    }
    
    protected void catchMaiTaiOrPlugin()
    {
        Intent action = this.getIntent();
        if( action == null )
        {
            return;
        }
        
        MaiTaiManager maitai = MaiTaiManager.getInstance();
        maitai.setFromMaiTai(false);
        PluginManager plugin = PluginManager.getInstance();
        plugin.setFromPlugin(false);
        ShortcutManager.getInstance().setFromShortcut(false);
        
        
        if ( ACTION_MAITAI.equalsIgnoreCase(action.getAction()) 
            || ACTION_WIDGET.equalsIgnoreCase(action.getAction()) )
        {
            int startFrom = 0;
            if(ACTION_MAITAI.equalsIgnoreCase(action.getAction()))
            {
                startFrom = 2;
            }
            else if(ACTION_WIDGET.equalsIgnoreCase(action.getAction()))
            {
                startFrom = 3;
            }
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAITAI, KontagentLogger.MAITAI_APPLICATION_LAUNCH, startFrom);
            MaiTaiHandler.getInstance().clear();
            maitai.setFromMaiTai(true);
            if ( action != null )
            {
                if ( action.getData() != null )
                {
                    maitai.setUri(action.getData().toString());
                }
                
                if (action.getExtras() != null)
                {
                    maitai.setBackupUri(action.getExtras().getString(
                        MaiTaiManager.PARAM_URI));
                }
                
                if (ACTION_WIDGET.equalsIgnoreCase(action.getAction()))
                {
                    maitai.setNeedValidate(false);
                }
                
                try
                {
                    boolean autowakeup = action.getBooleanExtra(FLAG_AUTOWAKEUP, false);
                    if (!autowakeup)
                    {
                        NotificationManager mNotificationManager = (NotificationManager) this
                                .getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.cancelAll();
                    }
                }
                catch(Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }
        else if( ACTION_PLUGIN.equalsIgnoreCase(action.getAction()) )
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAITAI, KontagentLogger.MAITAI_APPLICATION_LAUNCH, 2);
            PluginDataProvider.getInstance().clear();
            //catch Plugin
            plugin.setFromPlugin(true);
            Object request = action.getExtras().get(PLUGIN_REQUEST);
            if( request instanceof HashMap )
            {
                HashMap map = (HashMap)request;
                Hashtable data = new Hashtable();
                data.put(IMaiTai.KEY_SELECTED_MENU_ITEM, map.get(IMaiTai.KEY_SELECTED_MENU_ITEM));
                Object addr = map.get(IMaiTai.KEY_ONE_BOX_ADDRESS);
                if( addr instanceof ArrayList )
                {
                    ArrayList list = (ArrayList)addr;
                    Vector address = new Vector();
                    int size = list.size();
                    for( int i = 0 ; i < size ; i++ )
                    {
                        PluginAddress pluginAddr = (PluginAddress) list.get(i);
                        String tmpAddress = pluginAddr.getAddressLine();
                        if(tmpAddress != null && tmpAddress.indexOf("@") != -1)
                        {
                            String[] addStr = tmpAddress.split("@");
                            
                            String[] latLon = addStr[1].split(",");
                            int lat = 0;
                            int lon = 0;
                            try
                            {
                                if (latLon[0] != null)
                                {
                                    if (latLon[0].indexOf(".") >= 0)
                                    {
                                        lat = (int)(Double.parseDouble(latLon[0]) * 100000);
                                    }
                                    else
                                    {
                                        lat = Integer.parseInt(latLon[0]);
                                    }
                                }
                                if (latLon[1] != null)
                                    if (latLon[1].indexOf(".") >= 0)
                                    {
                                        lon = (int)(Double.parseDouble(latLon[1]) * 100000);
                                    }
                                    else
                                    {
                                        lon = Integer.parseInt(latLon[1]);
                                    }
                                
                                if(lat != 0 && lon != 0)
                                {
                                    Stop stop = new Stop();
                                    stop.setFirstLine(addStr[0]);
                                    stop.setStreetName(addStr[0]);
                                    stop.setHouseNumber("");
                                    stop.setLat(lat);
                                    stop.setLon(lon);
                                    data.put(IMaiTai.KEY_SELECTED_ADDRESS, stop);
                                    continue;
                                }
                            }
                            catch(Exception e)
                            {
                                Logger.log(this.getClass().getName(), e);
                            }
                        }
                        //Don't save address if it is empty - TNANDROID-2355
                        boolean saveAddress = tmpAddress != null
                                && !(("").compareToIgnoreCase(tmpAddress) == 0);
                        if (saveAddress)
                            address.addElement(pluginAddr);
                    }
                    data.put(IMaiTai.KEY_ONE_BOX_ADDRESS, address);
                }
                
                Object item = map.get(IMaiTai.KEY_SEARCH_ITEM);
                if( item instanceof String )
                {
                    data.put(IMaiTai.KEY_SEARCH_ITEM, item);
                }
                plugin.setRequestData(data);
            }
        }
        else if( PLUGIN_LAUNCH_NAV.equalsIgnoreCase(action.getAction()) ||
                PLUGIN_LAUNCH_MAP.equalsIgnoreCase(action.getAction()) ||
                PLUGIN_LAUNCH_BIZ.equalsIgnoreCase(action.getAction()) ||
                PLUGIN_LAUNCH_SHARE.equalsIgnoreCase(action.getAction()) )
        {
            Object request = action.getExtras().get(PLUGIN_REQUEST);
            if ( request instanceof HashMap )
            {
                HashMap map = (HashMap)request;
                Hashtable table = new Hashtable();
                table.putAll(map);
                plugin.setCallFromExternal(table);
            }
        }
        else if(PLUGIN_LAUNCH_DWF_LIST.equalsIgnoreCase(action.getAction()))
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_MAITAI,
                KontagentLogger.MAITAI_APPLICATION_LAUNCH, 4);
            
//            Logger.dwfLog(DwfLogger.INFO, "Launch Dwf Session");

            Hashtable table = new Hashtable();
            String tinyUrl = action.getStringExtra(IMaiTai.KEY_DWF_TINY_URL);
            
            if (tinyUrl != null && tinyUrl.trim().length() > 0)
            {
//                Logger.dwfLog(DwfLogger.INFO, "tinyUrl : " + tinyUrl);
                table.put(IMaiTai.KEY_DWF_TINY_URL, tinyUrl);
            }
            else
            {
                String sessionId = action.getStringExtra(IMaiTai.KEY_DWF_SESSION_ID);
                String userId = action.getStringExtra(IMaiTai.KEY_DWF_USER_ID);
                String userKey = action.getStringExtra(IMaiTai.KEY_DWF_USER_KEY);
                String addressDt = action.getStringExtra(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
                
//                Logger.dwfLog(DwfLogger.INFO, "sessionId : " + sessionId);
//                Logger.dwfLog(DwfLogger.INFO, "userId : " + userId);
//                Logger.dwfLog(DwfLogger.INFO, "userKey : " + userKey);
//                Logger.dwfLog(DwfLogger.INFO, "addressDt : " + addressDt);

                table.put(IMaiTai.KEY_DWF_SESSION_ID, sessionId);
                table.put(IMaiTai.KEY_DWF_USER_ID, userId == null ? "" : userId);
                table.put(IMaiTai.KEY_DWF_USER_KEY, userKey);
                table.put(IMaiTai.KEY_DWF_ADDRESS_FORMATDT, addressDt == null ? "" : addressDt);
            }
            plugin.setCallFromDWF(table);
        }
        else if (ShortcutManager.ACTION_SHORTCUT.equalsIgnoreCase(action.getAction()))
        {
            ShortcutManager.getInstance().setFromShortcut(true);
            String data = action.getExtras().getString(ShortcutManager.SHORTCUT_REQUEST);
            ShortcutManager.getInstance().setAction(data);
        }
        else //android.intent.action.MAIN
        {
            KontagentLogger.getInstance().addCustomEvent(KontagentLogger.CATEGORY_GLOBAL, KontagentLogger.GLOBAL_APP_LAUNCH);
        }
    }
    
    protected void exitMaiTai(Object params)
    {
        if( MaiTaiManager.getInstance().isMaiTaiExit() )
        {
            MaiTaiManager.getInstance().setFromMaiTai(false);
            Intent intent = new Intent();
            intent.setAction(ACTION_MAITAI_RESPONSE);
            intent.addCategory(CATEGORY_MAITAI);
            if( params != null && params instanceof Hashtable )
            {
                intent.putExtra(MAITAI_RESPONSE, (Hashtable)params);
            }
            this.sendBroadcast(intent);
            return;
        }
        if( PluginManager.getInstance().isPluginExit() )
        {
            PluginManager.getInstance().setFromPlugin(false);
            return;
        }
    }

    public Object callAppNativeFeature(String feature, Object[] params, INativeAppCallBack nativeAppCallback)
    {
        this.nativeAppCallback = nativeAppCallback;
        /*if(TeleNavDelegate.C2DM_FEATURE_APP_PRELOAD.equals(feature))
        {
            preloadC2DMNativeApp();
        }
        else*/ if(TeleNavDelegate.LAUNCH_LOCATION_SETTING.equals(feature))
        {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try
            {
                this.startActivityForResult(intent,TeleNavDelegate.LAUNCH_LOCATION_SETTING_REQUESTCODE);
            }
            catch (ActivityNotFoundException ex)
            {
                Logger.log(this.getClass().getName(), ex);
                
                intent.setAction(Settings.ACTION_SETTINGS);
                try
                {
                    this.startActivity(intent);
                }
                catch (Exception e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
            }
        }
        else if (TeleNavDelegate.FEATURE_NOTIFY_REFRESH_WIDGET.equals(feature))
        {
        	Intent intent = new Intent();
        	intent.setAction(ACTION_REFRESH_WIDGET);
        	sendBroadcast(intent);
        }
        else if (TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE.equals(feature))
        {
            boolean isPanMode = false;
            boolean isNothing = false;
            boolean isAlwaysHidden = false;
            if(params != null && params.length > 0)
            {
                if(params[0] instanceof Boolean)
                {
                    Boolean param_mode = (Boolean)params[0];
                    isPanMode = param_mode.booleanValue();
                }
                if(params.length > 1 && params[1] instanceof Boolean)
                {
                    isNothing = ((Boolean)params[1]).booleanValue();
                }
                if(params.length > 2 && params[2] instanceof Boolean)
                {
                    isAlwaysHidden = ((Boolean)params[2]).booleanValue();
                }
            }
            
            if(isPanMode)
            {
                runInUiThread(new Runnable()
                {
                    public void run()
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }
                });
            }
            else if(isNothing)
            {
                runInUiThread(new Runnable()
                {
                    public void run()
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    }
                });
            }
            else if (isAlwaysHidden)
            {
                runInUiThread(new Runnable()
                {
                    public void run()
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                    }
                });
            }
            else
            {
                runInUiThread(new Runnable()
                {
                    public void run()
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    }
                });
            }
        }
        else if (TeleNavDelegate.SWTICH_AIRPLANE_MODE.equals(feature))
        {
            TeleNavDelegate.IS_START_CRAZY_SWITCH = !TeleNavDelegate.IS_START_CRAZY_SWITCH;
            if (TeleNavDelegate.IS_START_CRAZY_SWITCH)
            {
                if (switchAirplaneModeNotiferListener == null)
                {
                    switchAirplaneModeNotiferListener = new INotifierListener()
                    {
                        private long lastNotifyTimestamp;
                        
                        public void setLastNotifyTimestamp(long timestamp)
                        {
                            lastNotifyTimestamp = timestamp;
                        }
                        
                        public void notify(long timestamp)
                        {
                            boolean isEnabled = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
                            Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled?0:1);
                            Intent i=new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                            i.putExtra("state", !isEnabled);
                            sendBroadcast(i);
                        }
                        
                        public long getNotifyInterval()
                        {
                            return 60000;
                        }
                        
                        public long getLastNotifyTimestamp()
                        {
                            return lastNotifyTimestamp;
                        }
                    };
                }
                Notifier.getInstance().addListener(switchAirplaneModeNotiferListener);
            }
            else
            {
                if (switchAirplaneModeNotiferListener != null)
                {
                    Notifier.getInstance().removeListener(switchAirplaneModeNotiferListener);
                }
            }
        }
        return null;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TeleNavDelegate.LAUNCH_LOCATION_SETTING_REQUESTCODE)
        {
            if (nativeAppCallback != null)
            {
                this.nativeAppCallback.nativeAppCallBack(requestCode, resultCode);
                this.nativeAppCallback = null;
            }
        }
        else if (requestCode == RecordActivity.REQUEST_RECORD_AUDIO)
        {
            zipSendoutFolder();
        }
        else if (requestCode == TeleNavDelegate.LAUNCH_EMAIL_SENDING_REQUESTCODE)
        {
            //close input method forcedly
            TeleNavDelegate.getInstance().callAppNativeFeature(
                TeleNavDelegate.FEATURE_UPDATE_WINDOW_SOFT_INPUT_MODE, new Object[]{PrimitiveTypeCache.valueOf(false), PrimitiveTypeCache.valueOf(false),PrimitiveTypeCache.valueOf(true)});
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Object getDataFromNativeApp(String key)
    {
//        if (TeleNavDelegate.KEY_C2DM_REGISTRATION_ID.equals(key))
//        {
//            String id = C2DMessaging.getRegistrationId(this);
//            return id;
//        }
        return null;
    }
    
    private void handleExit()
    {
        final ProgressDialog a = new ProgressDialog(TeleNav.this);
        
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                LinearLayout fullScreen = new LinearLayout(TeleNav.this);
                TeleNav.this.setContentView(fullScreen);
                a.setCancelable(false);
                ResourceBundle bundle = ResourceManager.getInstance().getCurrentBundle();
                if(bundle!=null && exitMsg == null)
                {
                    exitMsg = bundle.getString(IStringCommon.RES_MSG_APPLICATION_EXITING, IStringCommon.FAMILY_COMMON);
                }
                //fall through if not defined.
                if(exitMsg == null)
                {
                    exitMsg = "Application is exiting...";
                }
                a.setMessage(exitMsg);
                a.show();
            }
        });
        
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e)
                {
                    Logger.log(this.getClass().getName(), e);
                }
                finally
                {
                    TeleNavDelegate.getInstance().cancelExitWaiting();
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            a.dismiss();
                        }
                    });
                }
            }
        });
        t.start();

    }    
    
//    private void preloadC2DMNativeApp()
//    {
//        ((AbstractTnUiHelper) AbstractTnUiHelper.getInstance()).runOnUiThread(new Runnable()
//        {
//            public void run()
//            {
//                boolean needSendC2DM = false;
//                String id = C2DMessaging.getRegistrationId(TeleNav.this);
//                if((id == null || id.trim().length() == 0) || !DaoManager.getInstance().getBillingAccountDao().isLogin())
//                {
//                    needSendC2DM = true;
//                }
//                else
//                {
//                    
//                    boolean isC2dmSuccess = C2DMessaging.isSentToServer(TeleNav.this);
//                    needSendC2DM = !isC2dmSuccess;
//                    
//                    String serverDrivenAccount = DaoManager.getInstance().getServerDrivenParamsDao().getValue(ServerDrivenParamsDao.C2DM_SENDER_GOOGLE_ACCOUNT);
//                    if(serverDrivenAccount != null && serverDrivenAccount.trim().length() > 0)
//                    {
//                        String localeC2dmAccount = ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().getString(SimpleConfigDao.C2DM_SENDER_GOOGLE_ACCOUNT);
//                        if(!serverDrivenAccount.equals(localeC2dmAccount) && !C2DMReceiver.C2DM_TN_SENDER.equals(serverDrivenAccount))
//                        {
//                            needSendC2DM = true;
//                            ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().put(SimpleConfigDao.C2DM_SENDER_GOOGLE_ACCOUNT, serverDrivenAccount);
//                            ((DaoManager)DaoManager.getInstance()).getSimpleConfigDao().store();
//                        }
//                    }
//                }
//                
//                if(needSendC2DM)
//                {
//                    C2DMessaging.register(TeleNav.this, C2DMReceiver.C2DM_TN_SENDER);
//                }
//            }
//        });
//    }
    
    protected void onScreenOn()
    {
        //Important!!! Need check if the app is in foreground, otherwise don't call activateApp
        if (!isStopped)
        {
            TeleNavDelegate.getInstance().activateApp(TeleNavDelegate.ACTIVIATE_TYPE_BACKLIGHT_ON);
            if( AbstractDaoManager.getInstance().getBillingAccountDao().isLogin() &&
                    NavServiceManager.getNavService() != null )
            {
                NavServiceManager.getNavService().setForeground(true);
            }
            try
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                if (dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.setMainAppStatus(MainAppStatus.foreground.name());
                        if (dwfAidl.getSharingIntent() != null && !NavRunningStatusProvider.getInstance().isNavRunning())
                        {
                            dwfAidl.enableNotification(true, MainAppStatus.foreground.name());
                        }
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        TeleNavDelegate.getInstance().screenOn();
    }
    
    protected void onScreenOff()
    {
        if(!isStopped)
        {
            TeleNavDelegate.getInstance().deactivateApp(TeleNavDelegate.DEACTIVIATE_TYPE_BACKLIGHT_OFF);
            if( AbstractDaoManager.getInstance().getBillingAccountDao().isLogin() &&
                    NavServiceManager.getNavService() != null )
            {
                NavServiceManager.getNavService().setForeground(false);
            }
            try
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                if (!NavRunningStatusProvider.getInstance().isNavRunning() && dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.setMainAppStatus(MainAppStatus.background.name());
                        if (dwfAidl.getSharingIntent() != null)
                        {
                            dwfAidl.enableNotification(true, MainAppStatus.background.name());
                        }
                    }
                    catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                Logger.log(this.getClass().getName(), e);
            }
        }
        TeleNavDelegate.getInstance().screenOff();
    }
    
    protected void registerScreenStateReceiver()
    {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SHUTDOWN);
        screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(screenStateReceiver, filter);          
    }
    
    protected void unregisterScreenStateReceiver()
    {
        if (screenStateReceiver != null)
        {
            unregisterReceiver(screenStateReceiver);
            screenStateReceiver = null;
        }
    }
    
    public void closeVirtualKeyBoard(AbstractTnComponent component)
    {
        IBinder windowTokey = null;
        
        try
        {
            if (component == null || component.getNativeUiComponent() == null)
            {
               if (this.getCurrentFocus() != null)
                {
                    windowTokey = this.getCurrentFocus().getWindowToken();
                }
            }
            else
            {
                View view = (View) component.getNativeUiComponent();
                windowTokey = view.getWindowToken();
            }

            if (windowTokey == null)
            {
                return;
            }

            InputMethodManager inputMethodManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(windowTokey, 0);
        }
        catch (Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }
    
    class ScreenStateReceiver extends BroadcastReceiver 
    {  
        public void onReceive(Context context, Intent intent) 
        {  
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()))
            {
                onScreenOff();
            } 
            else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) 
            {  
                onScreenOn();
            } 
            else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction()))
            {
                TeleNavDelegate.getInstance().exitApp(true, null);
            } 
        }  
    } 
    
    private void stopNavService()
    {
        try
        {
            if (NavServiceManager.getNavService() != null)
            {
                NavServiceManager.getNavService().stopNavService();
            }

            if (navserviceBroadcastReceiver != null)
            {
                unregisterReceiver(navserviceBroadcastReceiver);
            }
        }
        catch (Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }
 
    private void startNavService()
    {
        IntentFilter eventFilter = new IntentFilter();
        eventFilter.addAction(NavServiceApi.NAV_SERVICE_CALLBACK_ACTION);
        
        if(this.navserviceBroadcastReceiver == null)
        {
           this.navserviceBroadcastReceiver = new BroadcastReceiver() {

                public void onReceive(Context context, Intent intent)
                {
                    try{
                        String action = intent.getAction();
                        Logger.log(Logger.INFO, this.getClass().getName(), "onReceive: got intent " + intent + " with action " + action);
                        if (NavServiceApi.NAV_SERVICE_CALLBACK_ACTION.equals(action))
                        {
                            new Thread(new Runnable()
                            {
                                public void run()
                                {
                                    try{
                                        if (NavServiceManager.getNavService() != null)
                                        {
                                            NavServiceParameter param = getNavServiceParamters();
                                            NavServiceManager.getNavService().setNavServiceParameters(param);
                                        }
                                    }
                                    catch(Throwable t)
                                    {
                                        Logger.log(this.getClass().getName(), t);
                                    }
                                }
                            }).start();
                        }
                    }
                    catch(Throwable t)
                    {
                        Logger.log(this.getClass().getName(), t);
                    }
                }
            };
        }
        
        try{
            registerReceiver(this.navserviceBroadcastReceiver, eventFilter);
            
            NavServiceParameter param = getNavServiceParamters();
            param.setForeground(true);
            NavServiceManager.getNavService().startNavService(param);
        }
        catch(Throwable t)
        {
            Logger.log(this.getClass().getName(), t);
        }
    }
    
    private NavServiceParameter getNavServiceParamters()
    {
        NavServiceParameter param = new NavServiceParameter();
        
        //this is used to monitor if client is still running
        param.setPid(Process.myPid());
        
        Host host = CommManager.getInstance().getComm().getHostProvider().createHost(IServerProxyConstants.ACT_SYNC_SERVICE_LOCATOR);
        if(host != null && host.host != null)
        {
            param.setServerUrl(CommManager.getInstance().getComm().getHostProvider().getUrl(host));
        }
        MandatoryNodeDao dao = DaoManager.getInstance().getMandatoryNodeDao();
        if (dao != null)
        {
            String userId = dao.getMandatoryNode().getUserInfo().userId;
            if(userId != null && userId.trim().length() > 0)
            {
                param.setUserId("tel:"+userId);
            }
        }
        
        param.setLogEnabled(AppConfigHelper.isLoggerEnable);
        if (NavSdkNavEngine.getInstance().isRunning() && NavSdkNavEngine.getInstance().getCurrentNavState() != null)
        {
            param.setRouteId(""+NavSdkNavEngine.getInstance().getCurrentNavState().getRoute());
        }
        
        param.setCarrierName(AppConfigHelper.mandatoryProgramCode);        
        return param;
    }

    public void setRequestedOrientation(int requestedOrientation)
    {
        if(AppConfigHelper.isTabletSize())
        {
            super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return;
        }
        
        super.setRequestedOrientation(requestedOrientation);
    }
    
    public int getRequestedOrientation()
    {
        return super.getRequestedOrientation();
    }

    public void setBackgroundDrawable(final Drawable drawable)
    {
        this.runOnUiThread(new Runnable()
        {
            
            public void run()
            {
                getWindow().setBackgroundDrawable(drawable);
            }
        });
    }

    public void setBackgroundDrawableResource(final int resid)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                getWindow().setBackgroundDrawableResource(resid);        
            }
        });
    }

    public boolean isES2Supported()

    {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        boolean result = configurationInfo.reqGlEsVersion >= 0x20000;

        return result;

    }

    public void initMapContainer()
    {
        final MapContainer cleanMapConatiner = UiFactory.getInstance().getCleanMapContainer(null, ICommonConstants.ID_MAP_CONTAINER, false);
        final TnScreen splashScreen = SplashScreenInflater.getInstance().getSplashScreenView();
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                splashScreen.setBackgroundComponent(cleanMapConatiner);
                showScreen(splashScreen);
            }
        });
    }

    public void snapBitmapCompleted(int buffer[], int width, int height, long transactionId)
    {
        synchronized (this)
        {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            ((AndroidUiHelper) AbstractTnUiHelper.getInstance()).createImage(bitmap, width, height, buffer);
            this.snappedBitmap = bitmap;
            this.notifyAll();
        }
    }

    public void snapBitmapCanceled(long transactionId)
    {
        
    }
    
    public void setBrightness(final float brightness)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Window w = getWindow();
                WindowManager.LayoutParams lp = w.getAttributes();
                if (brightness > 0)
                {
                    lp.screenBrightness = (float) brightness / 100;
                    if (lp.screenBrightness < .01f)
                    {
                        lp.screenBrightness = .01f;
                    }
                }
                else
                {
                    lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                }
                w.setAttributes(lp);
            }
        });
    }
}



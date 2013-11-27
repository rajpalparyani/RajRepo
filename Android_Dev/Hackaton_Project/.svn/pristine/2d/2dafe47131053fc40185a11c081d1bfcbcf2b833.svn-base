/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * CarConnectHostManager.java
 *
 */
package com.telenav.carconnect.host;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.telenav.app.AbstractPlatformIniter;
import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.AndroidPersistentContext;
import com.telenav.app.android.AndroidPlatformIniter;
import com.telenav.carconnect.CarConnectEvent;
import com.telenav.carconnect.CarConnectManager;
import com.telenav.carconnect.mislog.CarConnectMisLogFactory;
import com.telenav.carconnect.mislog.CommonCarConnectMisLog;
import com.telenav.carconnect.provider.AbstractProvider;
import com.telenav.carconnect.provider.CarConnectAddressProvider;
import com.telenav.carconnect.provider.CarConnectEtaProvider;
import com.telenav.carconnect.provider.CarConnectPhoneDialProvider;
import com.telenav.carconnect.provider.CarConnectPoiSearchProvider;
import com.telenav.carconnect.provider.ProtocolConvertor;
import com.telenav.carconnect.provider.applink.AppLinkEncodeDestinationProvider;
import com.telenav.carconnect.provider.applink.AppLinkRouteProvider;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.TripsDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.serverproxy.INetworkStatusListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.NetworkStatusManager;
import com.telenav.data.serverproxy.impl.navsdk.NavSdkNavigationProxy;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.RouteWrapper;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.logger.Logger;
import com.telenav.module.AppConfigHelper;
import com.telenav.module.ModuleFactory;
import com.telenav.module.carconnect.lockout.ScreenLockManager;
import com.telenav.module.carconnect.upsell.CarConnectUpsellController;
import com.telenav.module.entry.EntryController;
import com.telenav.module.entry.secretkey.SecretKeyController;
import com.telenav.module.location.LocationProvider;
import com.telenav.module.login.LoginController;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.module.sync.SyncResController;
import com.telenav.module.upsell.FeaturesManager;
import com.telenav.mvc.AbstractController;
import com.telenav.mvc.ICommonConstants;
import com.telenav.mvc.Parameter;
import com.telenav.navsdk.events.PointOfInterestData.PointOfInterest;
import com.telenav.tnui.core.android.AndroidUiHelper;
import com.telenav.tnui.graphics.AbstractTnGraphicsHelper;

/**
 * Claqss to manage CarConnect host environment and provide API for rest TN client components to interact with
 * CarConnect component.
 * 
 * @author chihlh
 * @date Mar 1, 2012
 */
public class CarConnectHostManager implements INetworkStatusListener
{
    private final static CarConnectHostManager sInstance = new CarConnectHostManager();

    private final static int STATE_INIT = -1; // just started

    public final static int STATE_MONITOR = 0; // host is not ready, but is monitoring external devices.

    private final static int STATE_READY = 1; // host is ready to take external connection

    private final static int STATE_CONNECTED = 2; // host is connected to external devices
    
    private final static int STATE_OFF_BOARD = 1;
    
    private final static int STATE_ON_BOARD = 2;
    
    private final static int STATE_BLUETOOTH_CONNECTED = 1;
    
    private final static int STATE_BLUETOOTH_DISCONNECTED = 2;
    
    private final static int STATE_CAR_CONNECTED = 1;
    
    private final static int STATE_CAR_DISCONNECTED = 2;

    private int state = STATE_INIT;
    
    private int boardState = STATE_OFF_BOARD;
    
    private int bluetoothState = STATE_BLUETOOTH_DISCONNECTED;
    
    private int carConnectState = STATE_CAR_DISCONNECTED;

    private Context ctx;

    private CarConnectionListenerImpl listener = null;

    boolean isLocationAlwaysOn = false;

    private NavSdkNavigationProxy navigationProxy;

    private int count = 0;

    // public final static String INTENT_EXTRA_CAR_CONNECT = "com.telenav.intent.extra.CAR_CONNECT"; // special action
    // for car connect only

    private List<AbstractProvider> providers = new ArrayList<AbstractProvider>();

    public static CarConnectHostManager getInstance()
    {
        return sInstance;
    }

    private CarConnectHostManager()
    {
    }

    private int notifyIcon;

    private final static String NOTIFICATION_TAG = "Telenav.CarConnect";

    private final static int NOTIFICATION_ID = 15783;

    private final static String MSG_CAR_CONNECT_ON = "Car Connect: On";

    private final static String MSG_CAR_CONNECT_OFF = "Car Connect: Off";

    private PendingIntent notifyIntent = null;

    private long sessionStartTime;

    // private final static long WAIT_APP_TIME_OUT = 120; //in seconds
    // public static final String INTENT_EXTRA_CAR_CONNECT = "com.telenav.intent.extra.CAR_CONNECT"; // special action
    // for car connect only;

    private void initialize(Context ctx)
    {
        if (state == STATE_INIT)
        {
            this.ctx = ctx.getApplicationContext();
            listener = new CarConnectionListenerImpl();
            setupNotification();
        }
    }

    private void finalizeEnv()
    {
        cancelNotification();
        // clean up stuff that do not interact with CarConnect component
        ctx = null;
        btStatusReceiver = null;
        launchedForCarConnect = false;
        listener = null;
        notifyIcon = 0;
        notifyIntent = null;
        upsellShownTime = 0;
        startCurrentPhoneNav = false;
    }

    private void initNetworkListener()
    {
        if (count >= 1)
        {
            return;
        }
        NetworkStatusManager.getInstance().addStatusListener(this);
        count++;
    }

    /**
     * start to monitor external devices
     * 
     */
    public synchronized void startMonitor(Context ctx)
    {
        bluetoothState = STATE_BLUETOOTH_CONNECTED;
        if (state < STATE_MONITOR)
        {
            // only need to start monitor state when the state is in STATE_INIT
            // Log.i("CarConnect", "startMonitor is called");

            // initialize needed facility
            if (AbstractTnGraphicsHelper.getInstance() == null)
            {
                AbstractTnGraphicsHelper.init(new AndroidUiHelper());

                AbstractTnGraphicsHelper.getInstance().init(ctx.getApplicationContext());
            }

            AndroidPersistentContext.getInstance().init(ctx.getApplicationContext());
            if (AbstractPlatformIniter.getInstance() == null)
            {
                AbstractPlatformIniter.init(new AndroidPlatformIniter());
            }

            AbstractPlatformIniter.getInstance().initIo();

            AppConfigHelper.load();

            initialize(ctx); // since start monitor is one of the entry point, need to do initialization tasks
            
            AbstractPlatformIniter.getInstance().initPersistent();
            AbstractDaoManager.init(new DaoManager());

            initNetworkListener();
            state = STATE_MONITOR;

            CarConnectManager.startMonitor(listener, AppConfigHelper.mandatoryProgramCode);
            // Log.i("CarConnect", "startMonitor - after call the CarConnectMannager.startMonitor - program code = " +
            // AppConfigHelper.mandatoryProgramCode);
            registerBTReceiver();
        }
    }

    /**
     *
     */
    public boolean isLocationAlwaysOn()
    {
        return this.isLocationAlwaysOn && (state == STATE_CONNECTED);
    }

    /**
     * stop the monitor process
     */
    public synchronized void stopMonitor()
    {
        if (state == STATE_MONITOR)
        {
            // TODO: Do we need to call this method? and when do we need to call this method?
            // we only need to do this only if the host is in monitor state
            // Log.i("CarConnect", "stopMonitor is called");

            unregisterBTReceiver();
            state = STATE_INIT;
            finalizeEnv(); // do a clean up job if necessary
        }
    }

    /**
     * Start the host environment and waiting for connection. It is suppose to be called when app is started for the
     * first time. This is Asynchronous call. the method will return immediately and will not block.
     */
    public synchronized void startHost(Context ctx)
    {
        if (state < STATE_READY)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: startHost called");
            initialize(ctx); // since start monitor is one of the entry point, need to do initialization tasks
            initNetworkListener();
            Thread t = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: startHost thread begin");
                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } // wait for 3 seconds to ensure the system is started properly
                    boolean isAppReady = waitForAppReady(-1); // wait forever to get app ready.
                    if (isAppReady)
                    {
                        synchronized (CarConnectHostManager.this)
                        {
                            if (state < STATE_READY)
                            {
                                state = STATE_READY; // set state before enable external interaction to avoid miss
                                                     // event.
                                CarConnectManager.create(listener, AppConfigHelper.mandatoryProgramCode, checkAccount());
                                registerBTReceiver(); // only register receiver when carconnect component is ready.
                            }
                            // handle requirements if the app is launched in response to carconnect request
                            // (attemptToConnect in Monitor mode)
                            if (launchedForCarConnect && upsellShownTime == 0)
                            {
                                // app is launched in response to carconnect request & no upsell page has been shown in
                                // login process
                                if (checkAccount())
                                {
                                    // upsell is not launched, indicates the user already have the carconnect feature
                                    // and not just purchased it in login process
                                    CarConnectManager.applinkEnabled(true);
                                }
                                else
                                {
                                    // show upsell when it has not shown before
                                    startUpsell();
                                }
                            }
                        }

                    }
                    else
                    {
                        // Should never happen
                    }
                    Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: startHost thread end");
                }
            });
            t.start();
        }
    }

    /**
     * stop the host environment
     * 
     */
    public synchronized void stopHost()
    {
        if (state >= STATE_MONITOR)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: stopHost called");
            stopCarConnect(); // make sure the current connection is terminated
            unregisterBTReceiver();
            CarConnectManager.destroy();
            state = STATE_INIT; // set state after disable external interaction to avoid miss event.

            finalizeEnv();
        }
    }

    /**
     * start car connect session. It is supposed only be called from the onCarConnected call back
     * 
     */
    public synchronized void startCarConnect()
    {
        carConnectState = STATE_CAR_CONNECTED;
        startCarConnectOnMobile();
    }

    private void startCarConnectOnMobile()
    {
        if (isAuthorizedConnected())
        {
            // only allow connection when state is in ready
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: startCarConnect called");
            if (checkAccount())
            {
                // check account again to ensure user has account to connect.
                registerProviders();
                setNotification(true);
                // start LocationProvider if it is off & the system require the Location Always on for the carconnect
                // sesion
                if (!TeleNavDelegate.getInstance().isActivate() && isLocationAlwaysOn)
                {
                    LocationProvider.getInstance().start();
                }

                // Check if is in Nav session
                boolean isInNav = checkNav(true);
                goToHome();
                state = STATE_CONNECTED;

                if (isInNav || startCurrentPhoneNav)
                {
                    // get last trip and inform NavEngine to navigation. It has to be run after the startCarConnect
                    // finished.
                    final TripsDao tripsDao = DaoManager.getInstance().getTripsDao();
                    startCurrentPhoneNav = false; // reset the flag.
                    Thread t = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            transferNav(tripsDao.getLastTrip());
                        }
                    });
                    t.start();
                }

                // log the session

                sessionStartTime = System.currentTimeMillis();
                CommonCarConnectMisLog misLog = CarConnectMisLogFactory.getInstance().creatCarConnectMisLog();
                Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
                { misLog });
                Logger.log(Logger.INFO, this.getClass().getName(),
                    "CarConnect Mislog------:650(TYPE_HEAD_UNIT_CAR_CONNECT) is loggered!");
            }
            else
            {
                // TODO: App log here it is abnormal behavior
            }
        }
    }

    private boolean isAuthorizedConnected()
    {
        return state == STATE_READY && carConnectState == STATE_CAR_CONNECTED && boardState == STATE_OFF_BOARD;
    }

    /**
     * Stop carconnect session
     * 
     */
    public synchronized void stopCarConnect()
    {
        carConnectState = STATE_CAR_DISCONNECTED;
        stopCarConnectOnMobile();
    }

    private void stopCarConnectOnMobile()
    {
        if (state >= STATE_CONNECTED)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: stopCarConnect called");
            state = STATE_READY;
            unregisterProviders();
            setNotification(false);
            unlockScreen();

            if (!TeleNavDelegate.getInstance().isActivate() && isLocationAlwaysOn
                    && !NavRunningStatusProvider.getInstance().isNavRunning())
            {
                // deactivate the LocationProvider if app is in background
                LocationProvider.getInstance().stop();
            }

            CommonCarConnectMisLog misLog = CarConnectMisLogFactory.getInstance().creatCarDisconnectMisLog();
            misLog.setSessionDuration(System.currentTimeMillis() - sessionStartTime);
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { misLog });
            Logger.log(Logger.INFO, this.getClass().getName(),
                "CarConnect Mislog------:651(TYPE_HEAD_UNIT_CAR_DISCONNECT) is loggered!");
        }
    }

    /**
     * Capture biz logic when the carconnect component issue attemptToConnect event. Typically, this is done if user
     * does not have account or account state is unknown.
     */
    public synchronized void attemptToConnect()
    {
        switch (state)
        {
            case STATE_MONITOR:
                launchApp(); // if host is not ready, need to launch app because the app is not started yet.
                break;
            case STATE_READY:
                // app is already started
                if (checkAccount())
                {
                    // should never happen.
                    CarConnectManager.applinkEnabled(true);
                    Logger.log(Logger.WARNING, this.getClass().getName(),
                        "CarConnect: attemptToConnect called even the account already enabled");
                }
                else
                {
                    // show upsell page in case there is no account.
                    startUpsell();
                }
                break;
            default:
                break;
        }
    }

    private boolean startCurrentPhoneNav = false;

    /**
     * Callback for upsell page return.
     * 
     * @param startNav - flag indicate that the subsequest connection to HU should automatically start the current
     *            interrupted navigation session.
     */
    public void upsellCallBack(final boolean startNav)
    {
        // long elapstedTime = System.currentTimeMillis() - lastUpsellRequestTime;
        // TODO - check whether there should a timeout restriction for the upsell page return
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: upsellCallback thread start");
                synchronized (CarConnectHostManager.this)
                {
                    // ensure the host state is ready before calling any CarConnect library code
                    while (state < STATE_READY)
                    {
                        try
                        {
                            CarConnectHostManager.this.wait(100);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (checkAccount())
                    {
                        CarConnectManager.applinkEnabled(true);
                        startCurrentPhoneNav = startNav;
                    }
                    else
                    {
                        CarConnectManager.applinkEnabled(false);
                    }
                }
                Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: upsellCallback thread end");
            }
        });
        t.start();
    }

    /**
     * Launch the app or bring it to foreground and indicate it is launched according to request from carconnect.
     */
    private void launchApp()
    {
        String activityClassName = ctx.getPackageName() + ".TeleNav";
        Intent intent;
        try
        {
            intent = new Intent(ctx, Class.forName(activityClassName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // intent.putExtra(INTENT_EXTRA_CAR_CONNECT, true);
            ctx.startActivity(intent);
            launchedForCarConnect = true;
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Check the received intent is for car connect or not
     */
    /*
     * public boolean isIntentForCarConnect(Intent intent) { return intent.getBooleanExtra(INTENT_EXTRA_CAR_CONNECT,
     * false); }
     */

    /**
     * request navigation in CarConnect
     */
    public synchronized void requestNav(final Address address)
    {
        if (state == STATE_CONNECTED)
        {
            Thread t = new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: requestNav is called");
                    PointOfInterest poi = ProtocolConvertor.convertAddressToPointOfInterest(address).build();
                    CarConnectManager.startNavigation(poi);
                }
            });
            t.start();
        }
    }

    /**
     * request navigation in CarConnect
     */
    public synchronized void transferNav(final Address address)
    {
        if (state == STATE_CONNECTED)
        {
            Thread t = new Thread(new Runnable()
            {

                @Override
                public void run()
                {
                    Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: transferNav is called");
                    PointOfInterest poi = ProtocolConvertor.convertAddressToPointOfInterest(address).build();
                    Route route = RouteWrapper.getInstance().getCurrentRoute();
                    int routeId = (route != null) ? route.getRouteID() : -1;
                    CarConnectManager.transferNavigation(poi, routeId);
                }
            });
            t.start();
        }
    }

    /**
     * lock the screen
     */
    public synchronized void lockScreen()
    {
        if (state == STATE_CONNECTED)
        {
            Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: lockScreen is called");
            ScreenLockManager.getInstance().lockScreen();
        }
    }

    /**
     * unlock the screen
     */
    public void unlockScreen()
    {
        Logger.log(Logger.INFO, this.getClass().getName(), "CarConnect: unlockScreen is called");
        ScreenLockManager.getInstance().unlockScreen();
    }

    /**
     * Capture business logic when there is CarConnect compatible device that is paired & low level connected with the
     * phone
     */
    public synchronized void deviceInRange()
    {
        if (state > STATE_INIT)
        {
            setNotification(false);
        }
    }

    /**
     * Capture business logic when there is CarConnect compatible device that is low level disconnected with the phone
     */
    public synchronized void deviceOutRange()
    {
        if (state > STATE_INIT)
        {
            cancelNotification();
            stopMonitor(); // maybe a good chance to call stop monitor since there is no device near the phone.
        }
    }

    /**
     * test if the CarConnect is active and in connect session.
     */
    public boolean isConnected()
    {
        return (state == STATE_CONNECTED);
    }
    
    public int getState()
    {
        return this.state;
    }

    private boolean launchedForCarConnect = false;

    private int upsellShownTime = 0;

    /**
     * return whether the app is launched according to CarConnect request
     */
    public boolean isLaunchedForCarConnect()
    {
        return launchedForCarConnect;
    }

    /**
     * check whether the nav is ongoing
     * 
     * @param pause - when true, nav will be paused
     * @return true if is in nav
     */
    private boolean checkNav(boolean pause)
    {
        if (NavRunningStatusProvider.getInstance().isNavRunning())
        {
            if (navigationProxy == null)
            {
                navigationProxy = new NavSdkNavigationProxy(null);
                navigationProxy.sendStopNavigationRequest();
            }
            NavRunningStatusProvider.getInstance().setNavRunningEnd();
            return true;
        }
        return false;
    }

    private void registerProviders()
    {
        AbstractProvider ap;
        ap = new CarConnectPoiSearchProvider();
        ap.register();
        providers.add(ap);
        ap = new CarConnectAddressProvider();
        ap.register();
        providers.add(ap);
        ap = new CarConnectEtaProvider();
        ap.register();
        providers.add(ap);
        ap = new AppLinkRouteProvider();
        ap.register();
        providers.add(ap);
        ap = new AppLinkEncodeDestinationProvider();
        ap.register();
        providers.add(ap);
        ap = new CarConnectPhoneDialProvider();
        ap.register();
        providers.add(ap);

        // any providers for tnlink only has to be registered in method TnLinkProviderManager.registerProviders()
        TnLinkProviderManager.registerProviders(providers);
    }

    private void unregisterProviders()
    {
        for (AbstractProvider ap : providers)
        {
            ap.unregister();
        }
        providers.clear();
    }

    /**
     * Check whether the user has account to use carconnect
     * 
     * @return
     */
    public boolean checkAccount()
    {
        int status = FeaturesManager.getInstance().getStatus(FeaturesManager.FEATURE_CODE_CAR_CONNECT);
        return ((status == FeaturesManager.FE_ENABLED) || (status == FeaturesManager.FE_PURCHASED));
    }

    /**
     * Start car connect specific upsell page
     */
    public void startUpsell()
    {
        AbstractController controller = AbstractController.getCurrentController();
        if (controller != null)
        {
            // check Nav, if it is in nav, pause it
            boolean navPaused = checkNav(true);
            IUserProfileProvider userProfileProvider = (IUserProfileProvider) controller.getModel().get(
                ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);

            Parameter data = new Parameter();
            data.put(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER, userProfileProvider);
            data.put(ICommonConstants.KEY_B_IS_NAV_PAUSED, navPaused);
            CarConnectUpsellController newController = new CarConnectUpsellController(controller);
            newController.init(data);

            // ModuleFactory.getInstance().startUpSellController((AbstractCommonController)controller,
            // FeaturesManager.FEATURE_CODE_CAR_CONNECT, false, userProfileProvider);
            upsellShownTime++;
            // lastUpsellRequestTime = System.currentTimeMillis();
        }
    }

    /**
     * log the mis log for poi search event
     */
    public synchronized void logPoiSearch(String searchId)
    {
        if (state == STATE_CONNECTED)
        {
            // only log the event when in connected state
            CommonCarConnectMisLog misLog = CarConnectMisLogFactory.getInstance().createCarConnectPoiSearchMisLog();
            misLog.setSearchId(searchId);
            Logger.log(Logger.INFO, this.getClass().getName(), IMisLogConstants.PROCESS_MISLOG, new Object[]
            { misLog });
            Logger.log(Logger.INFO, this.getClass().getName(),
                "CarConnect Mislog------:652(TYPE_HEAD_UNIT_CONNECTED_POI_SEARCH) is loggered!");
        }
    }

    private void cancelNotification()
    {
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, NOTIFICATION_ID);
    }

    private void setNotification(boolean flag)
    {
        if (notifyIntent != null)
        {
            String title = flag ? MSG_CAR_CONNECT_ON : MSG_CAR_CONNECT_OFF;
            // TODO - put title String under i18n control.
            NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification(notifyIcon, null, System.currentTimeMillis());
            notification.setLatestEventInfo(ctx, title, "", notifyIntent);
            nm.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notification);
        }
    }

    private void setupNotification()
    {
        String pkgName = ctx.getPackageName();
        try
        {
            Class<?> c = Class.forName(pkgName + ".R$drawable");
            notifyIcon = c.getField("app_icon").getInt(null);
            Intent intent = new Intent();
            intent.setClassName(ctx, pkgName + ".TeleNav");
            notifyIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        catch (ClassNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (SecurityException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (NoSuchFieldException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            // this is a chance to clear notification if there are notification residue
            cancelNotification();
        }
    }

    private final static long CHECK_INTERVAL = 300;

    private boolean waitForAppReady(long waitTime)
    {
        // if waitTime < 0 means wait forever
        waitTime = waitTime * 1000L;
        long timeElapsed = 0L;
        boolean isReady = false;
        while (!(isReady = appReady()) && ((waitTime < 0) || (timeElapsed < waitTime)))
        {
            try
            {
                Thread.sleep(CHECK_INTERVAL); // check every 100 ms
                timeElapsed += CHECK_INTERVAL;
            }
            catch (InterruptedException e)
            {
            }
        }
        return isReady;
    }

    private boolean appReady()
    {
        // boolean ret = AbstractDaoManager.getInstance().getBillingAccountDao().isLogin();
        // return ret;
        // Is the following a better way to determine if app is ready?
        // return DaoManager.getInstance().getSimpleConfigDao().getBoolean(SimpleConfigDao.KEY_IS_HOME_LAUCHED);
        AbstractController controller = AbstractController.getCurrentController();
        boolean ret = false;
        if (controller != null && !(controller instanceof EntryController) && !(controller instanceof SecretKeyController)
                && !(controller instanceof LoginController) && !(controller instanceof SyncResController)
                && MisLogManager.getInstance().isLoggerReady())
        {
            ret = true;
            ;
        }

        return ret;
    }

    private void goToHome()
    {
        AbstractController controller = AbstractController.getCurrentController();
        while (controller != null)
        {
            controller.release();
            controller = controller.getSuperController();
        }

        ModuleFactory.getInstance().startMain();
    }

    private static class BTStatusReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (state)
                {
                    case BluetoothAdapter.STATE_ON:
                        CarConnectManager.getEventBus().broadcast(CarConnectEvent.BLUETOOTH_ON, null);
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        CarConnectManager.getEventBus().broadcast(CarConnectEvent.BLUETOOTH_OFF, null);
                        break;
                }
            }
        }
    }

    private BTStatusReceiver btStatusReceiver = null;

    private void registerBTReceiver()
    {
        if (btStatusReceiver == null)
        {
            btStatusReceiver = new BTStatusReceiver();
            IntentFilter ifilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            ctx.registerReceiver(btStatusReceiver, ifilter);
        }
    }

    private void unregisterBTReceiver()
    {
        if (btStatusReceiver != null)
        {
            ctx.unregisterReceiver(btStatusReceiver);
            btStatusReceiver = null;
        }
    }

    @Override
    public void statusUpdate(boolean isConnected)
    {
        if(isConnected)
        {
            boardState = STATE_OFF_BOARD;
            startCarConnectOnMobile();
        }
        else
        {
            boardState = STATE_ON_BOARD;
            stopCarConnectOnMobile();
        }
    
    }

    protected void setBluetoothState(boolean isConnected)
    {
        this.bluetoothState = isConnected ? STATE_BLUETOOTH_CONNECTED : STATE_BLUETOOTH_DISCONNECTED;
    }

    public boolean isBluetoothConnected()
    {
        return bluetoothState == STATE_BLUETOOTH_CONNECTED;
    }
    
}

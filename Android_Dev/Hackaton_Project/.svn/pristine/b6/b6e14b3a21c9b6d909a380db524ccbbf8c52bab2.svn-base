/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AbstractCommonModel.java
 *
 */
package com.telenav.mvc;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import com.telenav.app.TeleNavDelegate;
import com.telenav.app.android.ExitNavSessionConfirmer;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.misc.SimpleConfigDao;
import com.telenav.data.dao.serverproxy.AbstractDaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.dwf.aidl.DwfAidl;
import com.telenav.dwf.aidl.DwfServiceConnection;
import com.telenav.dwf.aidl.Friend.FriendStatus;
import com.telenav.media.TnMediaManager;
import com.telenav.module.dwf.DwfSliderPopup;
import com.telenav.module.nav.NavRunningStatusProvider;
import com.telenav.res.IStringCommon;
import com.telenav.res.ResourceManager;
import com.telenav.sdk.kontagent.KontagentAssistLogger;
import com.telenav.sdk.maitai.IMaiTai;
import com.telenav.sdk.maitai.impl.MaiTaiUtil;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Jul 20, 2010
 */
public abstract class AbstractCommonModel extends AbstractModel
{

    protected final void doAction(int actionId)
    {
        switch(actionId)
        {
            case ICommonConstants.ACTION_EXIT:
            {
                DwfAidl dwfAidl = DwfServiceConnection.getInstance().getConnection();

                if (dwfAidl != null)
                {
                    try
                    {
                        dwfAidl.updateStatus(-1, FriendStatus.END.name(), 0, 0);
                        dwfAidl.stopShareLocation();
                        DwfSliderPopup.getInstance().hide();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
                NavRunningStatusProvider.getInstance().setNavRunningEnd();
                TeleNavDelegate.getInstance().exitApp();
                break;
            }
            case ICommonConstants.ACTION_SWITCH_AUDIO:
            {
                switchAudio();
                break;
            }
            case ICommonConstants.ACTION_EXIT_NAV:
            {
                KontagentAssistLogger.endLogNavigationTimeWhenExit();
                if (ExitNavSessionConfirmer.getInstance().isShowingExitNavConfirm())
                {
                    ExitNavSessionConfirmer.getInstance().endNav();
                    ExitNavSessionConfirmer.getInstance().closePopup();
                }
                else
                {
                    postModelEvent(ICommonConstants.EVENT_MODEL_COMMON_EXIT_NAV);
                }
                break;
            }
            case ICommonConstants.ACTION_RESUME_NAV:
            {
                ExitNavSessionConfirmer.getInstance().closePopup();
                break;
            }
            case ICommonConstants.ACTION_DWF_CHECK_DATA:
            {
                checkDwfData();
                break;
            }
            case ICommonConstants.ACTION_DWF_RESOLVE_URL:
            {
                Thread thread = new Thread(new Runnable()
                {
                    public void run()
                    {
                        resolveTinyUrl();
                    }
                }
                );
                thread.start();
                break;
            }
            case ICommonConstants.ACTION_SHOW_TIMEOUT_MESSAGE:
            {
                this.postModelEvent(ICommonConstants.EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
                break;
            }
            case ICommonConstants.ACTION_EDIT_PLACE_CHECK:
            {
                this.postModelEvent(ICommonConstants.EVENT_MODEL_COMMON_BACK);
                break;
            }
            case ICommonConstants.ACTION_IGNORE_END_DETOUR:
            {
                this.put(ICommonConstants.KEY_B_IS_IGNORE_END_DETOUR, true);
                break;
            }
        }
        
        doActionDelegate(actionId);
    }

    private void resolveTinyUrl()
    {
        boolean isSucc = false;
        Hashtable pluginRequest = (Hashtable) this.get(ICommonConstants.KEY_O_PLUGIN_REQUEST);
        if (pluginRequest != null)
        {
            String tinyUrl = (String) pluginRequest.get(IMaiTai.KEY_DWF_TINY_URL);
            if (tinyUrl != null && tinyUrl.trim().length() > 0)
            {
                String fullContextUrl = getScout4AppUrl(tinyUrl);
                if (fullContextUrl != null)
                {
                    Vector list = MaiTaiUtil.parseRequestUri(fullContextUrl);
                    
                    if (list != null)
                    {
                        String sessionId = null;
                        String userKey = null;
                        String userId = "";
                        String addressDt = null;
                        
                        for(int i = 0; i < list.size(); i++)
                        {
                            String[] pair = (String[])list.get(i);
                            if("session_id".equals(pair[0]))
                            {
                                sessionId = pair[1];
                            }
                            else if("user_key".equals(pair[0]))
                            {
                                userKey = pair[1];
                            }
                            else if("formated_dt".equals(pair[0]))
                            {
                                addressDt = pair[1];
                            }
                            else if("uid".equals(pair[0]))
                            {
                                userId = pair[1];
                            }
                        }
                        
                        if (sessionId != null && userKey != null && userId != null && addressDt != null)
                        {
                            pluginRequest.put(IMaiTai.KEY_DWF_SESSION_ID, sessionId);
                            pluginRequest.put(IMaiTai.KEY_DWF_USER_KEY, userKey);
                            pluginRequest.put(IMaiTai.KEY_DWF_USER_ID, userId);
                            pluginRequest.put(IMaiTai.KEY_DWF_ADDRESS_FORMATDT, addressDt);
                            
                            isSucc = true;
                        }
                    }
                }
            }
        }
        
        if (isSucc)
        {
            this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_LAUNCH_SESSION);
        }
        else
        {
            this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_PARAMETER_ERROR);
        }
    }

    protected void checkDwfData()
    {
        Hashtable pluginRequest = (Hashtable) this.get(ICommonConstants.KEY_O_PLUGIN_REQUEST);
        if (pluginRequest != null)
        {
            String tinyUrl = (String) pluginRequest.get(IMaiTai.KEY_DWF_TINY_URL);
            String sessionId = (String) pluginRequest.get(IMaiTai.KEY_DWF_SESSION_ID);
            String userKey = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_KEY);
            String userId = (String) pluginRequest.get(IMaiTai.KEY_DWF_USER_ID);
            String addressDt = (String) pluginRequest.get(IMaiTai.KEY_DWF_ADDRESS_FORMATDT);
            
            if (tinyUrl != null)
            {
                this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_RESOLVE_TINY_URL);
            }
            else if (sessionId != null && userKey != null && userId != null && addressDt != null)
            {
                this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_LAUNCH_SESSION);
            }
            else
            {
                this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_PARAMETER_ERROR);
            }
        }
        else
        {
            this.postModelEvent(ICommonConstants.EVENT_MODEL_DWF_PARAMETER_ERROR);
        }
    }

    protected void handleAccountFatalError()
    {
        //clear current information firstly.
        //and then let user relogin.
        DaoManager.getInstance().clearInternalRMS();
        TeleNavDelegate.getInstance().exitApp();
    }

    protected void switchAudio()
    {
        int audioPath = TnMediaManager.getInstance().getAudioPath();
        if(audioPath == TnMediaManager.PATH_BLUETOOTH_HEADSET)
        {
            TnMediaManager.getInstance().setAudioPath(TnMediaManager.PATH_SPEAKER_PHONE);
        }
        else
        {
            if(TnMediaManager.getInstance().isBluetoothHeadsetOn())
            {
                TnMediaManager.getInstance().setAudioPath(TnMediaManager.PATH_BLUETOOTH_HEADSET);
            }
            else
            {
                TnMediaManager.getInstance().setAudioPath(TnMediaManager.PATH_SPEAKER_PHONE);
                String msg = ResourceManager.getInstance().getCurrentBundle().getString(IStringCommon.RES_NO_BT_SUPPORT,
                    IStringCommon.FAMILY_COMMON);
                this.put(ICommonConstants.KEY_S_COMMON_MESSAGE, msg);
                this.postModelEvent(ICommonConstants.EVENT_MODEL_SHOW_TIMEOUT_MESSAGE);
            }
        }
    }

    public String getRegion()
    {
        IUserProfileProvider userProfileProvider = (IUserProfileProvider) get(ICommonConstants.KEY_O_USER_PROFILE_PROVIDER);
        MandatoryProfile profile;
        if (userProfileProvider != null)
        {
            profile = userProfileProvider.getMandatoryNode();
        }
        else
        {
            MandatoryNodeDao mandatoryNode = DaoManager.getInstance()
                    .getMandatoryNodeDao();
            profile = mandatoryNode.getMandatoryNode();
        }
        return profile.getUserInfo().region;        
    }
    
   protected boolean isMapCopyrightChanged()
    {
        boolean isCopyRightChanged = false;

        String prevMapCopyRight = AbstractDaoManager.getInstance().getStartupDao().getMapCopyright();
        String latestMapCopyRight = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_MAP_COPYRIGHT);

        if (latestMapCopyRight != null && !prevMapCopyRight.equalsIgnoreCase(latestMapCopyRight))
        {
            isCopyRightChanged = true;
        }
        return isCopyRightChanged;
    }
        
    protected void setMapCopyright()
    {
        String mapCopyright = DaoManager.getInstance().getSimpleConfigDao().getString(SimpleConfigDao.KEY_MAP_COPYRIGHT);
        DaoManager.getInstance().getStartupDao().setMapCopyright(mapCopyright);
        DaoManager.getInstance().getStartupDao().store();
        DaoManager.getInstance().getSimpleConfigDao().remove(SimpleConfigDao.KEY_MAP_COPYRIGHT);
        DaoManager.getInstance().getSimpleConfigDao().store();
    }
    
    protected Vector getControllerInStack(String className)
    {
        Vector matches = new Vector();
        AbstractController controller = AbstractController.getCurrentController();
        while (controller != null)
        {
            if (controller.getClass().getName().equalsIgnoreCase(className))
            {
                matches.addElement(controller);
            }
            controller = controller.getSuperController();
        }
        return matches;
    }
    
    protected String getScout4AppUrl(String tinyUrl)
    {
        if (tinyUrl == null || tinyUrl.trim().length() == 0)
        {
            return "";
        }

        HttpURLConnection con = null;
        int responseCode = -1;
        try
        {
            con = (HttpURLConnection) (new URL(tinyUrl).openConnection());
            con.setInstanceFollowRedirects(false);
            con.connect();
            responseCode = con.getResponseCode();
            //System.out.println(responseCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (responseCode == 301 || responseCode == 302)
        {
            String location = con.getHeaderField("Location");
            //System.out.println(location);
            if (location != null && location.indexOf("apps.scout.me") == -1)
            {
                return getScout4AppUrl(location);
            }
            else
            {
                return location;
            }
        }
        else
        {
            return tinyUrl;
        }
    }
    
    protected abstract void doActionDelegate(int actionId);
}

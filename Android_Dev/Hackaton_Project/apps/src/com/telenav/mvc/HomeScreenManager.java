package com.telenav.mvc;

import com.telenav.app.android.ShortcutManager;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.module.ModuleFactory;
import com.telenav.mvc.AbstractCommonView.BottomContainerArgs;
import com.telenav.res.IStringCommon;
import com.telenav.ui.ImageDecorator;

public class HomeScreenManager {
	
	public static int getHomeScreenEventID()
	{
		boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
		return isStartupMap ? ICommonConstants.EVENT_CONTROLLER_LINK_TO_MAP : ICommonConstants.EVENT_CONTROLLER_LINK_TO_AC;
	}
	
	public static int getHomeScreenAcFromType()
	{
		boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
		return isStartupMap ? ICommonConstants.TYPE_AC_FROM_NAV : ICommonConstants.TYPE_AC_FROM_ENTRY;
	}
	
	public static int getHomeMapFromType()
	{
		boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
		return isStartupMap ? ICommonConstants.TYPE_MAP_FROM_ENTRY : ICommonConstants.TYPE_MAP_FROM_AC;
	}

	public static void goToHomeScreen(AbstractCommonController controller, boolean isFirstTime, boolean isNeedPauseLater) 
	{
		boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
		
		ShortcutManager.getInstance().setInShortcutScreen(false);
		
		if (isStartupMap)
		{
			ModuleFactory.getInstance().startMapController(controller, ICommonConstants.TYPE_MAP_FROM_ENTRY, null, null, null);
		}
		else
		{
			ModuleFactory.getInstance().startDashboardController(controller, isFirstTime);
		}
	}
	
	public static void initBottomContainerArgs(BottomContainerArgs args)
	{
		if (null == args || null == args.cmdIds || null == args.displayStrResIds || null == args.focusImageAdapters || null == args.unfocusImageAdapters)
		{
			return;
		}
		boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
		if (isStartupMap)
		{
			args.cmdIds[0] = ICommonConstants.CMD_COMMON_LINK_TO_MAP;
            args.cmdIds[1] = ICommonConstants.CMD_COMMON_LINK_TO_AC;
            args.displayStrResIds[0] = IStringCommon.RES_BTTN_MAP;
            args.displayStrResIds[1] = IStringCommon.RES_BTTN_DRIVETO;
            args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_MAP_UNFOCUS;
            args.unfocusImageAdapters[1] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_UNFOCUS;
            args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_MAP_FOCUS;
            args.focusImageAdapters[1] = ImageDecorator.IMG_BOTTOM_BAR_DRIVE_FOCUS;
		}
		else
		{
			args.cmdIds[0] = ICommonConstants.CMD_COMMON_LINK_TO_AC;
        	args.cmdIds[1] = ICommonConstants.CMD_COMMON_LINK_TO_MAP;
        	args.displayStrResIds[0] = IStringCommon.RES_BTTN_DASHBOARD;
        	args.displayStrResIds[1] = IStringCommon.RES_BTTN_MAP;
        	args.unfocusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DASHBOARD_UNFOCUS;
        	args.unfocusImageAdapters[1] = ImageDecorator.IMG_BOTTOM_BAR_MAP_UNFOCUS;
        	args.focusImageAdapters[0] = ImageDecorator.IMG_BOTTOM_BAR_DASHBOARD_FOCUS;
        	args.focusImageAdapters[1] = ImageDecorator.IMG_BOTTOM_BAR_MAP_FOCUS;
		}
	}

	public static boolean isDriveTo(AbstractController controller, int acType)
	{
	    boolean isStartupMap = DaoManager.getInstance().getServerDrivenParamsDao().isStartupMap();
	    boolean isDriveTo = false;
	    if (isStartupMap)
	    {
	        AbstractController superController = controller.getSuperController();
	        while (superController != null)
	        {
//	            if (superController instanceof DashboardAttController)
//	            {
//	                isDriveTo = true;
//	                break;
//	            }
	            
	            superController = superController.getSuperController();
	        }
	    }
	    else
	    {
	        if (acType == ICommonConstants.TYPE_AC_FROM_DASHBOARD || acType == ICommonConstants.TYPE_AC_FROM_NAV)
	        {
	            isDriveTo = true;
	        }
	    }
	    
	    return isDriveTo;
	}
}

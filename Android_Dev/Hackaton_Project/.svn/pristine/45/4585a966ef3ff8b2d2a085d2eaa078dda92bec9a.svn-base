/**
 *
 * Copyright 2012 TeleNav, Inc. All rights reserved.
 * ModuleFactory.java
 *
 */
package com.telenav.carconnect.provider.tnlink.module;


import com.telenav.carconnect.provider.tnlink.module.nav.navigation.INavigationConstants;
import com.telenav.carconnect.provider.tnlink.module.nav.navigation.NavigationController;
import com.telenav.carconnect.provider.tnlink.module.nav.routeplan.RoutePlanController;
import com.telenav.carconnect.provider.tnlink.module.nav.routeplan.RoutePlanModel;
import com.telenav.carconnect.provider.tnlink.module.traffic.TrafficController;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.mvc.Parameter;

/**
 *@author xiangli
 *@date 2012-2-23
 */
public class ModuleFactory
{
    protected static ModuleFactory instance = new ModuleFactory();
    protected ModuleFactory()
    {
    }
    
    public static ModuleFactory getInstance()
    {
        return instance;
    }
    
    public void startNavigationController(Stop destStop, int routeId, Parameter navParameter)
    {
        NavigationController controller = new NavigationController(navParameter);
        navParameter.put(INavigationConstants.KEY_O_NAVCONTROLLER, controller);
        Parameter parameter = new Parameter();
        parameter.put(INavigationConstants.KEY_I_ROUTENAME, routeId);
        parameter.put(INavigationConstants.KEY_O_DEST_STOP, destStop);
        controller.init(parameter);
        controller.start(INavigationConstants.ACTION_NAVIGATION_START);
    }
    
    public void startRoutePlanController(Address origAddr, Address destAddr, int routeStyle, int routeSettings, Parameter navParameter)
    {
        RoutePlanController controller = new RoutePlanController(navParameter);
        
        Parameter parameter = new Parameter();
        parameter.put(RoutePlanModel.KEY_I_ROUTE_STYLE, routeStyle);
        parameter.put(RoutePlanModel.KEY_I_ROUTE_SETTINGS, routeSettings);
        parameter.put(RoutePlanModel.KEY_O_ADDRESS_ORI, origAddr);
        parameter.put(RoutePlanModel.KEY_O_ADDRESS_DEST, destAddr);
        controller.init(parameter);
        controller.start(0);
    }
    
    /*public void startAcController(PointOfInterestEvents.ReverseGeocodeRequest request)
    {
        AcController controller = new AcController();
        Parameter parameter = new Parameter();
        TnLocation location = new TnLocation("");
        location.setLatitude((int) request.getLoc().getLatitude());
        location.setLongitude((int) request.getLoc().getLongitude()); 
        parameter.put(IAcConstants.KEY_O_LOCATION, location);
        controller.init(parameter);
        controller.start(0);
    }
    */
    public void startTrafficController(int routeId)
    {
        TrafficController controller = new TrafficController();
        Parameter parameter = new Parameter();
        parameter.put(INavigationConstants.KEY_I_ROUTENAME, routeId);
        controller.init(parameter);
    }
    
}

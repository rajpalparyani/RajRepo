package com.telenav.nav.spt;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.route.Route;
import com.telenav.datatypes.route.SptTree;

public interface ISptDataHandler 
{
	public void handleSptData(SptTree sptTree, boolean isForwardRoute);
	
	public void setRouteData(Route[] routes, byte[] routeData, boolean isForwardRoute);
	
	public void setTotalOriginEdgeNumber(int totalOriginEdgeNumber, boolean isForwardRoute);
	
	public void setDest(Stop stop, boolean isForwardRoute);
	
	public void addMapSectionData(byte[] mapSectionData, boolean isForwardRoute);
	
    public void notifySptTransactionError();
}

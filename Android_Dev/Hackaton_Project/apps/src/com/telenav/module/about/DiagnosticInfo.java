package com.telenav.module.about;


/**
 * 
 * @author chliu
 *
 */
public class DiagnosticInfo 
{
	public static final int ID_LOGIN_ACCOUNT		= 1;
	public static final int ID_SERVICE_PLAN			= 2;
	public static final int ID_GPS_STATUS 			= 3;
	public static final int ID_NETWORK_STATUS 		= 4;
	public static final int ID_LOCATION_PERMISSION 	= 5;
	public static final int ID_NETGUARD_SETTINGS    = 6;
	public static final int ID_DATA_ROAMING_STATUS  = 7;
	public static final int ID_AIRPLANE_MODE		= 8;
	public static final int ID_DATA_SERVICE			= 9;
	public static final int ID_BATTERY_LEVEL		= 10;
	

	
	
	private static DiagnosticInfo instance = new DiagnosticInfo();
	private IDiagnosticInfoProvider provider = null;
	
	public static DiagnosticInfo getInstance()
	{
		return instance;
	}
	
	public void setProvider(IDiagnosticInfoProvider provider)
	{
		this.provider = provider;
	}
	
	
	public DiagnosticItem getDiagnosticItem(int id)
	{
		return provider.getDiagnosticItem(id);
	}
	
	public void updateItem(DiagnosticItem item)
	{
		provider.update(item);
	}
	

	
	
}

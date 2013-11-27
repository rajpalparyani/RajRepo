/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * AndroidHostList.java
 *
 */
package com.telenav.app;


/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 11, 2010
 */
public class HostList
{
	public final static int GROUP_NAME = 0;
	public final static int GROUP_RES_URL = 1;
	public final static String MANUAL_SERVER_NAME = "Manual Server";
	private HostList() 
	{

	}
	
    private final static String[] US_PRODUCTION_PB_INTERNET = 
    {
        "Production Internet",//0
        "http://tn7x-resource.telenav.com/resource-cserver/telenav-server-pb",//2
    };
    
    private final static String[] MANUAL_SERVER = 
    {
        MANUAL_SERVER_NAME,
        ""
    };

    
    //This QA_FP is true QA_FP, the former QA_FP have been renamed to DEV_FP
    private final static String[] US_QA_FEATURE_POOL_PB_INTERNET = 
    {
        "US QA Feature Pool ProtoBuf Internet - 02",//0
        "http://hqs-fptn-androidstudiocsvr02.telenav.com/resource-cserver/telenav-server-pb",//2
    };

    //This QA_FP Backup is true QA_FP Backup, the former QA_FP have been renamed to DEV_FP
    private final static String[] US_QA_FEATURE_POOL_PB_INTERNET_BACKUP = 
    {
        "US QA Feature Pool ProtoBuf Internet - 01",//0
        "http://hqs-fptn-androidstudiocsvr01.telenav.com/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] US_QA_FEATURE_POOL_PB_INTRANET = 
    {
        "US QA Feature Pool ProtoBuf Intranet",//0
        "http://hqs-fptn-androidstudiocsvr02.telenav.com/resource-cserver/telenav-server-pb",//2
    };
    
    private final static String[] US_DEV_PB_INTERNET = 
    {
    	"US DEV ProtoBuf Internet",//0
    	"http://dev-701csvrfp-02:8080/resource-cserver/telenav-server-pb",//2
    };
    
    private final static String[] US_DEV_PB_INTRANET = 
    {
    	"US DEV ProtoBuf Intranet",//0
    	"http://10.224.76.106:8080/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] US_DEV_FEATURE_POOL_PB_INTERNET = 
    {
        "US DEV Feature Pool ProtoBuf Internet",//0
        "http://hqd-fptn-titancsvr.telenav.com:80/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] US_DEV_FEATURE_POOL_PB_INTRANET = 
    {
        "US DEV Feature Pool ProtoBuf Intranet",//0
        "http://10.224.78.79:8080/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] LOAD_TEST_SERVER = 
    {
        "Load Test Server",//0
        "http://10.224.82.73:8080/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] TEST_SERVER_INTERNET = 
    {
        "Test Server Internet",//0
        "http://t-tn60-rim-resource.telenav.com:8080/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] STAGE_SERVER_INTERNET = 
    {
        "Stage Server Internet",//0
        "http://s-tn60-rim-resource.telenav.com:8080/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] LOAD_TEST_SERVER_EXTERNAL_IP = 
    {
        "Load Test Server External IP",//0
        "http://63.237.220.209:80/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] CERT_POOL = 
    {
        "Cert Pool",//0
        "http://hqs-certatt.telenav.com:80/resource-cserver/telenav-server-pb",//2
    };

    private final static String[] US_ROUTING_TEST = 
    {
        "US Routing Test",//0
        "http://hqd-routingcsvr.telenav.com/resource-cserver/telenav-server-pb"//2
    };
    
    public static String[][] getHostUrls()
    {
		return new String[][]
		     {		        
		        US_QA_FEATURE_POOL_PB_INTERNET,
		        US_QA_FEATURE_POOL_PB_INTRANET,
		        US_DEV_FEATURE_POOL_PB_INTERNET,
		        US_DEV_FEATURE_POOL_PB_INTRANET,
		        US_QA_FEATURE_POOL_PB_INTERNET_BACKUP,
		        STAGE_SERVER_INTERNET,
		        US_PRODUCTION_PB_INTERNET,
				US_DEV_PB_INTERNET,
				US_DEV_PB_INTRANET,
				LOAD_TEST_SERVER,
				LOAD_TEST_SERVER_EXTERNAL_IP,
				TEST_SERVER_INTERNET,
				CERT_POOL,
				MANUAL_SERVER,
				US_ROUTING_TEST,
		      };
    }
}

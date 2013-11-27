/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TnNetworkManager.java
 *
 */
package com.telenav.network;

import java.io.IOException;

/**
 * Factory class for creating new Connection objects. The creation of Connections is performed dynamically by looking up
 * a protocol implementation class whose name is formed from the platform name (read from a system property) and the
 * protocol name of the requested connection (extracted from the parameter string supplied by the application
 * programmer.) The parameter string that describes the target should conform to the URL format as described in RFC
 * 2396. This takes the general form: <br />
 * <br />
 * {scheme}:[{target}][{parms}] where {scheme} is the name of a protocol such as http.<br />
 * <br />
 * The {target} is normally some kind of network address.<br />
 * <br />
 * Any {parms} are formed as a series of equates of the form ";x=y". Example: ";type=a".
 * 
 * @author fqming (fqming@telenav.cn)
 *@date Jul 10, 2010
 */
public abstract class TnNetworkManager
{
    /**
     * Access mode
     */
    public static final int READ = 0;

    /**
     * Access mode
     */
    public static final int READ_WRITE = 1;

    /**
     * Access mode
     */
    public static final int WRITE = 2;

    private static TnNetworkManager networkManager;
    private static int initCount;
    
    protected TnNetworkRouter networkRouter;
    
    /**
     * Retrieve the instance of network manager.
     * 
     * @return {@link TnNetworkManager}
     */
    public static TnNetworkManager getInstance()
    {
        return networkManager;
    }

    /**
     * Before invoke the methods of this manager, need init the native manager of the platform first.
     * 
     * @param networkMngr This manager is native manager of platforms. Such as {@link AndroidNetworkManager} etc.
     */
    public synchronized static void init(TnNetworkManager networkMngr)
    {
        if(initCount >= 1)
            return;
        
        networkManager = networkMngr;
        initCount++;
    }

    /**
     * Apply router to network connections
     * Will provide proxy for every url.
     * @param router the router to set
     */
    public void setNetworkRouter(TnNetworkRouter router)
    {
        this.networkRouter = router;
    }
    
    /**
     * Create and open a Connection.
     * 
     * @param name The URL for the connection.
     * @return A new Connection object.
     * @throws IOException if the firewall disallows a connection that is not btspp or comm.
     * @throws IllegalArgumentException If a parameter is invalid.
     * @throws TnConnectionNotFoundException If the connection cannot be found.
     */
    public abstract TnConnection openConnection(String name) throws IOException;

    /**
     * Create and open a Connection.
     * 
     * @param name The URL for the connection.
     * @param access mode. {@link READ}, {@link READ_WRITE}, {@link WRITE}
     * @return A new Connection object.
     * @throws IOException if the firewall disallows a connection that is not btspp or comm.
     * @throws IllegalArgumentException If a parameter is invalid.
     * @throws TnConnectionNotFoundException If the connection cannot be found.
     */
    public abstract TnConnection openConnection(String name, int mode) throws IOException;

    /**
     * Create and open a Connection.
     * 
     * @param name The URL for the connection.
     * @param mode access mode. {@link READ}, {@link READ_WRITE}, {@link WRITE}
     * @param timeouts A flag to indicate that the called wants timeout exceptions.
     * @return A new Connection object.
     * @throws IOException if the firewall disallows a connection that is not btspp or comm.
     * @throws IllegalArgumentException If a parameter is invalid.
     * @throws TnConnectionNotFoundException If the connection cannot be found.
     */
    public abstract TnConnection openConnection(String name, int mode, boolean timeout) throws IOException;
    
    /**
     * Create and open a Connection.
     * 
     * @param name The URL for the connection.
     * @param mode access mode. {@link READ}, {@link READ_WRITE}, {@link WRITE}
     * @param timeouts A flag to indicate that the called wants timeout exceptions.
     * @param proxy the proxy apply to connection.
     * @return A new Connection object.
     * @throws IOException if the firewall disallows a connection that is not btspp or comm.
     * @throws IllegalArgumentException If a parameter is invalid.
     * @throws TnConnectionNotFoundException If the connection cannot be found.
     */
    public abstract TnConnection openConnection(String name, int mode, boolean timeout,TnProxy proxy) throws IOException;
}

/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IValidateAirportProxy.java
 *
 */
package com.telenav.data.serverproxy.impl;

import java.util.Vector;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-21
 */
public interface IValidateAirportProxy
{
    public Vector getSimilarAirports();
    
    public void validateAirport(String airportName, String region);
}

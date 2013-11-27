/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * RimBase64OutputStream.java
 *
 */
package com.telenav.io.rim;

import java.io.OutputStream;

import net.rim.device.api.io.Base64OutputStream;

import com.telenav.io.TnBase64OutputStream;

/**
 *@author fqming (fqming@telenav.cn)
 *@date Aug 13, 2010
 */
class RimBase64OutputStream extends Base64OutputStream implements TnBase64OutputStream
{

    public RimBase64OutputStream(OutputStream os)
    {
        super(os);
    }

}

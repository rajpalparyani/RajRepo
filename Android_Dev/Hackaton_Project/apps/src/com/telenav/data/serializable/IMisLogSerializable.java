/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * IMisLogSerializable.java
 *
 */
package com.telenav.data.serializable;

import com.telenav.log.mis.log.AbstractMisLog;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
public interface IMisLogSerializable
{
    public AbstractMisLog createMisLog(byte[] data);

    public byte[] toBytes(AbstractMisLog log, boolean needLogId, String sessionId);
}

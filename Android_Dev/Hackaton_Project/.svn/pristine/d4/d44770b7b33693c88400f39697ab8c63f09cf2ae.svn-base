/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodeMisLogSerializable.java
 *
 */
package com.telenav.data.serializable.txnode;

import java.util.Enumeration;

import com.telenav.data.serializable.IMisLogSerializable;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;

/**
 *@author fqming (fqming@telenav.cn)
 *@date 2010-12-23
 */
class TxNodeMisLogSerializable implements IMisLogSerializable
{
    public AbstractMisLog createMisLog(byte[] data)
    {
        if (data == null)
            return null;

        Node node = new Node(data, 0);
        AbstractMisLog log = null;
        int size = node.getValuesSize();
        if (size == node.getStringsSize() && size < 2)
        {
            // TODO screwed INode, should be the same number of keys and values
            // event should have must have at least 3 generic attributes
            throw new IllegalArgumentException("unable to parse as mislog");
        }
        if (node.getValueAt(0) != IMisLogConstants.ATTR_EVENT_TYPE_ID || node.getValueAt(1) != IMisLogConstants.ATTR_EVENT_ID
                || node.getValueAt(2) != IMisLogConstants.ATTR_EVENT_TIMESTAMP)
        {
            throw new IllegalArgumentException("unable to parse as mislog");
        }

        int type = Integer.parseInt(node.getStringAt(0));

        log = MisLogManager.getInstance().getFactory().createMisLog(type);
        log.setId(node.getStringAt(1));
        log.setTimestamp(Long.parseLong(node.getStringAt(2)));

        for (int i = 2; i < node.getValuesSize(); i++)
        {
            log.setAttribute(node.getValueAt(i), node.getStringAt(i));
        }

        return log;
    }

    public byte[] toBytes(AbstractMisLog log, boolean needLogId, String sessionId)
    {
        if (log == null)
            return null;

        Node node = new Node();

        node.addValue(IMisLogConstants.ATTR_EVENT_TYPE_ID);
        node.addString(Integer.toString(log.getType()));
        if (needLogId)
        {
            node.addValue(IMisLogConstants.ATTR_EVENT_ID);
            node.addString(log.getId());
        }
        node.addValue(IMisLogConstants.ATTR_EVENT_TIMESTAMP);
        node.addString(Long.toString(log.getTimestamp()));

        Enumeration keys = log.getAttributeKeys();
        if (keys != null)
        {
            while (keys.hasMoreElements())
            {
                Long key = (Long) keys.nextElement();
                node.addValue(key.longValue());
                node.addString(log.getAttribute(key.longValue()));
            }
        }

        if(log.getAttribute(IMisLogConstants.ATTR_SESSION_ID) == null || log.getAttribute(IMisLogConstants.ATTR_SESSION_ID).trim().length() == 0)
        {
            node.addValue(IMisLogConstants.ATTR_SESSION_ID);
            node.addString(sessionId);
        }
        
        return node.toBinary();
    }

}

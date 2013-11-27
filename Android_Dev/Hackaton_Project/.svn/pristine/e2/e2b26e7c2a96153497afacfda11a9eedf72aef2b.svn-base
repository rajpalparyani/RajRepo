/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * MislogDao.java
 *
 */
package com.telenav.data.dao.serverproxy;

import java.util.Enumeration;
import java.util.Vector;

import com.telenav.data.dao.AbstractDao;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.log.mis.IMisLogConstants;
import com.telenav.log.mis.MisLogManager;
import com.telenav.log.mis.log.AbstractMisLog;
import com.telenav.logger.Logger;
import com.telenav.persistent.TnStore;
import com.telenav.sort.SortAlgorithm;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author Casper (pwang@telenav.cn)
 *@date 2010-12-2
 */
public class MisLogDao extends AbstractDao
{
    private static final int SESSION_ID = 700001;

    private TnStore mislogDataWrapper;

    public MisLogDao(TnStore mislogDataWrapper)
    {
        this.mislogDataWrapper = mislogDataWrapper;
    }

    public void store()
    {
        this.mislogDataWrapper.save();
    }

    public void clear()
    {
        this.mislogDataWrapper.clear();
    }

    public String getSessionId()
    {
        if (this.mislogDataWrapper != null)
        {
            byte[] socTypeData = this.mislogDataWrapper.get(SESSION_ID);
            if (socTypeData != null)
            {
                return new String(socTypeData);
            }
        }

        return "";
    }

    public void setSessionId(String sessionId)
    {
        if (sessionId == null)
            return;

        if (this.mislogDataWrapper != null)
        {
            this.mislogDataWrapper.put(SESSION_ID, sessionId.getBytes());
        }
    }

    public void addMisLog(AbstractMisLog log)
    {
        this.mislogDataWrapper.put(log.getId().hashCode(), SerializableManager.getInstance().getMisLogSerializable().toBytes(log, true, MisLogManager.getInstance().getSessionId()));
    }

    public boolean removeMisLog(AbstractMisLog log)
    {
        if (mislogDataWrapper.contains(log.getId().hashCode()))
        {
            this.mislogDataWrapper.remove(log.getId().hashCode());
            return true;
        }
        else
        {
            return false;
        }
    }

    public AbstractMisLog[] getMislog()
    {
        AbstractMisLog[] sent = null;
        int size = mislogDataWrapper.size();
        // in storage we always has sessionID, so it should be more then 1
        // in order to send something to server
        if (size > 1)
        {
            String sessID = getSessionId();
            sent = new AbstractMisLog[size - 1];
            Vector v = new Vector();
            Enumeration e = mislogDataWrapper.keys();
            
            boolean changed = false;
            while (e.hasMoreElements())
            {
                try
                {
                    String keyStr = (String)e.nextElement();
                    Integer key = Integer.valueOf(keyStr);
                    
                    if (key.equals(PrimitiveTypeCache.valueOf(SESSION_ID)))
                        continue;
                    
                    AbstractMisLog log = SerializableManager.getInstance().getMisLogSerializable().createMisLog(
                        mislogDataWrapper.get(key.intValue()));
                    if(log.getAttribute(IMisLogConstants.ATTR_SESSION_ID) == null || log.getAttribute(IMisLogConstants.ATTR_SESSION_ID).trim().length() == 0)
                    {
                        log.setSessionId(sessID);
                    }
                    if(log.getPriority() > IMisLogConstants.PRIORITY_2)
                    {
                        changed = true;
                        this.mislogDataWrapper.remove(log.getId().hashCode());
                    }
                    v.addElement(log);
                }
                catch (Exception e2)
                {
                    Logger.log(this.getClass().getName(), e2, e2.getMessage());
                    continue;
                }
            }
            if(changed)
            {
                this.store();
            }
            if(v.size() > 0)
            {
                sent = new AbstractMisLog[v.size()];
                for (int i = 0; i < v.size(); i++)
                {
                    sent[i] = (AbstractMisLog) v.elementAt(i);
                }
                SortAlgorithm.sort(sent);
            }
        }
        return sent;
    }

}

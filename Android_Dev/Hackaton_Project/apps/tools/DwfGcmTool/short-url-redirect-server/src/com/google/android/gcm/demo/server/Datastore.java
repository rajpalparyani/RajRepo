/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gcm.demo.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

/**
 * Simple implementation of a data store using standard Java collections.
 * <p>
 * This class is neither persistent (it will lost the data when the app is restarted) nor thread safe.
 */
public final class Datastore
{

    static final int MULTICAST_SIZE = 1000;

    private static final String DEVICE_PTN_TYPE = "DeviceWithPtn";
    
    private static final String PTN_MSG_TYPE = "PtnWithMsgArray";

    private static final String DEVICE_TYPE = "Device";

    private static final String DEVICE_PHONE_NO_PROPERTY = "PhoneNo";

    private static final String DEVICE_REG_ID_PROPERTY = "regId";
    
    private static final String PTN_MSG_PROPERTY = "msg";

    private static final String MULTICAST_TYPE = "Multicast";

    private static final String MULTICAST_REG_IDS_PROPERTY = "regIds";

    private static final FetchOptions DEFAULT_FETCH_OPTIONS = FetchOptions.Builder
            .withPrefetchSize(MULTICAST_SIZE).chunkSize(MULTICAST_SIZE);

    private static final Logger logger = Logger.getLogger(Datastore.class.getName());

    private static final DatastoreService datastore = DatastoreServiceFactory
            .getDatastoreService();

    private Datastore()
    {
        throw new UnsupportedOperationException();
    }
    
    public static void saveMsg(String phoneNo, String msg)
    {
        deleteMsg(phoneNo);
        logger.info("Save msg : " + msg + " , to phoneNo. : " + phoneNo);
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity entity = new Entity(PTN_MSG_TYPE);
            entity.setProperty(DEVICE_PHONE_NO_PROPERTY, phoneNo);
            entity.setProperty(PTN_MSG_PROPERTY, msg);
            datastore.put(entity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    public static void registerPhoneNo(String phoneNo, String regId)
    {
        logger.info("Registering phoneNo : " + phoneNo + " , " + regId);
        Transaction txn = datastore.beginTransaction();
        try
        {
            List<Entity> entities = findByPhoneNo(phoneNo);
            if (entities != null && entities.size() > 0)
            {
                logger.fine(phoneNo + " is already registered; add more");
                // return;
            }
            Entity entity = new Entity(DEVICE_PTN_TYPE);
            entity.setProperty(DEVICE_PHONE_NO_PROPERTY, phoneNo);
            entity.setProperty(DEVICE_REG_ID_PROPERTY, regId);
            datastore.put(entity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Unregisters a device.
     * 
     * @param regId device's registration id.
     */
    public static void unregisterPhoneNo(String phoneNo)
    {
        logger.info("Unregistering phoneNo : " + phoneNo);
        List<Entity> entities = findByPhoneNo(phoneNo);
        if (entities == null || entities.size() == 0)
        {
            logger.warning("Device " + phoneNo + " already unregistered");
        }
        else
        {
            int size = entities.size() ;
            logger.info("Found " + size + " entities to unregister ... ");
            for (int i = 0 ; i < size ; i ++)
            {
                String regId = (String)entities.get(i).getProperty(DEVICE_REG_ID_PROPERTY);
                unregister(regId);
            }
        }
    }
    
    public static void deleteMsg(String phoneNo)
    {
        logger.info("deleting msg for phoneNo : " + phoneNo);
        Transaction txn = datastore.beginTransaction();
        try
        {
            List<Entity> msgList = findMsgByPhoneNo(phoneNo);
            if (msgList.size() > 0)
            {
                int size = msgList.size();
                for (int i = 0 ; i < size ; i ++)
                {
                    Key key = msgList.get(i).getKey();
                    datastore.delete(key);
                }
            }
            else
            {
                logger.info("no msg for phoneNo : " + phoneNo);
            }
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Registers a device.
     * 
     * @param regId device's registration id.
     */
    public static void register(String regId)
    {
        logger.info("Registering " + regId);
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity entity = findDeviceByRegId(regId);
            if (entity != null)
            {
                logger.fine(regId + " is already registered; ignoring.");
                return;
            }
            entity = new Entity(DEVICE_TYPE);
            entity.setProperty(DEVICE_REG_ID_PROPERTY, regId);
            datastore.put(entity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Unregisters a device.
     * 
     * @param regId device's registration id.
     */
    public static void unregister(String regId)
    {
        logger.info("Unregistering " + regId);
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity entity = findDeviceByRegId(regId);
            if (entity == null)
            {
                logger.warning("Device " + regId + " already unregistered");
            }
            else
            {
                logger.info("deleting data for previous id");
                Key key = entity.getKey();
                datastore.delete(key);
            }
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Updates the registration id of a device.
     */
    public static void updateRegistration(String oldId, String newId)
    {
        logger.info("Updating " + oldId + " to " + newId);
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity entity = findDeviceByRegId(oldId);
            if (entity == null)
            {
                logger.warning("No device for registration id " + oldId);
                return;
            }
            entity.setProperty(DEVICE_REG_ID_PROPERTY, newId);
            datastore.put(entity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Gets all registered devices.
     */
    public static List<String> getDevices()
    {
        List<String> devices;
        Transaction txn = datastore.beginTransaction();
        try
        {
            Query query = new Query(DEVICE_TYPE);
            Iterable<Entity> entities = datastore.prepare(query).asIterable(
                DEFAULT_FETCH_OPTIONS);
            devices = new ArrayList<String>();
            for (Entity entity : entities)
            {
                String device = (String) entity.getProperty(DEVICE_REG_ID_PROPERTY);
                devices.add(device);
            }
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
        return devices;
    }

    /**
     * Gets all registered devices.
     */
    public static List<String> getPhoneNos()
    {
        List<String> phoneNoList;
        Transaction txn = datastore.beginTransaction();
        try
        {
            Query query = new Query(DEVICE_PTN_TYPE);
            Iterable<Entity> entities = datastore.prepare(query).asIterable(
                DEFAULT_FETCH_OPTIONS);
            phoneNoList = new ArrayList<String>();
            for (Entity entity : entities)
            {
                String phoneNo = (String) entity.getProperty(DEVICE_PHONE_NO_PROPERTY);
                phoneNoList.add(phoneNo);
            }
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
        return phoneNoList;
    }

    /**
     * Gets the number of total devices.
     */
    public static int getTotalDevices()
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Query query = new Query(DEVICE_TYPE).setKeysOnly();
            List<Entity> allKeys = datastore.prepare(query).asList(DEFAULT_FETCH_OPTIONS);
            int total = allKeys.size();
            logger.fine("Total number of devices: " + total);
            txn.commit();
            return total;
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    public static String[] fndDeviceRegIdByPhoneNo(String phoneNo)
    {
        List<Entity> entities = findByPhoneNo(phoneNo);
        if (entities == null || entities.size() == 0)
        {
            return null;
        }
        else
        {
            int size = entities.size() ;
            String[] regIds = new String[size];
            for (int i = 0 ; i < size ; i ++)
            {
                regIds[i] = (String) entities.get(i).getProperty(DEVICE_REG_ID_PROPERTY);
            }
            
            return regIds;
        }
    }

    private static List<Entity> findByPhoneNo(String phoneNo)
    {
        Query query = new Query(DEVICE_PTN_TYPE).addFilter(DEVICE_PHONE_NO_PROPERTY,
            FilterOperator.EQUAL, phoneNo);
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<Entity> entities = preparedQuery.asList(DEFAULT_FETCH_OPTIONS);
        int size = entities.size();
        if (size > 0)
        {
            logger.severe("Found " + size + " entities for phoneNo " + phoneNo + ": "
                    + entities);
        }
        return entities;
    }
    
    private static List<Entity> findMsgByPhoneNo(String phoneNo)
    {
        Query query = new Query(PTN_MSG_TYPE).addFilter(DEVICE_PHONE_NO_PROPERTY,
            FilterOperator.EQUAL, phoneNo);
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<Entity> entities = preparedQuery.asList(DEFAULT_FETCH_OPTIONS);
        
        int size = entities.size();
        logger.severe("Found " + size + " msg for phoneNo " + phoneNo + ": "
                + phoneNo);
        return entities;
    }

    private static Entity findDeviceByRegId(String regId)
    {
        Query query = new Query(DEVICE_PTN_TYPE).addFilter(DEVICE_REG_ID_PROPERTY,
            FilterOperator.EQUAL, regId);
        PreparedQuery preparedQuery = datastore.prepare(query);
        List<Entity> entities = preparedQuery.asList(DEFAULT_FETCH_OPTIONS);
        Entity entity = null;
        if (!entities.isEmpty())
        {
            logger.info("Found " + entities.size() + " device(s) with redId : " + regId);
            entity = entities.get(0);
        }
        else
        {
            logger.info("Found 0 device with redId : " + regId);
        }
        int size = entities.size();
        if (size > 0)
        {
            logger.severe("Found " + size + " entities for regId " + regId + ": "
                    + entities);
        }
        return entity;
    }

    /**
     * Creates a persistent record with the devices to be notified using a multicast message.
     * 
     * @param devices registration ids of the devices.
     * @return encoded key for the persistent record.
     */
    public static String createMulticast(List<String> devices)
    {
        logger.info("Storing multicast for " + devices.size() + " devices");
        String encodedKey;
        Transaction txn = datastore.beginTransaction();
        try
        {
            Entity entity = new Entity(MULTICAST_TYPE);
            entity.setProperty(MULTICAST_REG_IDS_PROPERTY, devices);
            datastore.put(entity);
            Key key = entity.getKey();
            encodedKey = KeyFactory.keyToString(key);
            logger.fine("multicast key: " + encodedKey);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
        return encodedKey;
    }

    /**
     * Gets a persistent record with the devices to be notified using a multicast message.
     * 
     * @param encodedKey encoded key for the persistent record.
     */
    public static List<String> getMulticast(String encodedKey)
    {
        Key key = KeyFactory.stringToKey(encodedKey);
        Entity entity;
        Transaction txn = datastore.beginTransaction();
        try
        {
            entity = datastore.get(key);
            @SuppressWarnings("unchecked")
            List<String> devices = (List<String>) entity
                    .getProperty(MULTICAST_REG_IDS_PROPERTY);
            txn.commit();
            return devices;
        }
        catch (EntityNotFoundException e)
        {
            logger.severe("No entity for key " + key);
            return Collections.emptyList();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Updates a persistent record with the devices to be notified using a multicast message.
     * 
     * @param encodedKey encoded key for the persistent record.
     * @param devices new list of registration ids of the devices.
     */
    public static void updateMulticast(String encodedKey, List<String> devices)
    {
        Key key = KeyFactory.stringToKey(encodedKey);
        Entity entity;
        Transaction txn = datastore.beginTransaction();
        try
        {
            try
            {
                entity = datastore.get(key);
            }
            catch (EntityNotFoundException e)
            {
                logger.severe("No entity for key " + key);
                return;
            }
            entity.setProperty(MULTICAST_REG_IDS_PROPERTY, devices);
            datastore.put(entity);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

    /**
     * Deletes a persistent record with the devices to be notified using a multicast message.
     * 
     * @param encodedKey encoded key for the persistent record.
     */
    public static void deleteMulticast(String encodedKey)
    {
        Transaction txn = datastore.beginTransaction();
        try
        {
            Key key = KeyFactory.stringToKey(encodedKey);
            datastore.delete(key);
            txn.commit();
        }
        finally
        {
            if (txn.isActive())
            {
                txn.rollback();
            }
        }
    }

}

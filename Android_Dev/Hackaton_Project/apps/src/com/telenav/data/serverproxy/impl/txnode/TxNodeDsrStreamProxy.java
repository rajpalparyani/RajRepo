/**
 *
 * Copyright 2010 TeleNav, Inc. All rights reserved.
 * TxNodeDsrStreamProxy.java
 *
 */
package com.telenav.data.serverproxy.impl.txnode;

import java.util.Vector;

import com.telenav.comm.Comm;
import com.telenav.comm.Host;
import com.telenav.data.dao.misc.DaoManager;
import com.telenav.data.dao.serverproxy.MandatoryNodeDao;
import com.telenav.data.dao.serverproxy.ResourceBarDao;
import com.telenav.data.datatypes.address.Address;
import com.telenav.data.datatypes.address.Stop;
import com.telenav.data.datatypes.mandatory.MandatoryProfile;
import com.telenav.data.datatypes.poi.Poi;
import com.telenav.data.serializable.SerializableManager;
import com.telenav.data.serverproxy.IServerProxyConstants;
import com.telenav.data.serverproxy.IServerProxyListener;
import com.telenav.data.serverproxy.IUserProfileProvider;
import com.telenav.data.serverproxy.RequestItem;
import com.telenav.data.serverproxy.impl.IDsrStreamProxy;
import com.telenav.datatypes.audio.AudioData;
import com.telenav.datatypes.audio.AudioDataFactory;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.datatypes.audio.RuleNode;
import com.telenav.logger.Logger;
import com.telenav.module.media.IAudioConstants;
import com.telenav.util.PrimitiveTypeCache;

/**
 *@author bduan
 *@date 2010-12-28
 */
public class TxNodeDsrStreamProxy extends AbstractTxNodeServerProxy implements IDsrStreamProxy
{
    protected Stop anchorStop;
    
    protected int audioFormat;
    
    protected String requestType;
    
    protected String cmd;
    
    protected String[] msgs;
    
    protected Node resultNode;
    
    protected Node audioNode;
    
    protected Node audioPureNode;
    
    protected Node searchAlongAudioNode;
    
    protected long transactionId;
    
    protected Vector missingAudioIds;
    
    protected boolean isBrokenItemMeet;

    protected boolean isOutsideRange;
    
    protected Vector currentAudioPlayList;

    protected Vector allAudioPlayList;

    protected Vector multiMatchList;
    
    protected int currentAudioPlayIndex;
    
    
    public TxNodeDsrStreamProxy(Host host, Comm comm, String type, IServerProxyListener serverProxyListener, IUserProfileProvider userProfileProvider)
    {
        super(host, comm, serverProxyListener, userProfileProvider);
        this.requestType = type;
    }

    protected void addRequestArgs(Node requestNode, Node argNode, RequestItem requestItem)
    {
        //no impl
    }

    protected int parseRequestSpecificData(Node node)
    {
        return 0;
    }

    public void setAnchorStop(Stop stop)
    {
        this.anchorStop = stop;
    }
    
    public void setAudioFormat(int format)
    {
        this.audioFormat = format;
    }
    
    public byte[] getLogData()
    {
        //TODO: db, use real log.
        Node node = new Node();
        return node.toBinary();
    }

    public byte[] getMadantoryData()
    {
        MandatoryNodeDao mandatoryNodeDao = DaoManager.getInstance().getMandatoryNodeDao();
        MandatoryProfile profile = mandatoryNodeDao.getMandatoryNode();
        Node mandatoryNode = new Node(SerializableManager.getInstance().getMandatorySerializable().toBytes(profile), 0);
        
        return mandatoryNode.toBinary();
    }

    public long getRequestSession()
    {
        return 0;
    }

    public String getRequestType()
    {
        if(requestType != null && requestType.length() > 0)
            return requestType;
        
        return IAudioConstants.DSR_RECOGNIZE_COMMAND_CONTROL;
    }

    public byte[] getAnchorStopData()
    {
        if(anchorStop == null)
            return null;
        
        Node node = new Node();
        node.addValue(Stop.TYPE_STOP);//0
        node.addValue(anchorStop.getLat());//1
        node.addValue(anchorStop.getLon());//2
        node.addValue(anchorStop.getType());//3
        node.addValue(anchorStop.getStatus());//4
        node.addValue(anchorStop.isGeocoded() ? 1:0);//5
        node.addValue(0);//6 //hash code
        node.addValue(anchorStop.isSharedAddress() ? 1:0);
     
        String firstLine = anchorStop.getFirstLine();
        
        node.addString(anchorStop.getLabel());//0
        node.addString(firstLine);//1
        node.addString(anchorStop.getCity());//2
        node.addString(anchorStop.getProvince());//3
        node.addString("" + anchorStop.getId());//4
        node.addString(anchorStop.getPostalCode());//5
        node.addString(anchorStop.getCountry());//6
        
        return node.toBinary();
    }

    public int handleRespData(byte[] resp)
    {
        reset();
        if(resp == null)
            return IServerProxyConstants.FAILED;
        
        if(resp != null)
        {
            Node node = new Node(resp, 0);
            
//            System.out.println("DB-Test --- > ");
//            System.out.println(node);
            
            int childrenSize = node.getChildrenSize();
            if(childrenSize <= 0)
            {
                return IServerProxyConstants.FAILED;
            }
            
            if(node.getValuesSize() > 2)
            {
                transactionId = node.getValueAt(2);
            }
            
            resultNode = node.getChildAt(0);
            
            if(childrenSize > 1)
            {
                audioNode = node.getChildAt(1);
                fetchDynamicAudio(audioNode);
            }
            
            if(childrenSize > 2)
            {
                audioPureNode = node.getChildAt(2);
                parseAudio(audioPureNode);
            }
            
            if(childrenSize > 2)
            {
                searchAlongAudioNode = node.getChildAt(3);
            }
            
        }
        
        return IServerProxyConstants.SUCCESS;
    }
    
    public void parseAudio(Node audioNode)
    {
        missingAudioIds = new Vector();
        isOutsideRange = false;

        int type = (int) audioNode.getValueAt(0);
        if (type == IAudioConstants.TYPE_AUDIO_SEQUENCE)
        {
            currentAudioPlayList = new Vector();
            allAudioPlayList = new Vector();

            currentAudioPlayIndex = 0;
            isBrokenItemMeet = false;

            for (int i = 0; i < audioNode.getChildrenSize(); i++)
            {
                Node node1 = audioNode.getChildAt(i);

                if (node1.getValueAt(0) == IAudioConstants.TYPE_MSG_AUDIO)
                {
                    if (!isBrokenItemMeet)
                    {
                        currentAudioPlayList.addElement(convertToAudioNode(node1));
                    }

                    int id = (int) node1.getValueAt(1);

                    if (node1.getBinaryData() != null)
                    {
                        DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, node1.getBinaryData());
                    }
                    else if (DaoManager.getInstance().getResourceBarDao().getAudio(id) == null)
                    {
                        missingAudioIds.addElement(PrimitiveTypeCache.valueOf(id));
                    }

                }
                else if (node1.getValueAt(0) == IAudioConstants.TYPE_AUDIO_PROMPT)
                {
                    addPromptNode(node1);
                }
                else if (node1.getValueAt(0) == IAudioConstants.TYPE_AUDIO_ITEM)
                {
                    int previousPlayListSize = currentAudioPlayList.size();

                    for (int x = 0; x < node1.getChildrenSize(); x++)
                    {
                        Node node2 = node1.getChildAt(x);

                        if (node2.getValueAt(0) == IAudioConstants.TYPE_MSG_AUDIO)
                        {
                            handleMsgAudio(node2);
                        }
                        else if (node2.getValueAt(0) == IAudioConstants.TYPE_AUDIO_PROMPT)
                        {
                            addPromptNode(node2);
                        }
                    }

                    if (isBrokenItemMeet)
                    {
                        // remove incorrect nodes!!!
                        for (int x = currentAudioPlayList.size() - 1; x >= previousPlayListSize; x--)
                        {
                            currentAudioPlayList.removeElementAt(x);
                        }
                    }

                }
            }
        }
    }
    
    private AudioDataNode convertToAudioNode(Node node)
    {
        if(node.getValueAt(0) == IAudioConstants.TYPE_MSG_AUDIO)
        {
            int id = (int) node.getValueAt(1);
    
            //TODO: DB, handle version info later.
//            int version = (int) node.getValueAt(2);
    
            byte[] audio = node.getBinaryData();
    
            if (audio != null)
            {
                DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, audio);
            }
            else
            {
                audio = DaoManager.getInstance().getResourceBarDao().getAudio(id);
            }
            
            if (audio != null)
            {
                currentAudioPlayIndex++;
                AudioData data = AudioDataFactory.getInstance().createAudioData(audio);
                return AudioDataFactory.getInstance().createAudioDataNode(data);
            }
        }
        
        return null;
    }
    
    private void addPromptNode(Node node1)
    {
        int ruleId = (int) node1.getValueAt(1);

        RuleNode rule = DaoManager.getInstance().getResourceBarDao().getAudioRule(ruleId);

        if (rule == null)
        {
            Logger.log(Logger.ERROR, this.getClass().getName(), "Missing rule " + ruleId);
            return;
        }

        Vector intV = new Vector();
        Vector nodeV = new Vector();

        for (int x = 0; x < node1.getChildrenSize(); x++)
        {
            Node node2 = node1.getChildAt(x);

            if (node2.getValueAt(0) == IAudioConstants.TYPE_INT_ARGUMENTS)
            {
                for (int y = 1; y < node2.getValuesSize(); y++)
                {
                    int value = (int) node2.getValueAt(y);
                    intV.addElement(PrimitiveTypeCache.valueOf(value));
                }

            }
            else if (node2.getValueAt(0) == IAudioConstants.TYPE_NODE_ARGUMENTS)
            {
                for (int y = 0; y < node2.getChildrenSize(); y++)
                {
                    nodeV.addElement(node2.getChildAt(y));
                }
            }
        }

        int[] intArgs = new int[intV.size()];
        for (int x = 0; x < intV.size(); x++)
        {
            intArgs[x] = ((Integer) intV.elementAt(x)).intValue();
        }
        AudioDataNode[] nodeArgs = new AudioDataNode[nodeV.size()];

        for (int x = 0; x < nodeV.size(); x++)
        {
            Node node = (Node)nodeV.elementAt(x);
            nodeArgs[x] = convertArgNode(node);
        }

        AudioDataNode node = rule.eval(intArgs, nodeArgs);

        Vector childAudio = new Vector();
        
        addToResponseNode(node, childAudio);
        
        if(multiMatchList == null)
        {
            multiMatchList = new Vector();
        }

        multiMatchList.addElement(childAudio);
    }
    
    protected AudioDataNode convertArgNode(Node node)
    {
        AudioDataNode audioDataNode = null;
        if (node.getValueAt(0) == IAudioConstants.TYPE_MSG_AUDIO)
        {
            int id = (int) node.getValueAt(1);

            byte[] binData = node.getBinaryData();
            if(binData != null)
            {
                audioDataNode = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(binData));
            }
            else
            {
                audioDataNode = AudioDataFactory.getInstance().createAudioDataNode(AudioDataFactory.getInstance().createAudioData(id));
            }
            
            int size = node.getChildrenSize();
            if(size > 0)
            {
                for(int i = 0 ; i < size ; i ++)
                {
                    AudioDataNode childAudioDataNode = convertArgNode(node.getChildAt(i));
                    audioDataNode.addChild(childAudioDataNode);
                }
            }
        }
        
        return audioDataNode;
    }
    
    private void addToResponseNode(AudioDataNode subNode, Vector childVector)
    {
        for (int i = 0; i < subNode.getChildrenSize(); i++)
        {
            AudioDataNode node = subNode.getChild(i);
            if (node.getAudioData() != null)
            {
                node.getAudioData().setDataProvider(DaoManager.getInstance().getResourceBarDao());
                if (node.getAudioData().getResourceId() == -1)
                {
                    addToResponseNode(node, childVector);
                }
                else
                {
                    int id = node.getAudioData().getResourceId();

                    byte[] binData = node.getAudioData().getData();

                    if (binData != null)
                    {
                        DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, binData);
                    }
                    else if (DaoManager.getInstance().getResourceBarDao().getAudio(id) == null)
                    {
                        isBrokenItemMeet = true;
                        missingAudioIds.addElement(PrimitiveTypeCache.valueOf(id));
                    }

                    if (!isOutsideRange)
                    {
                        if (!isBrokenItemMeet)
                        {
                            currentAudioPlayList.addElement(node);
                            childVector.addElement(node);
                        }
                        allAudioPlayList.addElement(node);
                    }
                }
            }
            else
            {
                addToResponseNode(node, childVector);
            }
        }
    }
    
    private void handleMsgAudio(Node msgAudioNode)
    {
        int id = (int) msgAudioNode.getValueAt(1);

        if (id < 0)
        {
            for (int i = 0; i < msgAudioNode.getChildrenSize(); i++)
            {
                Node child = msgAudioNode.getChildAt(i);
                if (child.getValueAt(0) == IAudioConstants.TYPE_MSG_AUDIO)
                {
                    handleMsgAudio(child);
                }
            }
            return;
        }

        if (!isOutsideRange)
        {
            if (!isBrokenItemMeet)
            {
                currentAudioPlayList.addElement(convertToAudioNode(msgAudioNode));
            }
            allAudioPlayList.addElement(msgAudioNode);
        }

        ResourceBarDao resDao = DaoManager.getInstance().getResourceBarDao();

        if (msgAudioNode.getBinaryData() != null)
        {
            resDao.putDynamicAudio(id, msgAudioNode.getBinaryData());
        }
        else if (resDao.getAudio(id) == null)
        {
            isBrokenItemMeet = true;
        }

    }
    
    /**
     * Fetch dynamic audio and save them to cache.
     * 
     * @param audioNode
     */
    public void fetchDynamicAudio(Node audioNode)
    {
        if (audioNode != null)
        {
            int type = (int) audioNode.getValueAt(0);
            if (type == IAudioConstants.TYPE_MSG_AUDIO)
            {
                int id = (int) audioNode.getValueAt(1);

                if (audioNode.getBinaryData() != null)
                {
                    DaoManager.getInstance().getResourceBarDao().putDynamicAudio(id, audioNode.getBinaryData());
                }
            }

            int size = audioNode.getChildrenSize();

            for (int i = 0; i < size; i++)
            {
                Node child = audioNode.getChildAt(i);
                fetchDynamicAudio(child);
            }
        }
    }

    public int getFormat()
    {
        return audioFormat;
    }

    public void reset()
    {
        resultNode = null;
        audioNode = null;
        audioPureNode = null;
        searchAlongAudioNode = null;
        transactionId = -1L;
        currentAudioPlayIndex = 0;
        if(currentAudioPlayList != null)
            currentAudioPlayList.removeAllElements();
        
        if(allAudioPlayList != null)
            allAudioPlayList.removeAllElements();
        
        currentAudioPlayList = null;
        allAudioPlayList = null;
    }

    public Vector getAudio()
    {
        return currentAudioPlayList;
    }

    public String getCommand()
    {
        if(resultNode != null && resultNode.getStringsSize() > 0)
        {
            return resultNode.getStringAt(0);
        }
        
        return "";
    }
    
    public int getCommandType()
    {
        if(resultNode == null || resultNode.getChildrenSize() <= 0)
            return -1;
        
        Node infoNode = resultNode.getChildAt(0);
        if(infoNode.getValuesSize() > 0)
            return (int)infoNode.getValueAt(0);
        
        return -1;
    }
    
    public String getStopName()
    {
        if(resultNode.getChildrenSize() <= 0)
            return "";
        
        Node infoNode = resultNode.getChildAt(0);
        if(infoNode.getStringsSize() > 0)
            return infoNode.getStringAt(0);
        
        return "";
    }

    public String[] getMessages()
    {
        if(resultNode != null && resultNode.getChildrenSize() > 0)
        {
            Node infoNode = resultNode.getChildAt(0);
            int size = infoNode.getStringsSize();
            if(size > 0)
            {
                String[] messages = new String[size];
                for(int i = 0 ; i < size ; i ++)
                {
                    messages[i] = infoNode.getStringAt(i);
                }
                return messages;
            }
        }
        
        return null;
    }

    public AudioDataNode getSearchAlongAudios()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Vector getAddresses()
    {
        if(resultNode == null)
            return null;
        
        return convertToAddress(resultNode);
    }
    
    protected Vector convertToAddress(Node resultNode)
    {
        if(resultNode.getChildrenSize() <= 0)
            return null;
        
        Vector addresses = new Vector();
        for(int i = 0 ; i < resultNode.getChildrenSize(); i ++)
        {
            Node stopInfoNode = resultNode.getChildAt(i);

            if (stopInfoNode.getChildrenSize() <= 0)
                return null;

            Address address = new Address(Address.SOURCE_DSR);
            Node childNode = stopInfoNode.getChildAt(0);
            int value = (int)childNode.getValueAt(0);
            if(value == Stop.TYPE_STOP || value == Stop.TYPE_AIRPORT)
            {
                Stop stop = SerializableManager.getInstance().getAddressSerializable().createStop(childNode.toBinary());
                if (stopInfoNode.getStringsSize() > 1)
                {
                    stop.setLabel(stopInfoNode.getStringAt(1));
                }
                
                if(stop.getFirstLine() != null && stop.getFirstLine().trim().length() > 0)
                {
                    stop.setStreetName(stop.getFirstLine());
                    stop.setHouseNumber("");
                }
                address.setNearbyStop(stop);
            }
            else
            {
                Poi poi = SerializableManager.getInstance().getPoiSerializable().createPoi(childNode.toBinary());
                address.setPoi(poi);
            }

            
            address.setType(Address.TYPE_RECENT_STOP);
            addresses.add(address);
        }
        return addresses;
    }

    public long getTransactionId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Vector getMissingAudios()
    {
        return missingAudioIds;
    }
    
    public Vector getMultiMatchAudios()
    {
        return multiMatchList;
    }
    
}
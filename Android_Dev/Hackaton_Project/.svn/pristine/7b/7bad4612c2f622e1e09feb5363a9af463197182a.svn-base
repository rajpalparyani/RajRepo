package com.telenav.data.serverproxy.impl;

import java.util.Vector;

import com.telenav.data.datatypes.address.Stop;
import com.telenav.datatypes.audio.AudioDataNode;
import com.telenav.dsr.IMetaInfoProvider;

public interface IDsrStreamProxy extends IMetaInfoProvider
{
    //TODO: to be impl
    public void setAnchorStop(Stop stop);
    
    public void setAudioFormat(int format);
    
    public int handleRespData(byte[] resp);
    
    public long getTransactionId();
    
    public String getCommand();
    
    public int getCommandType();
    
    public String[] getMessages();
    
    public Vector getAddresses();
    
    public String getStopName();
    
    public Vector getAudio();
    
    //for multimatch audios.
    public Vector getMultiMatchAudios();
    
    public AudioDataNode getSearchAlongAudios();
    
    public Vector getMissingAudios();
}
